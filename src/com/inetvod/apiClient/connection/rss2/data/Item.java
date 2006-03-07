/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;

public class Item implements Readable
{
	/* Constants */
	public static final Constructor<Item> CtorDataReader = DataReader.getCtor(Item.class);
	private static final int TitleMaxLength = 64;
	private static final int LinkMaxLength = 4096;
	private static final int GuidMaxLength = 128;
	private static final String DateFormat = "EEE, d MMM yyyy HH:mm:ss Z";
	private static final int DateMaxLength = 32;

	/* Fields */
	private String fTitle;
	private String fLink;
	private Description fDescription;
	private CategoryIDList fCategoryIDList;
	private Enclosure fEnclosure;
	private String fGuid;
	private Date fPubDate;

	/* Getters and Setters */
	public String getTitle() { return fTitle; }
	public String getLink() { return fLink; }
	public CategoryIDList getCategoryIDList() { return fCategoryIDList; }
	public Description getDescription() { return fDescription; }
	public Enclosure getEnclosure() { return fEnclosure; }
	public String getGuid() { return fGuid; }
	public Date getPubDate() { return fPubDate; }

	/* Construction */
	public Item(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fTitle = reader.readString("title", TitleMaxLength);
		fLink = reader.readString("link", LinkMaxLength);
		fDescription = reader.readObject("description", Description.CtorDataReader);
		fCategoryIDList = reader.readStringList("category", CategoryID.MaxLength, CategoryIDList.Ctor, CategoryID.CtorString);
		fEnclosure = reader.readObject("enclosure", Enclosure.CtorDataReader);
		fGuid = reader.readString("guid", GuidMaxLength);
		fPubDate = (new SimpleDateFormat(DateFormat)).parse(reader.readString("pubDate", DateMaxLength));
	}

//	public void writeTo(DataWriter writer) throws Exception
//	{
//		writer.writeString("title", fTitle, 256);
//		writer.writeString("link", fLink, 256);
//		writer.writeObject("description", fDescription);
//		writer.writeStringList("category", fCategoryIDList, CategoryID.MaxLength);
//		writer.writeObject("enclosure", fEnclosure);
//		writer.writeString("guid", fGuid, 256);
//		writer.writeDate("pubDate", fPubDate);
//	}
}
