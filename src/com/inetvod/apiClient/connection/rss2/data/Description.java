/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.StrUtil;

public class Description implements Readable
{
	/* Constants */
	public static final Constructor<Description> CtorDataReader = DataReader.getCtor(Description.class);
	private static final int TextMaxLength = Short.MAX_VALUE;

	/* Fields */
//	private String fLink;
//	private String fImage;
	private String fText;

	/* Getters and Setters */
	public String getText() { return fText; }

	/* Construction */
	public Description(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
//		fLink = reader.readString("a", 256);
//		fImage = reader.readString("img", 256);
		fText = StrUtil.removeHtml(reader.readString(null, TextMaxLength));
		if(StrUtil.hasLen(fText))
			fText = fText.replace("\t", " ");
	}
}
