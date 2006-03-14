/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.providerClient.rqdata.ProviderStatusCode;

public class INetVODProviderResp implements Readable
{
	/* Constants */
	public static final Constructor<INetVODProviderResp> CtorDataReader = DataReader.getCtor(INetVODProviderResp.class);

	/* Fields */
	private String fRequestID;
	private ProviderStatusCode fStatusCode;
	private ResponseData fResponseData;

	/* Getters and Setters */
	public ProviderStatusCode getStatusCode() { return fStatusCode; }
	public ResponseData getResponseData() { return fResponseData; }

	/* Construction */
	public INetVODProviderResp(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fRequestID = reader.readString("RequestID", INetVODProviderRqst.RequestIDMaxLength);
		fStatusCode = ProviderStatusCode.convertFromInt(reader.readInt("StatusCode"));
		fResponseData = reader.readObject("ResponseData", ResponseData.CtorDataReader);
	}
}
