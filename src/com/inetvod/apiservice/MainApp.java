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
	}

	private boolean processArgs(String[] args)
	{
		return true;
	}

	private void doWork() throws Exception
	{
		Logger.logInfo(this, "doWork", "Start...");

		Logger.logInfo(this, "doWork", "Done!");
	}
}
