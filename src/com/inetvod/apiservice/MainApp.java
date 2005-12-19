/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.apiservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.inetvod.common.core.Logger;
import com.inetvod.common.dbdata.Category;
import com.inetvod.common.dbdata.Member;
import com.inetvod.common.dbdata.MemberPrefs;
import com.inetvod.common.dbdata.MemberProvider;
import com.inetvod.common.dbdata.MemberSession;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderList;
import com.inetvod.common.dbdata.Rating;
import com.inetvod.common.dbdata.RentedShow;
import com.inetvod.common.dbdata.SerialNumber;
import com.inetvod.common.dbdata.Show;
import com.inetvod.common.dbdata.ShowCategory;
import com.inetvod.common.dbdata.ShowProvider;
import org.apache.log4j.xml.DOMConfigurator;

public class MainApp
{
	/* Fields */
	private static MainApp fMainApp = new MainApp();

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
		}
		catch(Exception e)
		{
			Logger.logErr(fMainApp, "main", e);
		}
	}

	private void init() throws IOException, InvalidPropertiesFormatException
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

		DOMConfigurator.configure(new File(properties.getProperty("log4j")).toURL());

		// Preload DatabaseAdaptors
		Provider.getDatabaseAdaptor();
		Category.getDatabaseAdaptor();
		Rating.getDatabaseAdaptor();
		Member.getDatabaseAdaptor();
		MemberPrefs.getDatabaseAdaptor();
		SerialNumber.getDatabaseAdaptor();
		MemberSession.getDatabaseAdaptor();
		MemberProvider.getDatabaseAdaptor();
		Show.getDatabaseAdaptor();
		ShowProvider.getDatabaseAdaptor();
		ShowCategory.getDatabaseAdaptor();
		RentedShow.getDatabaseAdaptor();
	}

	@SuppressWarnings({"UNUSED_SYMBOL"})
	private boolean processArgs(String[] args)
	{
		return true;
	}

	private void doWork() throws Exception
	{
		Logger.logInfo(this, "doWork", "Start...");

		updateAllProviders();

		Logger.logInfo(this, "doWork", "Done!");
	}

	private void updateAllProviders() throws Exception
	{
		ProviderList providerList = ProviderList.find();

		for(Provider provider : providerList)
			ProviderShowUpdater.getThe().doUpdate(provider.getProviderID());
	}

//	private void testWork() throws Exception
//	{
//		ShowProvider.getDatabaseAdaptor().metaDataCheck();

		/**************************************************************************************************************/

//		ShowID showID = new ShowID("107d3f17-6a9c-46de-b0dc-2b4757a8dd7d");
//		ProviderID providerID = new ProviderID("moviesmovies");
//
//		ShowProvider showProvider = ShowProvider.findByShowIDProviderID(showID, providerID);
//		if(showProvider != null)
//			showProvider.delete();
//
//		showProvider = ShowProvider.newInstance(showID, providerID, new ProviderShowID("mf123"));
//		ShowCost showCost = new ShowCost();
//		showCost.setShowCostType(ShowCostType.PayPerView);
//		showCost.setCostDisplay("$3.95");
//		showCost.setCost(new Money(CurrencyID.USD, 3.95));
//		showCost.setRentalHours((short)72);
//		showProvider.setShowCost(showCost);
//		showProvider.update();
//
//		showProvider = ShowProvider.findByShowIDProviderID(showID, providerID);
//		showCost = showProvider.getShowCost();
//		showCost.setShowCostType(ShowCostType.Free);
//		showCost.setCostDisplay("Free");
//		showCost.setCost(null);
//		showProvider.update();

		/**************************************************************************************************************/

//		ProviderShowUpdater.getThe().doUpdate(new ProviderID("excellentvideos"));
//		ProviderShowUpdater.getThe().doUpdate(new ProviderID("internetvideos"));
//		ProviderShowUpdater.getThe().doUpdate(new ProviderID("mlb"));
//		ProviderShowUpdater.getThe().doUpdate(new ProviderID("moviesmovies"));
//		ProviderShowUpdater.getThe().doUpdate(new ProviderID("vodflicks"));

//		CreateDataXml.doIt(new ProviderID("moviesmovies"));
//		CreateDataXml.confirm(new ProviderID("moviesmovies"));
//		CreateDataXml.doIt(new ProviderID("excellentvideos"));
//		CreateDataXml.confirm(new ProviderID("excellentvideos"));
//		CreateDataXml.doIt(new ProviderID("internetvideos"));
//		CreateDataXml.confirm(new ProviderID("internetvideos"));
//		CreateDataXml.doIt(new ProviderID("vodflicks"));
//		CreateDataXml.confirm(new ProviderID("vodflicks"));

//		Provider provider = Provider.get(new ProviderID("moviesmovies"));
//		ProviderRequestor providerRequestor = ProviderRequestor.newInstance(provider);
//		if(providerRequestor.pingServer())
//		{
//			ShowIDList showIDList = providerRequestor.showList();
//			ShowDetailList showDetailList = providerRequestor.showDetail(showIDList);
//
//		}
//		else
//			Logger.logErr(this, "doWork", "Can't ping server", null);
//	}
}
