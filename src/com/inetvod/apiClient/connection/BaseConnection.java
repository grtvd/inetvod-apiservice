/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection;

import java.io.InputStream;
import java.lang.reflect.Constructor;

import com.inetvod.apiClient.ShowDataList;
import com.inetvod.common.core.FileExtension;
import com.inetvod.common.core.Logger;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.StreamUtil;
import com.inetvod.common.core.XmlDataReader;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public abstract class BaseConnection
{
	/* Fields */
	protected Provider fProvider;
	protected ProviderConnection fProviderConnection;
	protected String fConnectionURL;
	protected static final int TimeoutMillis = 60000;	//TODO: config?

	/* Getters and Setters */

	/* Construction */
	protected BaseConnection(Provider provider, ProviderConnection providerConnection)
	{
		fProvider = provider;
		fProviderConnection = providerConnection;
		fConnectionURL = fProviderConnection.getConnectionURL();
	}

	/* Implementation */
	public abstract ShowDataList process();

	protected <T extends Readable> T sendRequest(String rootElement, Constructor<T> ctorDataReader) throws Exception
	{
		String logFileName = null;

		try
		{
			Logger.logInfo(this, "sendRequest", String.format("Starting processing ConnectionURL(%s) from Provider(%s) on ProviderConnection(%s)",
				fConnectionURL, fProvider.getProviderID(), fProviderConnection.getProviderConnectionID()));

			// Send HTTP request to server
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setParameter("http.socket.timeout", TimeoutMillis);
			//String contentType = "text/xml; charset=ISO-8859-1";
			GetMethod getMethod = new GetMethod(fConnectionURL);
			getMethod.setFollowRedirects(true);

			try
			{
				httpClient.executeMethod(getMethod);
				InputStream responseStream = getMethod.getResponseBodyAsStream();

				InputStream memoryResponseStream = StreamUtil.streamCopyToMemory(responseStream);
				logFileName = Logger.logFile(memoryResponseStream, "connection", FileExtension.xml);

				XmlDataReader dataReader = new XmlDataReader(memoryResponseStream);
				return dataReader.readObject(rootElement, ctorDataReader);
			}
			finally
			{
				getMethod.releaseConnection();
			}
		}
		catch(Exception e)
		{
			Logger.logInfo(this, "sendRequest", String.format("Exception while processing ConnectionURL(%s) from Provider(%s) on ProviderConnection(%s), logged to File(%s)",
				fConnectionURL, fProvider.getProviderID(), fProviderConnection.getProviderConnectionID(), logFileName), e);
			throw e;
		}
	}
}
