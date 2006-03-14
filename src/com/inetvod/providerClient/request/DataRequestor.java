/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import com.inetvod.common.core.Logger;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.core.XmlDataReader;
import com.inetvod.common.core.XmlDataWriter;
import com.inetvod.providerClient.rqdata.Authenticate;
import com.inetvod.providerClient.rqdata.ProviderStatusCode;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class DataRequestor
{
	/* Constants */
	private static final String Version = "1.0.0";
	private static final int fPingTimeoutMillis = 5000;			// (5 seconds) Timeout for PingRqst - TODO: move to config file
	private static final int fRequestTimeoutMillis = 30000;		// (30 seconds) Default timeout for requests - TODO: move to config file

	/* Fields */
	private String fRequestURL;
	private String fAdminUserID;
	private String fAdminPassword;
	private String fMemberUserID;
	private String fMemberPassword;
	private ProviderStatusCode fStatusCode;

	/* Getters and Setters */
	public ProviderStatusCode getStatusCode() { return fStatusCode; }

	/* Construction */
	private DataRequestor(String requestURL, String adminUserID, String adminPassword, String memberUserID,
		String memberPassword)
	{
		fRequestURL = requestURL;
		fAdminUserID = adminUserID;
		fAdminPassword = adminPassword;
		fMemberUserID = memberUserID;
		fMemberPassword = memberPassword;
	}

	public static DataRequestor newInstance(String requestURL, String adminUserID, String adminPassword,
		String memberUserID, String memberPassword)
	{
		return new DataRequestor(requestURL, adminUserID, adminPassword, memberUserID, memberPassword);
	}

	/* Implementation */
	private INetVODProviderRqst createHeader(Writeable payload)
	{
		INetVODProviderRqst request = INetVODProviderRqst.newInstance(Version, UUID.randomUUID().toString());

		Authenticate authenticate = Authenticate.newInstance(fAdminUserID, fAdminPassword, fMemberUserID, fMemberPassword);
		request.setAuthenticate(authenticate);

		RequestData requestData = RequestData.newInstance(payload);
		request.setRequestData(requestData);

		return request;
	}

	private Readable sendRequest(Writeable payload, int timeoutMillis)
	{
		INetVODProviderRqst iNetVODProviderRqst;
		INetVODProviderResp iNetVODProviderResp;
		String requestName = "INetVODProviderRqst";
		String responseName = "INetVODProviderResp";

		try
		{
			// create request envelop
			iNetVODProviderRqst = createHeader(payload);

			// Convert 'writeable' to XML
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			XmlDataWriter dataWriter = new XmlDataWriter(printWriter, "UTF-8");
			dataWriter.writeObject(requestName, iNetVODProviderRqst);
			String requestXml = stringWriter.toString();
			//System.out.println(requestXml);

			// Send HTTP request to server
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setParameter("http.socket.timeout", timeoutMillis);
			String contentType = "text/xml; charset=ISO-8859-1";
			PostMethod postMethod = new PostMethod(fRequestURL);
			postMethod.setRequestEntity(new StringRequestEntity(requestXml, contentType, null));

			postMethod.setRequestHeader("Content-type", contentType);
			try
			{
				httpClient.executeMethod(postMethod);
				InputStream responseStream = postMethod.getResponseBodyAsStream();

				// Convert response XML to FieldReadable
				XmlDataReader dataReader = new XmlDataReader(responseStream);
				iNetVODProviderResp = dataReader.readObject(responseName, INetVODProviderResp.CtorDataReader);
				fStatusCode = iNetVODProviderResp.getStatusCode();

				if(iNetVODProviderResp.getResponseData() != null)
					return iNetVODProviderResp.getResponseData().getResponse();
			}
			finally
			{
				postMethod.releaseConnection();
			}
		}
		catch(Exception e)
		{
			Logger.logInfo(this, "sendRequest", e);
			fStatusCode = ProviderStatusCode.sc_ProviderConnectionError;
		}

		return null;
	}

	public ProviderStatusCode pingServer()
	{
		sendRequest(PingRqst.newInstance(), fPingTimeoutMillis);

		if(!ProviderStatusCode.sc_Success.equals(fStatusCode))
			Logger.logInfo(this, "pingServer", String.format("bad StatusCode(%d) returned", ProviderStatusCode.convertToInt(fStatusCode)));
		return fStatusCode;
	}

	public EnrollResp enroll(EnrollRqst enrollRqst)
	{
		return (EnrollResp)sendRequest(enrollRqst, fRequestTimeoutMillis);
	}

	public ShowListResp showList()
	{
		return (ShowListResp)sendRequest(ShowListRqst.newInstance(), fRequestTimeoutMillis);
	}

	public ShowDetailResp showDetail(ShowDetailRqst showDetailRqst)
	{
		return (ShowDetailResp)sendRequest(showDetailRqst, fRequestTimeoutMillis);
	}

	public CheckShowAvailResp checkShowAvail(CheckShowAvailRqst checkShowAvailRqst)
	{
		return (CheckShowAvailResp)sendRequest(checkShowAvailRqst, fRequestTimeoutMillis);
	}

	public RentShowResp rentShow(RentShowRqst rentShowRqst)
	{
		return (RentShowResp)sendRequest(rentShowRqst, fRequestTimeoutMillis);
	}

	public WatchShowResp watchShow(WatchShowRqst watchShowRqst)
	{
		return (WatchShowResp)sendRequest(watchShowRqst, fRequestTimeoutMillis);
	}

	public ReleaseShowResp releaseShow(ReleaseShowRqst releaseShowRqst)
	{
		return (ReleaseShowResp)sendRequest(releaseShowRqst, fRequestTimeoutMillis);
	}
}
