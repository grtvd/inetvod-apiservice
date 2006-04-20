/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.StrUtil;
import com.inetvod.common.core.StringList;

public class Channel implements Readable
{
	/* Constants */
	public static final Constructor<Channel> CtorDataReader = DataReader.getCtor(Channel.class);
	private static final int TitleMaxLength = 64;
	private static final int DescriptionMaxLength = Short.MAX_VALUE;
	private static final int CategoryMaxLength = 128;

	/* Fields */
	private String fTitle;
	private TextItem fMediaTitle;
	private String fDescription;
	private TextItem fMediaDescription;
	private StringList fCategoryList;
	private ITunesCategoryList fITunesCategoryList;
	private ITunesExplicit fITunesExplicit;
	private ItemList fItemList = new ItemList();

	/* Getters and Setters */
	public String getTitle() { return fTitle; }
	public TextItem getMediaTitle() { return fMediaTitle; }
	public String getDescription() { return fDescription; }
	public TextItem getMediaDescription() { return fMediaDescription; }
	public StringList getCategoryList() { return fCategoryList; }
	public ITunesCategoryList getITunesCategoryList() { return fITunesCategoryList; }
	public ITunesExplicit getITunesExplicit() { return fITunesExplicit; }
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
		fMediaTitle = reader.readObject("media:title", TextItem.CtorDataReader);
		fDescription = reader.readString("description", DescriptionMaxLength);
		fMediaDescription = reader.readObject("media:description", TextItem.CtorDataReader);
		//TODO: fLanguage = reader.readString("language", 16);
		fCategoryList = reader.readStringList("category", CategoryMaxLength, StringList.Ctor, StrUtil.CtorString);
		fITunesCategoryList = reader.readList("itunes:category", ITunesCategoryList.Ctor, ITunesCategory.CtorDataReader);
		fITunesExplicit = ITunesExplicit.convertFromString(reader.readString("itunes:explicit", ITunesExplicit.MaxLength));
		fItemList = reader.readList("item", ItemList.Ctor, Item.CtorDataReader);
	}
}
