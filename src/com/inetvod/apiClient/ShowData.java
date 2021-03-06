/**
 * Copyright � 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient;

import java.util.Date;

import com.inetvod.common.core.LanguageID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.RatingID;
import com.inetvod.common.data.ShowRentalList;

public class ShowData
{
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
	public void setProviderShowID(ProviderShowID providerShowID) { fProviderShowID = providerShowID; }

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

	public CategoryIDList getCategoryIDList()
	{
		if(fCategoryIDList == null)
			fCategoryIDList = new CategoryIDList();
		return fCategoryIDList;
	}

	public RatingID getRatingID() { return fRatingID; }
	public void setRatingID(RatingID ratingID) { fRatingID = ratingID; }

	public LanguageID getLanguageID() { return fLanguageID; }
	public void setLanguageID(LanguageID languageID) { fLanguageID = languageID; }

	public Boolean getIsAdult() { return fIsAdult; }
	public void setIsAdult(Boolean isAdult) { fIsAdult = isAdult; }

	public ShowRentalList getShowRentalList()
	{
		if(fShowRentalList == null)
			fShowRentalList = new ShowRentalList();
		return fShowRentalList;
	}
}
