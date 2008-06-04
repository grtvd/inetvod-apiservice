/**
 * Copyright © 2006-2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.inetvod.apiClient.ShowDataList;
import com.inetvod.common.core.FileExtension;
import com.inetvod.common.core.Logger;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.StrUtil;
import com.inetvod.common.core.StreamUtil;
import com.inetvod.common.core.XmlDataReader;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;

public abstract class BaseConnection
{
	/* Constants */
	private static final int ConnectionTimeoutMillis = 30000;	//TODO: config?
	private static final int SocketTimeoutMillis = 30000;	//TODO: config?

	/* Fields */
	protected Provider fProvider;
	protected ProviderConnection fProviderConnection;
	protected String fConnectionURL;
	private HashMap<String, Boolean> fConfirmPictureMap = new HashMap<String, Boolean>();
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
				int rc = httpClient.executeMethod(getMethod);
				if(rc != HttpStatus.SC_OK)
					throw new Exception(String.format("Bad result(%d) from url(%s)", rc, fConnectionURL));

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

	private static boolean confirmURL(String url)
	{
		try
		{
			// Send HTTP request to server
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setParameter("http.connection.timeout", ConnectionTimeoutMillis);	//http.connection.timeout
			httpClient.getParams().setParameter("http.socket.timeout", SocketTimeoutMillis);	//http.socket.timeout
			HeadMethod headMethod = new HeadMethod(url);
			headMethod.setFollowRedirects(true);

			try
			{
				int rc = httpClient.executeMethod(headMethod);
				if(rc != HttpStatus.SC_OK)
				{
					if(rc != HttpStatus.SC_NOT_FOUND)
						Logger.logWarn(BaseConnection.class, "confirmURL", String.format(
							"Bad result(%d) from url(%s)", rc, url));
					return false;
				}

				return true;
			}
			finally
			{
				headMethod.releaseConnection();
			}
		}
		catch(Exception e)
		{
			try
			{
				new URL(url);	//is in valid URL format?
				Logger.logWarn(BaseConnection.class, "confirmURL",
					String.format("Exception while validating url(%s)", url), e);
			}
			catch(MalformedURLException ignore)
			{
				Logger.logInfo(BaseConnection.class, "confirmURL", String.format("Invalid url(%s)", url));
			}
		}

		return false;
	}

	protected String confirmPicture(String pictureURL)
	{
		if(!StrUtil.hasLen(pictureURL))
			return null;

		Boolean valid = fConfirmPictureMap.get(pictureURL);
		if(valid == null)
		{
			try
			{
				new URL(pictureURL);
			}
			catch(MalformedURLException ignore)
			{
				pictureURL = buildURLFromRelative(pictureURL);
				valid = fConfirmPictureMap.get(pictureURL);
				if(valid != null)
					return valid ? pictureURL : null;
			}

			valid = confirmURL(pictureURL);
			fConfirmPictureMap.put(pictureURL, valid);
		}
		return valid ? pictureURL : null;
	}

	private String buildURLFromRelative(String relativePath)
	{
		try
		{
			URL connectionURL = new URL(fConnectionURL);
			URL fullURL = new URL(connectionURL.getProtocol(), connectionURL.getHost(), relativePath);
			return fullURL.toString();
		}
		catch(MalformedURLException ignore)
		{
			return relativePath;
		}
	}
}
