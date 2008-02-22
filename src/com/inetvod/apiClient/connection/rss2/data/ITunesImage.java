/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.XmlClassMeta;

@XmlClassMeta(attributeList = {"href"})
public class ITunesImage implements Readable
{
	/* Constants */
	public static final Constructor<ITunesImage> CtorDataReader = DataReader.getCtor(ITunesImage.class);
	private static final int HREFMaxLength = 4096;

	/* Fields */
	private String fHREF;

	/* Getters and Setters */
	public String getHREF() { return fHREF; }

	/* Construction */
	public ITunesImage(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fHREF = reader.readString("href", HREFMaxLength);
	}
}
