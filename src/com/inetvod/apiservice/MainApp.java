/**
 * Copyright © 2005-2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiservice;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.inetvod.apiClient.CategoryMapper;
import com.inetvod.apiClient.connection.ConnectionShowUpdater;
import com.inetvod.apiClient.providerapi.ProviderShowUpdater;
import com.inetvod.common.core.Logger;
import com.inetvod.common.crypto.CryptoKeyStore;
import com.inetvod.common.data.ProviderConnectionID;
import com.inetvod.common.data.ProviderConnectionType;
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.dbdata.Category;
import com.inetvod.common.dbdata.DatabaseAdaptor;
import com.inetvod.common.dbdata.Member;
import com.inetvod.common.dbdata.MemberAccount;
import com.inetvod.common.dbdata.MemberLogon;
import com.inetvod.common.dbdata.MemberPrefs;
import com.inetvod.common.dbdata.MemberProvider;
import com.inetvod.common.dbdata.MemberSession;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import com.inetvod.common.dbdata.ProviderConnectionList;
import com.inetvod.common.dbdata.ProviderList;
import com.inetvod.common.dbdata.Rating;
import com.inetvod.common.dbdata.RentedShow;
import com.inetvod.common.dbdata.Show;
import com.inetvod.common.dbdata.ShowCategory;
import com.inetvod.common.dbdata.ShowCategoryList;
import com.inetvod.common.dbdata.ShowProvider;
import com.inetvod.contmgr.mgr.ContentManager;

public class MainApp
{
	/* Fields */
	private static MainApp fMainApp = new MainApp();

	private ProviderID fProviderID;
	private ProviderConnectionID fProviderConnectionID;

	/* Getters and Setters */
	public static MainApp getThe() { return fMainApp; }

	/* Construction */
	private MainApp()
	{
	}

	public static void main(String[] args)
	{
		try
		{
			fMainApp.init();
			if(fMainApp.processArgs(args))
				fMainApp.doWork();
			else
				fMainApp.printUsage();
		}
		catch(Exception e)
		{
			Logger.logErr(fMainApp, "main", e);
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "MethodMayBeStatic" })
	private void init() throws Exception
	{
		//noinspection MismatchedQueryAndUpdateOfCollection
		Properties properties = new Properties();
		FileInputStream propertiesFile = new FileInputStream(new File("apiservice.xml"));
		try
		{
			properties.loadFromXML(propertiesFile);
		}
		finally
		{
			propertiesFile.close();
		}

		Logger.initialize(properties.getProperty("log4j"), properties.getProperty("logdir"));

		DatabaseAdaptor.setDBConnectFile(properties.getProperty("dbconnect"));

		// Preload DatabaseAdaptors
		Provider.getDatabaseAdaptor();
		ProviderConnection.getDatabaseAdaptor();
		Category.getDatabaseAdaptor();
		Rating.getDatabaseAdaptor();
		Member.getDatabaseAdaptor();
		MemberLogon.getDatabaseAdaptor();
		MemberAccount.getDatabaseAdaptor();
		MemberPrefs.getDatabaseAdaptor();
		MemberSession.getDatabaseAdaptor();
		MemberProvider.getDatabaseAdaptor();
		Show.getDatabaseAdaptor();
		ShowProvider.getDatabaseAdaptor();
		ShowCategory.getDatabaseAdaptor();
		RentedShow.getDatabaseAdaptor();

		CryptoKeyStore.load(properties.getProperty("cryptokeystore"));
		CategoryMapper.load(properties.getProperty("categoryMapper"));
		ContentManager.initialize(properties.getProperty("contmgrServiceURL"));
	}

	@SuppressWarnings({"UNUSED_SYMBOL"})
	private boolean processArgs(String[] args)
	{
		try
		{
			if((args == null) || (args.length == 0))
				return true;

			if(args.length != 2)
				return false;

			for(int i = 0; i < args.length; i++)
			{
				if("-p".equals(args[i]))
				{
					if(i < args.length - 1)
					{
						i++;
						fProviderID = new ProviderID(args[i]);
					}
					else
						return false;
				}
				else if("-pc".equals(args[i]))
				{
					if(i < args.length - 1)
					{
						i++;
						fProviderConnectionID = new ProviderConnectionID(args[i]);
					}
					else
						return false;
				}
				else
					return false;
			}

			return true;
		}
		catch(Exception ignore)
		{
			return false;
		}
	}

	@SuppressWarnings({ "MethodMayBeStatic" })
	private void printUsage()
	{
		System.out.println("usage: apiservice [options] [args]");
		System.out.println("   -p <ProviderID>");
		System.out.println("   -pc <ProviderConnectionID>");
	}

	private void doWork() throws Exception
	{
		Logger.logWarn(this, "doWork", "Start...");	// make warning to send notification

		if(fProviderID != null)
			updateProvider(fProviderID, false);
		else if(fProviderConnectionID != null)
			updateProviderConnection(fProviderConnectionID, false);
		else
		{
			// pre-check unconfirmed
			updateAllProviders(true);
			// normal processing
			updateAllProviders(false);
			// post-check unconfirmed
			updateAllProviders(true);
		}

		ShowCategoryList.resetFeatured();

		Logger.logWarn(this, "doWork", "Done!");	// make warning to send notification
	}

	private void updateAllProviders(boolean processUnconfirmedOnly) throws Exception
	{
		ProviderList providerList = ProviderList.find();

		for(Provider provider : providerList)
			updateProvider(provider, processUnconfirmedOnly);
	}

	private void updateProvider(Provider provider, boolean processUnconfirmedOnly) throws Exception
	{
		ProviderConnectionList providerConnectionList = ProviderConnectionList.findByProviderID(provider.getProviderID());

		for(ProviderConnection providerConnection : providerConnectionList)
			updateProviderConnection(provider, providerConnection, processUnconfirmedOnly);
	}

	private void updateProvider(ProviderID providerID, boolean processUnconfirmedOnly) throws Exception
	{
		updateProvider(Provider.get(providerID), processUnconfirmedOnly);
	}

	private void updateProviderConnection(Provider provider, ProviderConnection providerConnection,
		boolean processUnconfirmedOnly) throws Exception
	{
		try
		{
			Logger.logInfo(this, "updateProviderConnection", String.format("Beginning update of Provider(%s)/ProviderConnection(%s)",
				provider.getProviderID().toString(), providerConnection.getProviderConnectionID().toString()));

			if(ProviderConnectionType.ProviderAPI.equals(providerConnection.getProviderConnectionType()))
				ProviderShowUpdater.newInstance(provider, providerConnection).doUpdate();
			else
				ConnectionShowUpdater.newInstance(provider, providerConnection).doUpdate(processUnconfirmedOnly);
		}
		catch(Exception e)
		{
			Logger.logErr(this, "updateProviderConnection", String.format("Failed during update of Provider(%s)/ProviderConnection(%s)",
				provider.getProviderID().toString(), providerConnection.getProviderConnectionID().toString()), e);
		}
	}

	private void updateProviderConnection(ProviderConnectionID providerConnectionID, boolean processUnconfirmedOnly)
		throws Exception
	{
		ProviderConnection providerConnection = ProviderConnection.get(providerConnectionID);
		Provider provider = Provider.get(providerConnection.getProviderID());
		updateProviderConnection(provider, providerConnection, processUnconfirmedOnly);
	}
}
