/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.util.Date;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.RatingID;
import com.inetvod.common.data.ShowID;

public class Show extends DatabaseObject
{
	/* Constants */
	public static final int NameMaxLength = 64;
	public static final int EpisodeNameMaxLength = 64;
	public static final int EpisodeNumberMaxLength = 32;
	public static final int DescriptionMaxLength = Short.MAX_VALUE;
	public static final int ShowURLMaxLength = 4096;
	public static final int PictureURLMaxLength = 4096;

	/* Fields */
	private ShowID fShowID;
	private String fName;
	private String fEpisodeName;
	private String fEpisodeNumber;
	private Date fReleasedOn;
	private Short fReleasedYear;
	private String fDescription;
	private Short fRunningMins;
	private String fPictureURL;
	private RatingID fRatingID;
	private Boolean fIsAdult;

	private static DatabaseAdaptor<Show, ShowList> fDatabaseAdaptor =
		new DatabaseAdaptor<Show, ShowList>(Show.class, ShowList.class);
	public static DatabaseAdaptor<Show, ShowList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ShowID getShowID() { return fShowID; }

	public String getName() { return fName; }
	public void setName(String name) { fName = name; }

	public String getEpisodeName() { return fEpisodeName; }
	public void setEpisodeName(String episodeName) { fEpisodeName = episodeName; }

	public String getEpisodeNumber() { return fEpisodeNumber; }
	public void setEpisodeNumber(String episodeNumber) { fEpisodeNumber = episodeNumber; }

	public Date getReleasedOn() { return fReleasedOn; }
	public void setReleasedOn(Date releasedOn) { fReleasedOn = releasedOn; }

	public Short getReleasedYear() { return fReleasedYear; }
	public void setReleasedYear(Short releasedYear) { fReleasedYear = releasedYear; }

	public String getDescription() { return fDescription; }
	public void setDescription(String description) { fDescription = description; }

	public Short getRunningMins() { return fRunningMins; }
	public void setRunningMins(Short runningMins) { fRunningMins = runningMins; }

	public String getPictureURL() { return fPictureURL; }
	public void setPictureURL(String pictureURL) { fPictureURL = pictureURL; }

	public RatingID getRatingID() { return fRatingID; }
	public void setRatingID(RatingID ratingID) { fRatingID = ratingID; }

	public Boolean getIsAdult() { return fIsAdult; }
	public void setIsAdult(Boolean isAdult) { fIsAdult = isAdult; }

	/* Construction */
	private Show(String name, boolean isAdult)
	{
		super(true);
		fShowID = ShowID.newInstance();
		fName = name;
		fIsAdult = isAdult;
	}

	public Show(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static Show newInstance(String name, boolean isAdult)
	{
		return new Show(name, isAdult);
	}

	private static Show load(ShowID showID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(showID, exists);
	}

	public static Show get(ShowID showID) throws Exception
	{
		return load(showID, DataExists.MustExist);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowID = reader.readDataID("ShowID", ShowID.MaxLength, ShowID.CtorString);
		fName = reader.readString("Name", NameMaxLength);
		fEpisodeName = reader.readString("EpisodeName", EpisodeNameMaxLength);
		fEpisodeNumber = reader.readString("EpisodeNumber", EpisodeNumberMaxLength);
		fReleasedOn = reader.readDate("ReleasedOn");
		fReleasedYear = reader.readShort("ReleasedYear");
		fDescription = reader.readString("Description", DescriptionMaxLength);
		fRunningMins = reader.readShort("RunningMins");
		fPictureURL = reader.readString("PictureURL", PictureURLMaxLength);
		fRatingID = reader.readDataID("RatingID", RatingID.MaxLength, RatingID.CtorString);
		fIsAdult = reader.readBoolean("IsAdult");
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ShowID", fShowID, ShowID.MaxLength);
		writer.writeString("Name", fName, NameMaxLength);
		writer.writeString("EpisodeName", fEpisodeName, EpisodeNameMaxLength);
		writer.writeString("EpisodeNumber", fEpisodeNumber, EpisodeNumberMaxLength);
		writer.writeDate("ReleasedOn", fReleasedOn);
		writer.writeShort("ReleasedYear", fReleasedYear);
		writer.writeString("Description", fDescription, DescriptionMaxLength);
		writer.writeShort("RunningMins", fRunningMins);
		writer.writeString("PictureURL", fPictureURL, PictureURLMaxLength);
		writer.writeDataID("RatingID", fRatingID, RatingID.MaxLength);
		writer.writeBoolean("IsAdult", fIsAdult);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fShowID);
	}
}
