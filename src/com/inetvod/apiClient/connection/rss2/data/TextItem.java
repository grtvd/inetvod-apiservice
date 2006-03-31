/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.StrUtil;
import com.inetvod.common.core.XmlClassMeta;

@XmlClassMeta(attributeList = {"url", "type"})
public class TextItem implements Readable
{
	/* Constants */
	public static final Constructor<TextItem> CtorDataReader = DataReader.getCtor(TextItem.class);
	private static final int TextMaxLength = Short.MAX_VALUE;

	/* Fields */
	private TextType fTextType;
	private String fText;
	private String fPlainText;

	/* Getters and Setters */
	public TextType getTextType() { return fTextType; }
	public String getText() { return fText; }

	/* Construction */
	public TextItem(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fTextType = TextType.convertFromString(reader.readString("type", TextType.MaxLength));
		fText = reader.readString(null, TextMaxLength);
	}

	public String toString()
	{
		if(!StrUtil.hasLen(fText))
			return null;

		if(fPlainText == null)
			fPlainText = StrUtil.removeHtml(fText);
		return fPlainText;
	}
}
