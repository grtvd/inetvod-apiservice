/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.XmlClassMeta;

@XmlClassMeta(attributeList = {"url"})
public class MediaThumbnail implements Readable
{
	/* Constants */
	public static final Constructor<MediaThumbnail> CtorDataReader = DataReader.getCtor(MediaThumbnail.class);
	private static final int URLMaxLength = 4096;

	/* Fields */
	private String fURL;

	/* Getters and Setters */
	public String getURL() { return fURL; }

	/* Construction */
	public MediaThumbnail(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fURL = reader.readString("url", URLMaxLength);
	}
}
