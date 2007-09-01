/**
 * Copyright © 2005-2007 iNetVOD, Inc. All Rights Reserved.
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
		catch(Exception e)
		{
			return false;
		}
	}

	private void printUsage()
	{
		System.out.println("usage: apiservice [options] [args]");
		System.out.println("   -p <ProviderID>");
		System.out.println("   -pc <ProviderConnectionID>");
	}

	private void doWork() throws Exception
	{
		Logger.logInfo(this, "doWork", "Start...");

		if(fProviderID != null)
			updateProvider(fProviderID);
		else if(fProviderConnectionID != null)
			updateProviderConnection(fProviderConnectionID);
		else
			updateAllProviders();


		Logger.logInfo(this, "doWork", "Done!");
	}

	private void updateAllProviders() throws Exception
	{
		ProviderList providerList = ProviderList.find();

		for(Provider provider : providerList)
			updateProvider(provider);
	}

	private void updateProvider(Provider provider) throws Exception
	{
		ProviderConnectionList providerConnectionList = ProviderConnectionList.findByProviderID(provider.getProviderID());

		for(ProviderConnection providerConnection : providerConnectionList)
			updateProviderConnection(provider, providerConnection);
	}

	private void updateProvider(ProviderID providerID) throws Exception
	{
		updateProvider(Provider.get(providerID));
	}

	private void updateProviderConnection(Provider provider, ProviderConnection providerConnection) throws Exception
	{
		try
		{
			if(ProviderConnectionType.ProviderAPI.equals(providerConnection.getProviderConnectionType()))
				ProviderShowUpdater.newInstance(provider, providerConnection).doUpdate();
			else
				ConnectionShowUpdater.newInstance(provider, providerConnection).doUpdate();
		}
		catch(Exception e)
		{
			Logger.logErr(this, "updateProviderConnection", String.format("Failed during update of Provider(%s)/ProviderConnection(%s)",
				provider.getProviderID().toString(), providerConnection.getProviderConnectionID().toString()), e);
		}
	}

	private void updateProviderConnection(ProviderConnectionID providerConnectionID) throws Exception
	{
		ProviderConnection providerConnection = ProviderConnection.get(providerConnectionID);
		Provider provider = Provider.get(providerConnection.getProviderID());
		updateProviderConnection(provider, providerConnection);
	}

//	private void testWork() throws Exception
//	{
//		ProviderConnection providerConnection = ProviderConnection.newInstance(new ProviderID("rocketboom"),
//			new ProviderConnectionType("Rss2"));
//		providerConnection.update();
//		providerConnection.setConnectionURL("http://");
//		providerConnection.update();
//		providerConnection.delete();
//
//		ProviderConnection providerConnection = new ProviderConnection();
//		ConnectionShowUpdater connectionShowUpdater = ConnectionShowUpdater.newInstance(providerConnection);
//		connectionShowUpdater.doUpdate();
//
//		Rss20 rss20 = Rss2Connection.newInstance("").process();
//		if(rss20 == null)
//			return;
//		Channel channel = rss20.getChannel();
//		if(channel == null)
//			return;
//
//		System.out.println("Channel");
//		System.out.println(String.format("Title: %s", channel.getTitle()));
//		System.out.println(String.format("Desc: %s", channel.getDescription()));
//
//		for(Item item : channel.getItemList())
//		{
//			System.out.println();
//			System.out.println("Item");
//			System.out.println(String.format("Title: %s", item.getTitle()));
//			System.out.println(String.format("Link: %s", item.getLink()));
//
//			Enclosure enclosure = item.getEnclosure();
//			System.out.println(String.format("url: %s", enclosure.getURL()));
//			System.out.println(String.format("type: %s", enclosure.getType()));
//
//			System.out.println(String.format("Desc: %s", item.getDescription()));
//			System.out.println(String.format("Guid: %s", item.getGuid()));
//		}

		/**************************************************************************************************************/

//		ShowProvider.getDatabaseAdaptor().metaDataCheck();

		/**************************************************************************************************************/

//		Member member = Member.get(new MemberID("F2C3E739-85C9-4B61-B906-230986C656C5"));
//		//Member member = Member.newInstance();
//
//		try
//		{
//			member.update();
//
//			MemberLogon memberLogon = MemberLogon.getCreate(member.getMemberID());
//			memberLogon.setEmail("bobd@bob.com");
//			memberLogon.setPassword("123456");
//			memberLogon.setSecretQuestion("hi");
//			memberLogon.setSecretAnswer("hi");
//			memberLogon.setTermsAcceptedOn(new Date());
//			memberLogon.setTermsAcceptedVersion("1");
//			memberLogon.update();
//			memberLogon.update();
//			memberLogon.delete();
//
//			MemberPrefs memberPrefs = MemberPrefs.getCreate(member.getMemberID());
//			memberPrefs.update();
//			memberPrefs.update();
//			memberPrefs.delete();
//
//			MemberAccount memberAccount = MemberAccount.getCreate(member.getMemberID());
//
//			Address home = new Address();
//			home.setAddrStreet1("1000 Hoy Cir");
//			home.setAddrStreet2("Suite 100");
//			home.setCity("Collegeville");
//			home.setState("PA");
//			home.setPostalCode("19426-4302");
//			home.setCountry(CountryID.US);
//			home.setPhone("610-757-0092");
//			memberAccount.setHomeAddress(home);
//
//			CreditCard creditCard = new CreditCard();
//			creditCard.setNameOnCC("Robert S Davidson");
//			creditCard.setCCType(CreditCardType.Visa);
//			creditCard.setCCNumber("1234-5678-9012-2345");
//			creditCard.setCCSIC("3210");
//			creditCard.setExpireDate(7, 2008);
//			creditCard.setBillingAddress(home);
//			memberAccount.setCreditCard(creditCard);
//
//			Calendar cal = Calendar.getInstance();
//			cal.set(1968, 6, 13, 0, 0, 0);
//			memberAccount.setBirthDate(cal.getTime());
//			memberAccount.update();
//
//			memberAccount = MemberAccount.getCreate(member.getMemberID());
//			if(memberAccount.isNewRecord())
//			{
//				memberAccount.update();
//			}
//			memberAccount.delete();
//		}
//		finally
//		{
//			member.delete();
//		}

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
//		ProviderShowUpdater.getThe().doUpdate(new ProviderID("rocketboom"));
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
