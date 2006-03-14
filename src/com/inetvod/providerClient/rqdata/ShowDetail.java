/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.rqdata;

import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.LanguageID;
import com.inetvod.common.core.Readable;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.RatingID;
import com.inetvod.common.data.ShowRental;
import com.inetvod.common.data.ShowRentalList;
import com.inetvod.common.dbdata.Show;

public class ShowDetail implements Readable
{
	/* Constants */
	public static Constructor<ShowDetail> CtorDataReader = DataReader.getCtor(ShowDetail.class);

	/* Fields */
	private ProviderShowID fProviderShowID;
	private String fName;
	private String fEpisodeName;
	private String fEpisodeNumber;
	private Date fReleasedOn;
	private Short fReleasedYear;
	private String fDescription;
	private Short fRunningMins;
	private String fPictureURL;

	private CategoryIDList fCategoryIDList;
	private RatingID fRatingID;
	private LanguageID fLanguageID;
	private Boolean fIsAdult;

	private ShowRentalList fShowRentalList;

	/* Getters and Setters */
	public ProviderShowID getProviderShowID() { return fProviderShowID; }
	public String getName() { return fName; }
	public String getEpisodeName() { return fEpisodeName; }
	public String getEpisodeNumber() { return fEpisodeNumber; }
	public Date getReleasedOn() { return fReleasedOn; }

	public Short getReleasedYear()
	{
		if(fReleasedYear != null)
			return fReleasedYear;
		if(fReleasedOn != null)
		{
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));	// use GMT so time component is not considered.
			cal.setTime(fReleasedOn);
			return (short)cal.get(Calendar.YEAR);
		}

		return null;
	}

	public String getDescription() { return fDescription; }
	public Short getRunningMins() { return fRunningMins; }
	public String getPictureURL() { return fPictureURL; }

	public CategoryIDList getCategoryIDList() { return fCategoryIDList; }
	public RatingID getRatingID() { return fRatingID; }
	public LanguageID getLanguageID() { return fLanguageID; }
	public Boolean getIsAdult() { return fIsAdult; }

	public ShowRentalList getShowRentalList() { return fShowRentalList; }

	/* Constuction */
	public ShowDetail(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fProviderShowID = reader.readDataID("ShowID", ProviderShowID.MaxLength, ProviderShowID.CtorString);
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
