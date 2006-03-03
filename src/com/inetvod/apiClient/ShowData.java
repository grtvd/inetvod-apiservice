/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.apiClient;

import java.util.Date;

import com.inetvod.common.data.RatingID;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.ShowRentalList;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.core.LanguageID;

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

	private CategoryIDList fCategoryIDList = new CategoryIDList();
	private RatingID fRatingID;
	private LanguageID fLanguageID;
	private Boolean fIsAdult;

	private ShowRentalList fShowRentalList = new ShowRentalList();

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

	public CategoryIDList getCategoryIDList() { return fCategoryIDList; }

	public RatingID getRatingID() { return fRatingID; }
	public void setRatingID(RatingID ratingID) { fRatingID = ratingID; }

	public LanguageID getLanguageID() { return fLanguageID; }
	public void setLanguageID(LanguageID languageID) { fLanguageID = languageID; }

	public Boolean getIsAdult() { return fIsAdult; }
	public void setIsAdult(Boolean isAdult) { fIsAdult = isAdult; }

	public ShowRentalList getShowRentalList() { return fShowRentalList; }
}
