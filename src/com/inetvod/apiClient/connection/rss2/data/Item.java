/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
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
	private static final int TitleMaxLength = 128;
	private static final int LinkMaxLength = 4096;
	private static final int GuidMaxLength = 128;
	private static final String DateFormat = "EEE, d MMM yyyy HH:mm:ss Z";
	private static final int DateMaxLength = 32;

	/* Fields */
	private String fTitle;
	private TextItem fMediaTitle;
	private String fLink;
	private Description fDescription;
	private TextItem fMediaDescription;
	private CategoryIDList fCategoryIDList;
	private Enclosure fEnclosure;
	private String fGuid;
	private Date fPubDate;

	private MediaGroup fMediaGroup;
	private MediaContentList fMediaContentList = new MediaContentList();

	/* Getters and Setters */
	public String getTitle() { return fTitle; }
	public TextItem getMediaTitle() { return fMediaTitle; }
	public String getLink() { return fLink; }
	public Description getDescription() { return fDescription; }
	public TextItem getMediaDescription() { return fMediaDescription; }
	public CategoryIDList getCategoryIDList() { return fCategoryIDList; }
	public Enclosure getEnclosure() { return fEnclosure; }
	public String getGuid() { return fGuid; }
	public Date getPubDate() { return fPubDate; }

	public MediaGroup getMediaGroup() { return fMediaGroup; }
	public MediaContentList getMediaContentList() { return fMediaContentList; }

	/* Construction */
	public Item(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fTitle = reader.readString("title", TitleMaxLength);
		fMediaTitle = reader.readObject("media:title", TextItem.CtorDataReader);
		fLink = reader.readString("link", LinkMaxLength);
		fDescription = reader.readObject("description", Description.CtorDataReader);
		fMediaDescription = reader.readObject("media:description", TextItem.CtorDataReader);
		fCategoryIDList = reader.readStringList("category", CategoryID.MaxLength, CategoryIDList.Ctor, CategoryID.CtorString);
		fEnclosure = reader.readObject("enclosure", Enclosure.CtorDataReader);
		fGuid = reader.readString("guid", GuidMaxLength);
		fPubDate = (new SimpleDateFormat(DateFormat)).parse(reader.readString("pubDate", DateMaxLength));

		fMediaGroup = reader.readObject("media:group", MediaGroup.CtorDataReader);
		fMediaContentList = reader.readList("media:content", MediaContentList.Ctor, MediaContent.CtorDataReader);
	}
}
