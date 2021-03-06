/**
 * Copyright � 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.XmlClassMeta;

@XmlClassMeta(attributeList = {"url", "type"})
public class Enclosure implements Readable
{
	/* Constants */
	public static final Constructor<Enclosure> CtorDataReader = DataReader.getCtor(Enclosure.class);
	private static final int URLMaxLength = 4096;
	private static final int TypeMaxLength = 64;

	/* Fields */
	private String fURL;
	private String fType;

	/* Getters and Setters */
	public String getURL() { return fURL; }
	public String getType() { return fType; }

	/* Construction */
	public Enclosure(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fURL = reader.readString("url", URLMaxLength);
		fType = reader.readString("type", TypeMaxLength);
	}
}
