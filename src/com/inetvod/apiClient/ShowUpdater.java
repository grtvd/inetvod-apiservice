/**
 * Copyright © 2006-2007 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient;

import java.util.HashSet;

import com.inetvod.common.core.Logger;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.MediaEncoding;
import com.inetvod.common.data.MediaMIME;
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.data.ShowAvail;
import com.inetvod.common.data.ShowFormat;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ShowRental;
import com.inetvod.common.data.ShowRentalList;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import com.inetvod.common.dbdata.Show;
import com.inetvod.common.dbdata.ShowCategory;
import com.inetvod.common.dbdata.ShowCategoryList;
import com.inetvod.common.dbdata.ShowList;
import com.inetvod.common.dbdata.ShowProvider;
import com.inetvod.common.dbdata.ShowProviderList;

public abstract class ShowUpdater
{
	//TODO: see what methods can be combined from ProviderShowUpdater and ConnectionShowUpdater and added to this class

	/* Fields */
	protected Provider fProvider;
	protected ProviderConnection fProviderConnection;

	/* Construction */
	protected ShowUpdater(Provider provider, ProviderConnection providerConnection)
	{
		fProvider = provider;
		fProviderConnection = providerConnection;
	}

	/* Implementation */
	public abstract void doUpdate() throws Exception;

	protected Show locateExistingShow(ShowData showData) throws Exception
	{
		if((showData.getReleasedYear() == null) && (showData.getReleasedOn() == null))
			return null;

		ShowList showList;

		if(showData.getReleasedOn() != null)
			showList = ShowList.findByNameReleasedOn(showData.getName(), showData.getEpisodeName(),
				showData.getReleasedOn());
		else
			showList = ShowList.findByNameReleasedYear(showData.getName(), showData.getEpisodeName(),
				showData.getReleasedYear());

		//TODO: how do we handle more than one result?
		if(showList.size() >= 1)
		{
			if(showList.size() > 1)
				Logger.logErr(this, "locateExistingShow", String.format("Found (%d) shows, for name(%s), episode(%s)",
					showList.size(), showData.getName(), showData.getEpisodeName()));
			return showList.get(0);
		}

		return null;
	}

	protected boolean confirmShowRentalList(ShowRentalList showRentalList)
	{
		HashSet<ShowFormat> showFormatSet = new HashSet<ShowFormat>();

		for(ShowRental showRental : showRentalList)
			for(ShowFormat showFormat : showRental.getShowFormatList())
			{
				// showFormatSet.contains is not working as expected, not calling ShowFormat.equals
//				for(ShowFormat showFormatCheck : showFormatSet)
//					if(showFormatCheck.equals(showFormat))
//						return false;
				if(showFormatSet.contains(showFormat))
					return false;
				showFormatSet.add(showFormat);
			}

		return true;
	}

	protected void updateShow(ShowData showData) throws Exception
	{
		final String METHOD_NAME = "updateShow";
		ProviderID providerID = fProviderConnection.getProviderID();
		Show show;
		ShowProviderList showProviderList;

		showProviderList = ShowProviderList.findByProviderConnectionIDProviderShowID(
			fProviderConnection.getProviderConnectionID(), showData.getProviderShowID());
		if(showProviderList.size() > 0)
		{
			if(!showProviderList.isShowIDSame())
			{
				Logger.logErr(this, METHOD_NAME, String.format("Found multiple ShowIDs for ProviderConnectionID(%s), ProviderShowID(%s)",
					fProviderConnection.getProviderConnectionID().toString(), showData.getProviderShowID().toString()));
				return;
			}

			show = Show.get(showProviderList.get(0).getShowID());
		}
		else
		{
			show = locateExistingShow(showData);
			if(show != null)
			{
				if(ShowProviderList.findByShowIDProviderID(show.getShowID(), providerID).size() > 0)
				{
					Logger.logErr(this, METHOD_NAME, String.format("Provider already set for Show, possible bad ProviderShowID(%s) for ShowID(%s)",
						showData.getProviderShowID(), show.getShowID()));
					return;
				}
			}
			else
				show = Show.newInstance(showData.getName(), showData.getIsAdult());
		}

		saveShowData(showData, show, showProviderList);
	}

	private void saveShowData(ShowData showData, Show show, ShowProviderList showProviderList) throws Exception
	{
		//TODO: need to have logic for updating same Show from two Providers

		// update Show values
		show.setName(showData.getName());
		show.setEpisodeName(showData.getEpisodeName());
		show.setEpisodeNumber(showData.getEpisodeNumber());
		show.setReleasedOn(showData.getReleasedOn());
		show.setReleasedYear(showData.getReleasedYear());
		show.setDescription(showData.getDescription());
		show.setRunningMins(showData.getRunningMins());
		show.setPictureURL(showData.getPictureURL());
		show.setRatingID(showData.getRatingID());
		//TODO: showData.getLanguageID();
		show.setIsAdult(showData.getIsAdult());
		show.update();

		// update ShowProvider values
		reconcileShowProvider(showData, show.getShowID(), showProviderList);

		// update ShowCategory entries
		reconcileShowCategory(showData, show.getShowID());
	}

	private void reconcileShowProvider(ShowData showData, ShowID showID, ShowProviderList showProviderList)
		throws Exception
	{
		// Note: the same ShowFormat should not be duplicated across ShowRentalList. Checks done in confirmShowRentalList().

		for(ShowRental showRental : showData.getShowRentalList())
		{
			for(ShowFormat showFormat : showRental.getShowFormatList())
			{
//				String showFormatMime;
//				if(showFormat instanceof ShowFormatExt)
//					showFormatMime = ((ShowFormatExt)showFormat).getShowFormatMime();
//				else
//					showFormatMime = mapShowFormatMimeFromMediaEncoding(showFormat.getMediaEncoding());
//
//				ShowProvider showProvider = showProviderList.findByShowFormatMime(showFormatMime);
				String showFormatMime;
				ShowProvider showProvider;

				if(showFormat instanceof ShowFormatExt)
				{
					showProvider = showProviderList.findByShowURL(((ShowFormatExt)showFormat).getShowURL());
					showFormatMime = ((ShowFormatExt)showFormat).getShowFormatMime();
				}
				else
				{
					showFormatMime = mapShowFormatMimeFromMediaEncoding(showFormat.getMediaEncoding());
					//TODO Later this should map the showFormat into a valid MediaFormatID, then MediaFormatID would be used
					showProvider = showProviderList.findByShowFormatMime(showFormatMime);
				}

				if(showProvider == null)
				{
					showProvider = ShowProvider.newInstance(showID, fProviderConnection.getProviderID(),
						fProviderConnection.getProviderConnectionID(), showData.getProviderShowID(), showFormatMime);
				}
				else
					showProviderList.remove(showProvider);

				if(showFormat instanceof ShowFormatExt)
					showProvider.setShowURL(((ShowFormatExt)showFormat).getShowURL());

				showProvider.getShowCostList().copy(showRental.getShowCostList());
				showProvider.setShowAvail(ShowAvail.Available);
				showProvider.update();
			}
		}

		// delete old ShowProviders
		for(ShowProvider showProvider : showProviderList)
			showProvider.delete();
	}

	//TODO temporary method until MediaFormatID is support
	private static String mapShowFormatMimeFromMediaEncoding(MediaEncoding mediaEncoding)
	{
		if(MediaEncoding.WMV2.equals(mediaEncoding))
			return MediaMIME.video_x_ms_wmv.toString();
		if(MediaEncoding.DIVX.equals(mediaEncoding))
			return MediaMIME.video_x_msvideo.toString();
		if(MediaEncoding.SVQ3.equals(mediaEncoding))
			return MediaMIME.video_mov.toString();
		if(MediaEncoding.MPGA.equals(mediaEncoding))
			return MediaMIME.audio_mpeg.toString();

		throw new IllegalArgumentException(mediaEncoding.toString());
	}

	private static void reconcileShowCategory(ShowData showData, ShowID showID) throws Exception
	{
		ShowCategoryList showCategoryList = ShowCategoryList.findByShowID(showID);

		CategoryIDList providerCategoryIDList = showData.getCategoryIDList();

		// remove old categories
		for(ShowCategory showCategory : showCategoryList)
		{
			if(CategoryID.Featured.equals(showCategory.getCategoryID()))	// skip Featured
				continue;

			if(!providerCategoryIDList.contains(showCategory.getCategoryID()))
				showCategory.delete();
		}

		// add new categories
		CategoryIDList categoryIDList = showCategoryList.getCategoryIDList();
		for(CategoryID categoryID : providerCategoryIDList)
		{
			if(CategoryID.Featured.equals(categoryID))	// skip Featured
				continue;

			if(!categoryIDList.contains(categoryID))
				ShowCategory.newInstance(showID, categoryID).update();
		}
	}
}
