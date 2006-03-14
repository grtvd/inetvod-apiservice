/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;


import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;
import com.inetvod.providerClient.rqdata.Authenticate;

public class INetVODProviderRqst implements Writeable
{
	/* Constants */
	private static final int VersionMaxLength = 16;
	public static final int RequestIDMaxLength = 64;

	/* Fields */
	private String fVersion;
	private String fRequestID;
	private Authenticate fAuthenticate;
	private RequestData fRequestData;

	/* Getters and Setters */
	public void setVersion(String version) { fVersion = version; }
	public void setRequestID(String requestID) { fRequestID = requestID; }
	public void setAuthenticate(Authenticate authenticate) { fAuthenticate = authenticate; }
	public void setRequestData(RequestData requestData) { fRequestData = requestData; }

	/* Construction */
	private INetVODProviderRqst(String version, String requestID)
	{
		fVersion = version;
		fRequestID = requestID;
	}

	public static INetVODProviderRqst newInstance(String version, String requestID)
	{
		return new INetVODProviderRqst(version, requestID);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("Version", fVersion, VersionMaxLength);
		writer.writeString("RequestID", fRequestID, RequestIDMaxLength);
		writer.writeObject("Authenticate", fAuthenticate);
		writer.writeObject("RequestData", fRequestData);
	}
}
