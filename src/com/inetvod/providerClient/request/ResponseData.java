/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;

public class ResponseData implements Readable
{
	/* Constants */
	public static final Constructor<ResponseData> CtorDataReader = DataReader.getCtor(ResponseData.class);
	private static final int ResponseTypeMaxLength = 64;

	/* Fields */
	private Readable fResponse;

	/* Construction */
	public ResponseData(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Getters and Setters */
	public Readable getResponse() { return fResponse; }

	/* Implementation */
	@SuppressWarnings({"unchecked"})
	public void readFrom(DataReader reader) throws Exception
	{
		String responseType = reader.readString("ResponseType", ResponseTypeMaxLength);

		Class<Readable> cl = (Class<Readable>)Class.forName(getClass().getPackage().getName() + "." + responseType);
		Constructor<Readable> ctor = cl.getConstructor(new Class[] { DataReader.class });
		fResponse = reader.readObject(responseType, ctor);
	}
}
