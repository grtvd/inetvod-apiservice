/**
 * Copyright © 2006-2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;
import java.util.Date;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DateUtil;
import com.inetvod.common.core.Logger;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.StrUtil;
import com.inetvod.common.core.StringList;

public class Item implements Readable
{
	/* Constants */
	public static final Constructor<Item> CtorDataReader = DataReader.getCtor(Item.class);
	private static final int TitleMaxLength = 256;
	private static final int LinkMaxLength = 4096;
	private static final int ITunesSummaryMaxLength = Short.MAX_VALUE;
	private static final int CategoryMaxLength = 128;
	private static final int ITunesDurationMaxLength = 16;
	private static final int GuidMaxLength = 256;
	private static final int DateMaxLength = 64;

	/* Fields */
	private String fTitle;
	private TextItem fMediaTitle;
	private String fLink;
	private Description fDescription;
	private TextItem fMediaDescription;
	private String fITunesSummary;
	private StringList fCategoryList;
	private ITunesCategoryList fITunesCategoryList;
	private String fITunesDuration;
	private ITunesExplicit fITunesExplicit;
	private Enclosure fEnclosure;
	private String fGuid;
	private Date fPubDate;
	private MediaThumbnail fMediaThumbnail;

	private MediaGroup fMediaGroup;
	private MediaContentList fMediaContentList;

	/* Getters and Setters */
	public String getTitle() { return fTitle; }
	public TextItem getMediaTitle() { return fMediaTitle; }
	public String getLink() { return fLink; }
	public Description getDescription() { return fDescription; }
	public TextItem getMediaDescription() { return fMediaDescription; }
	public String getITunesSummary() { return fITunesSummary; }
	public StringList getCategoryList() { return fCategoryList; }
	public ITunesCategoryList getITunesCategoryList() { return fITunesCategoryList; }
	public String getITunesDuration() { return fITunesDuration; }
	public ITunesExplicit getITunesExplicit() { return fITunesExplicit; }
	public Enclosure getEnclosure() { return fEnclosure; }
	public String getGuid() { return fGuid; }
	public Date getPubDate() { return fPubDate; }
	public MediaThumbnail getMediaThumbnail() { return fMediaThumbnail; }

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
		fITunesSummary = reader.readString("itunes:summary", ITunesSummaryMaxLength);
		fCategoryList = reader.readStringList("category", CategoryMaxLength, StringList.Ctor, StrUtil.CtorString);
		fITunesCategoryList = reader.readList("itunes:category", ITunesCategoryList.Ctor, ITunesCategory.CtorDataReader);
		fITunesDuration = reader.readString("itunes:duration", ITunesDurationMaxLength);
		fITunesExplicit = ITunesExplicit.convertFromString(reader.readString("itunes:explicit", ITunesExplicit.MaxLength));
		fEnclosure = reader.readObject("enclosure", Enclosure.CtorDataReader);
		fGuid = reader.readString("guid", GuidMaxLength);
		fPubDate = parsePubDate(reader.readString("pubDate", DateMaxLength));
		fMediaThumbnail = reader.readObject("media:thumbnail", MediaThumbnail.CtorDataReader);

		fMediaGroup = reader.readObject("media:group", MediaGroup.CtorDataReader);
		fMediaContentList = reader.readList("media:content", MediaContentList.Ctor, MediaContent.CtorDataReader);
	}

	/**
	 * Some invalid formats from various provider need to be specifically handled
	 */
	private static Date parsePubDate(String dateStr)
	{
		if(!StrUtil.hasLen(dateStr))
			return null;

		Date date = DateUtil.convertFromRFC2822(dateStr);
		if(date != null)
			return date;

		if(dateStr.endsWith("+5000"))
			date = DateUtil.convertFromRFC2822(String.format("%s-0500", dateStr.substring(0, dateStr.length() - 5)));

		if(date == null)
		{
			String[] dateStrParts = dateStr.split("\\,");
			if(dateStrParts.length == 2)
				date = DateUtil.parseDate(dateStrParts[1].trim(), "d MMM yyyy HH:mm:ss Z");
		}

		if(date == null)
			Logger.logErr(Item.class, "parsePubDate", String.format("Can't parse dateStr(%s)", dateStr));
		return date;
	}
}
