/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;

public class Channel implements Readable
{
	/* Constants */
	public static final Constructor<Channel> CtorDataReader = DataReader.getCtor(Channel.class);
	private static final int TitleMaxLength = 64;
	private static final int DescriptionMaxLength = Short.MAX_VALUE;

	/* Fields */
	private String fTitle;
	private String fDescription;
	private ItemList fItemList = new ItemList();

	/* Getters and Setters */
	public String getTitle() { return fTitle; }
	public String getDescription() { return fDescription; }

	public ItemList getItemList() { return fItemList; }

	/* Construction */
	public Channel(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fTitle = reader.readString("title", TitleMaxLength);
		fDescription = reader.readString("description", DescriptionMaxLength);
		//TODO: fLanguage = reader.readString("language", 16);
		fItemList = reader.readList("item", ItemList.Ctor, Item.CtorDataReader);
	}

//	public void writeTo(DataWriter writer) throws Exception
//	{
//		writer.writeString("title", fTitle, TitleMaxLength);
//		writer.writeString("description", fDescription, DescriptionMaxLength);
//		writer.writeList("item", fItemList);
//	}
}
