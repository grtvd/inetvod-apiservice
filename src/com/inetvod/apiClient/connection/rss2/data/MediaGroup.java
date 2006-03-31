/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;

public class MediaGroup implements Readable
{
	/* Constants */
	public static final Constructor<MediaGroup> CtorDataReader = DataReader.getCtor(MediaGroup.class);

	/* Fields */
	private TextItem fMediaTitle;
	private TextItem fMediaDescription;
	private MediaContentList fMediaContentList = new MediaContentList();

	/* Getters and Setters */
	public TextItem getMediaTitle() { return fMediaTitle; }
	public TextItem getMediaDescription() { return fMediaDescription; }
	public MediaContentList getMediaContentList() { return fMediaContentList; }

	/* Construction */
	public MediaGroup(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fMediaTitle = reader.readObject("media:title", TextItem.CtorDataReader);
		fMediaDescription = reader.readObject("media:description", TextItem.CtorDataReader);
		fMediaContentList = reader.readList("media:content", MediaContentList.Ctor, MediaContent.CtorDataReader);
	}
}
