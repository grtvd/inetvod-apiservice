/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.rqdata;

import java.lang.reflect.Constructor;
import java.util.Date;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.LanguageID;
import com.inetvod.common.core.Readable;
import com.inetvod.common.dbdata.CategoryID;
import com.inetvod.common.dbdata.CategoryIDList;
import com.inetvod.common.dbdata.RatingID;
import com.inetvod.common.dbdata.Show;
import com.inetvod.common.dbdata.ShowID;

public class ShowDetail implements Readable
{
	/* Constants */
	public static Constructor<ShowDetail> CtorDataReader = DataReader.getCtor(ShowDetail.class);

	/* Fields */
	protected ShowID fShowID;
	protected String fName;
	protected String fEpisodeName;
	protected String fEpisodeNumber;
	protected Date fReleasedOn;
	protected Short fReleasedYear;
	protected String fDescription;
	protected Short fRunningMins;
	protected String fPictureURL;

	protected CategoryIDList fCategoryIDList;
	protected RatingID fRatingID;
	protected LanguageID fLanguageID;
	protected Boolean fIsAdult;

	protected ShowRentalList fShowRentalList;

	/* Constuction */
	public ShowDetail(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowID = reader.readDataID("ShowID", ShowID.MaxLength, ShowID.CtorString);
		fName = reader.readString("Name", Show.NameMaxLength);
		fEpisodeName = reader.readString("EpisodeName", Show.EpisodeNameMaxLength);
		fEpisodeNumber = reader.readString("EpisodeNumber", Show.EpisodeNumberMaxLength);

		fReleasedOn = reader.readDate("ReleasedOn");
		fReleasedYear = reader.readShort("ReleasedYear");
		fDescription = reader.readString("Description", Show.DescriptionMaxLength);

		fRunningMins = reader.readShort("RunningMins");
		fPictureURL = reader.readString("PictureURL", Show.PictureURLMaxLength);

		fCategoryIDList = reader.readStringList("CategoryID", CategoryID.MaxLength, CategoryIDList.Ctor, CategoryID.CtorString);
		fRatingID = reader.readDataID("RatingID", RatingID.MaxLength, RatingID.CtorString);
		fLanguageID = reader.readDataID("LanguageID", LanguageID.MaxLength, LanguageID.CtorString);
		fIsAdult = reader.readBoolean("IsAdult");

		fShowRentalList = reader.readList("ShowRental", ShowRentalList.Ctor, ShowRental.CtorDataReader);
	}
}
