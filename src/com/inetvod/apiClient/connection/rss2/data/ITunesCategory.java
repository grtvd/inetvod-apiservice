/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.XmlClassMeta;

@XmlClassMeta(attributeList = {"text"})
public class ITunesCategory implements Readable
{
	/* Constants */
	public static final Constructor<ITunesCategory> CtorDataReader = DataReader.getCtor(ITunesCategory.class);
	private static final int TextMaxLength = Short.MAX_VALUE;

	/* Fields */
	private String fText;

	/* Getters and Setters */
	public String getText() { return fText; }

	/* Construction */
	public ITunesCategory(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fText = reader.readString("text", TextMaxLength);
	}

	public String toString()
	{
		return fText;
	}
}
