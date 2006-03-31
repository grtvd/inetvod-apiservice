/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.XmlClassMeta;

@XmlClassMeta(attributeList = {"url", "type", "medium", "duration"})
public class MediaContent implements Readable
{
	/* Constants */
	public static final Constructor<MediaContent> CtorDataReader = DataReader.getCtor(MediaContent.class);
	private static final int URLMaxLength = 4096;
	private static final int TypeMaxLength = 64;

	/* Fields */
	private TextItem fMediaTitle;
	private TextItem fMediaDescription;

	private String fURL;
	private String fType;
	private MediaContentMedium fMedium;
	private Integer fDurationSecs;

	/* Getters and Setters */
	public TextItem getMediaTitle() { return fMediaTitle; }
	public TextItem getMediaDescription() { return fMediaDescription; }

	public String getURL() { return fURL; }
	public String getType() { return fType; }
	public MediaContentMedium getMedium() { return fMedium; }
	public Integer getDurationSecs() { return fDurationSecs; }

	/* Construction */
	public MediaContent(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fMediaTitle = reader.readObject("media:title", TextItem.CtorDataReader);
		fMediaDescription = reader.readObject("media:description", TextItem.CtorDataReader);

		fURL = reader.readString("url", URLMaxLength);
		fType = reader.readString("type", TypeMaxLength);
		fMedium = MediaContentMedium.convertFromString(reader.readString("medium", MediaContentMedium.MaxLength));
		fDurationSecs = reader.readInt("duration");
	}
}
