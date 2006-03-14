/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;

public class RequestData implements Writeable
{
	/* Constants */
	private static final int RequestTypeMaxLength = 64;

	/* Fields */
	private String fRequestType;
	private Writeable fRequest;

	/* Construction */
	private RequestData(Writeable request)
	{
		String[] nameParts = request.getClass().getName().split("\\.");
		fRequestType = nameParts[nameParts.length - 1];
		fRequest = request;
	}

	public static RequestData newInstance(Writeable request)
	{
		return new RequestData(request);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("RequestType", fRequestType, RequestTypeMaxLength);
		writer.writeObject(fRequestType, fRequest);
	}
}
