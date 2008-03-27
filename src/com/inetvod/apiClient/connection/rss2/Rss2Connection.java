/**
 * Copyright © 2006-2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2;

import com.inetvod.apiClient.CategoryMapper;
import com.inetvod.apiClient.ShowData;
import com.inetvod.apiClient.ShowDataList;
import com.inetvod.apiClient.ShowFormatExt;
import com.inetvod.apiClient.connection.BaseConnection;
import com.inetvod.apiClient.connection.rss2.data.Channel;
import com.inetvod.apiClient.connection.rss2.data.Enclosure;
import com.inetvod.apiClient.connection.rss2.data.ITunesCategory;
import com.inetvod.apiClient.connection.rss2.data.ITunesCategoryList;
import com.inetvod.apiClient.connection.rss2.data.ITunesExplicit;
import com.inetvod.apiClient.connection.rss2.data.Item;
import com.inetvod.apiClient.connection.rss2.data.MediaContent;
import com.inetvod.apiClient.connection.rss2.data.MediaGroup;
import com.inetvod.apiClient.connection.rss2.data.Rss20;
import com.inetvod.common.core.Logger;
import com.inetvod.common.core.StrUtil;
import com.inetvod.common.core.StringList;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.RatingID;
import com.inetvod.common.data.ShowCost;
import com.inetvod.common.data.ShowCostType;
import com.inetvod.common.data.ShowFormat;
import com.inetvod.common.data.ShowFormatList;
import com.inetvod.common.data.ShowRental;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import com.inetvod.common.dbdata.Show;

@SuppressWarnings({"OverlyComplexClass"})
public class Rss2Connection extends BaseConnection
{
	/* Constants */
	private static final String ProviderNameField = "provider.name";
	private static final String ChannelTitleField = "channel.title";
	private static final String ChannelDescriptionField = "channel.description";
	private static final String ItemTitleField = "item.title";
	private static final String ItemMediaTitleField = "item.media:title";
	private static final String ItemMediaDescriptionField = "item.media:description";
	private static final String MediaGroupMediaTitleField = "mediagroup.media:title";
	private static final String MediaGroupMediaDescriptionField = "mediagroup.media:description";
	private static final String MediaContentMediaTitleField = "mediacontent.media:title";
	private static final String MediaContentMediaDescriptionField = "mediacontent.media:description";
	private static final String[] EpisodeNameSeparatorList = { ": ", " - " };

	/* Fields */

	/* Getters and Setters */

	/* Construction */
	private Rss2Connection(Provider provider, ProviderConnection providerConnection)
	{
		super(provider, providerConnection);
	}

	public static Rss2Connection newInstance(Provider provider, ProviderConnection providerConnection)
	{
		return new Rss2Connection(provider, providerConnection);
	}

	/* Implementation */
	public ShowDataList process()
	{
		try
		{
			ShowDataList showDataList = new ShowDataList();
			ShowData showData;

			Rss20 rss20 = this.sendRequest("rss", Rss20.CtorDataReader);
			Channel channel = rss20.getChannel();
			MediaGroup mediaGroup;
			MediaContent mediaContent;
			ShowRental showRental;
			ShowFormatList showFormatList;

			ShowCost showCost = new ShowCost();
			showCost.setShowCostType(ShowCostType.Free);
			showCost.setCostDisplay("Free");

			//TODO: check channel.getLastBuildDate() to see if new content, if not new, can skip update

			for(Item item : channel.getItemList())
			{
				mediaGroup = item.getMediaGroup();
				mediaContent = null;
				if(mediaGroup == null)
				{
					if(item.getMediaContentList().size() > 1)
						throw new Exception(String.format("Can't handle MediaContent sequence for ProviderConnectionID(%s)",
							fProviderConnection.getProviderConnectionID()));

					if(item.getMediaContentList().size() == 1)
						mediaContent = item.getMediaContentList().get(0);
				}

				showFormatList = getShowFormatList(item, mediaGroup, mediaContent);
				if(showFormatList.size() == 0)
					continue;

				showData = new ShowData();
				showData.setProviderShowID(getProviderShowID(item));
				showData.setName(confirmMaxLength(getShowName(channel, item, mediaGroup, mediaContent),
					Show.NameMaxLength));
				showData.setEpisodeName(confirmMaxLength(getEpisodeName(showData.getName(), channel, item, mediaGroup,
					mediaContent), Show.EpisodeNameMaxLength));
				showData.setReleasedOn(item.getPubDate());
				showData.setDescription(confirmMaxLength(getDescription(channel, item, mediaGroup, mediaContent),
					Show.DescriptionMaxLength));
				showData.setRunningMins(getRunningMins(item, mediaGroup, mediaContent));

				showData.getCategoryIDList().copy(getCategories(channel, item));
				showData.setRatingID(mapFromITunesExplicit(channel, item));
				showData.setPictureURL(getImage(channel, item));

				showRental = new ShowRental();
				showRental.getShowFormatList().copy(showFormatList);
				showRental.getShowCostList().add(showCost);

				showData.getShowRentalList().add(showRental);

				showDataList.add(showData);
			}

			return showDataList;
		}
		catch(Exception e)
		{
			Logger.logErr(this, "process", String.format("Failed processing Provider(%s) on ProviderConnection(%s)",
				fProvider.getProviderID(), fProviderConnection.getProviderConnectionID()), e);
		}

		return null;
	}

	private static ProviderShowID getProviderShowID(Item item) throws Exception
	{
		String guid = item.getGuid();

		if(!StrUtil.hasLen(guid))
		{
			Enclosure enclosure = item.getEnclosure();
			if(enclosure != null)
				guid = enclosure.getURL();
		}

		if(StrUtil.hasLen(guid))
			return new ProviderShowID(guid);

		throw new Exception("No GUID found for Item");
	}

	private String getShowName(Channel channel, Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		String field = fProviderConnection.getUseFieldForName();
		if(field != null)
			return getFieldValue(field, channel, item, mediaGroup, mediaContent);

		return channel.getTitle();
	}

	private String getEpisodeName(String showName, Channel channel, Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		String episodeName = getEpisodeNameValue(channel, item, mediaGroup, mediaContent);

		if(!StrUtil.hasLen(showName) || !StrUtil.hasLen(episodeName)
				|| !episodeName.toLowerCase().startsWith(showName.toLowerCase()))
			return episodeName;

		episodeName = episodeName.substring(showName.length());

		for(String prefix : EpisodeNameSeparatorList)
		{
			if(episodeName.startsWith(prefix))
				episodeName = episodeName.substring(prefix.length());
		}

		return episodeName;
	}

	private String getEpisodeNameValue(Channel channel, Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		String field = fProviderConnection.getUseFieldForEpisodeName();
		if(field != null)
			return getFieldValue(field, channel, item, mediaGroup, mediaContent);

		if((mediaContent != null) && (mediaContent.getMediaTitle() != null))
			return mediaContent.getMediaTitle().toString();
		if((mediaGroup != null) && (mediaGroup.getMediaTitle() != null))
			return mediaGroup.getMediaTitle().toString();

		return item.getTitle();
	}

	private static String getDescription(Channel channel, Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		if((mediaContent != null) && (mediaContent.getMediaDescription() != null))
			return mediaContent.getMediaDescription().toString();
		if((mediaGroup != null) && (mediaGroup.getMediaDescription() != null))
			return mediaGroup.getMediaDescription().toString();

		if(item.getMediaDescription() != null)
			return item.getMediaDescription().toString();
		if(channel.getMediaDescription() != null)
			return channel.getMediaDescription().toString();

		if(item.getITunesSummary() != null)
			return item.getITunesSummary();

		if(item.getDescription() != null)
			return item.getDescription().getText();

		return null;
	}

	private Short getRunningMins(Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		Integer durationSecs = null;

		if((mediaContent != null) && (mediaContent.getDurationSecs() != null))
			durationSecs = mediaContent.getDurationSecs();
		if((mediaGroup != null) && (mediaGroup.getMediaContentList().size() > 0))
			durationSecs = mediaGroup.getMediaContentList().get(0).getDurationSecs();

		if(item.getITunesDuration() != null)
			durationSecs = parseITunesDuration(item.getITunesDuration());

		if(durationSecs == null)
			return null;

		//noinspection MagicNumber
		return (short)((durationSecs.doubleValue() / 60.0) + 0.5);
	}

	private Integer parseITunesDuration(String iTunesDuration)
	{
		try
		{
			if(!StrUtil.hasLen(iTunesDuration))
				return null;

			int hours = 0;
			int minutes = 0;
			int seconds = 0;

			String[] parts = iTunesDuration.split(":");

			if(parts.length >= 3)
			{
				hours = Integer.parseInt(parts[0]);
				minutes = Integer.parseInt(parts[1]);
				seconds = Integer.parseInt(parts[2]);
			}
			else if(parts.length == 2)
			{
				minutes = Integer.parseInt(parts[0]);
				seconds = Integer.parseInt(parts[1]);
			}
			else if(parts.length == 1)
				seconds = Integer.parseInt(parts[0]);

			//noinspection MagicNumber
			return (hours * 3600) + (minutes * 60) + seconds;
		}
		catch(Exception e)
		{
			Logger.logErr(this, "parseITunesDuration", String.format("Can't parse itunes:duration(%s)", iTunesDuration), e);
			return null;
		}

	}

	private static CategoryIDList getCategories(Channel channel, Item item)
	{
		if(item.getITunesCategoryList().size() != 0)
			return mapFromITunesCategory(item.getITunesCategoryList());
		if(item.getCategoryList().size() != 0)
			return mapFromMiscCategory(item.getCategoryList());

		if(channel.getITunesCategoryList().size() != 0)
			return mapFromITunesCategory(channel.getITunesCategoryList());
		if(channel.getCategoryList().size() != 0)
			return mapFromMiscCategory(channel.getCategoryList());

		return null;
	}

	private static CategoryIDList mapFromITunesCategory(ITunesCategoryList iTunesCategoryList)
	{
		CategoryMapper categoryMapper = CategoryMapper.getThe();
		CategoryIDList categoryIDList = new CategoryIDList();
		CategoryID categoryID;

		for(ITunesCategory iTunesCategory : iTunesCategoryList)
		{
			categoryID = categoryMapper.mapCategory(iTunesCategory.getText());
			if(categoryID != null)
				categoryIDList.add(categoryID);
			//TODO else
			//TODO	Logger.logWarn(this, "mapFromITunesCategory", String.format("Skipping category(%s)", iTunesCategory.getText()));
		}

		return categoryIDList;
	}

	@SuppressWarnings({ "UnusedDeclaration" })
	private static CategoryIDList mapFromMediaCategory()
	{
		//TODO:
		return null;
	}

	private static CategoryIDList mapFromMiscCategory(StringList categoryList)
	{
		CategoryMapper categoryMapper = CategoryMapper.getThe();
		CategoryIDList categoryIDList = new CategoryIDList();
		CategoryID categoryID;

		for(String category : categoryList)
		{
			categoryID = categoryMapper.mapCategory(category);
			if(categoryID != null)
				categoryIDList.add(categoryID);
			else
				Logger.logErr(Rss2Connection.class, "mapFromMiscCategory", String.format("Skipping category(%s)", category));
		}

		return categoryIDList;
	}

	private static RatingID mapFromITunesExplicit(Channel channel, Item item)
	{
		ITunesExplicit iTunesExplicit;

		iTunesExplicit = item.getITunesExplicit();
		if(iTunesExplicit == null)
			iTunesExplicit = channel.getITunesExplicit();

		if(iTunesExplicit == null)
			return null;

		if(ITunesExplicit.Yes.equals(iTunesExplicit))
			return RatingID.ITunesExplicit;
		if(ITunesExplicit.Clean.equals(iTunesExplicit))
			return RatingID.ITunesClean;
		//ITunesExplicit.No
		return null;
	}

	private static String getImage(Channel channel, Item item)
	{
		if((item.getMediaThumbnail() != null) && (item.getMediaThumbnail().getURL() != null))
			return item.getMediaThumbnail().getURL();

		if((channel.getITunesImage() != null) && (channel.getITunesImage().getHREF() != null))
			return channel.getITunesImage().getHREF();

		if((channel.getMediaThumbnail() != null) && (channel.getMediaThumbnail().getURL() != null))
			return channel.getMediaThumbnail().getURL();

		if((channel.getImage() != null) && (channel.getImage().getURL() != null))
			return channel.getImage().getURL();

		return null;
	}

	private String getFieldValue(String field, Channel channel, Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		if(ProviderNameField.equals(field))
			return fProvider.getName();
		if(ChannelTitleField.equals(field))
			return channel.getTitle();
		if(ChannelDescriptionField.equals(field))
			return channel.getDescription();
		if(ItemTitleField.equals(field))
			return item.getTitle();
		if(ItemMediaTitleField.equals(field))
		{
			if(item.getMediaTitle() != null)
				return item.getMediaTitle().toString();
			return null;
		}
		if(ItemMediaDescriptionField.equals(field))
		{
			if(item.getMediaDescription() != null)
				return item.getMediaDescription().toString();
			return null;
		}
		if(MediaGroupMediaTitleField.equals(field))
		{
			if((mediaGroup != null) && (mediaGroup.getMediaTitle() != null))
				return mediaGroup.getMediaTitle().toString();
			return null;
		}
		if(MediaGroupMediaDescriptionField.equals(field))
		{
			if((mediaGroup != null) && (mediaGroup.getMediaDescription() != null))
				return mediaGroup.getMediaDescription().toString();
			return null;
		}
		if(MediaContentMediaTitleField.equals(field))
		{
			if((mediaContent != null) && (mediaContent.getMediaTitle() != null))
				return mediaContent.getMediaTitle().toString();
			return null;
		}
		if(MediaContentMediaDescriptionField.equals(field))
		{
			if((mediaContent != null) && (mediaContent.getMediaDescription() != null))
				return mediaContent.getMediaDescription().toString();
			return null;
		}

		throw new IllegalArgumentException(String.format("Unknown field(%s)", field));
	}

	private String confirmMaxLength(String str, int maxLen)
	{
		if(!StrUtil.hasLen(str))
			return str;
		int len = str.length();
		if(len <= maxLen)
			return str;

		Logger.logInfo(this, "confirmMaxLength", String.format("Trunking len(%d) to maxLen(%d) for (%s)", len, maxLen, str));
		return str.substring(0, maxLen);
	}

	private ShowFormatList getShowFormatList(Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		ShowFormatList showFormatList = new ShowFormatList();
		ShowFormat showFormat;

		if(mediaGroup != null)
		{
			for(MediaContent content : mediaGroup.getMediaContentList())
			{
				showFormat = getShowFormatFromMediaContent(content);
				if(showFormat != null)
					showFormatList.add(showFormat);
			}
		}
		else if(mediaContent != null)
		{
			showFormat = getShowFormatFromMediaContent(mediaContent);
			if(showFormat != null)
				showFormatList.add(showFormat);
		}
		else
		{
			showFormat = getShowFormatFromItem(item);
			if(showFormat != null)
				showFormatList.add(showFormat);
		}

		if(showFormatList.size() == 0)
			Logger.logInfo(this, "getShowFormatList", String.format("No ShowFormats found for item(%s)", item.getGuid()));
		return showFormatList;
	}

	private static ShowFormat getShowFormatFromItem(Item item)
	{
		Enclosure enclosure = item.getEnclosure();
		if(enclosure == null)
			return null;

		if(!StrUtil.hasLen(enclosure.getURL()))
			return null;

		//TODO: confirm MediaEncoding, set other fields

		return new ShowFormatExt(enclosure.getURL(), enclosure.getType());
	}

	private static ShowFormat getShowFormatFromMediaContent(MediaContent mediaContent)
	{
		if(!StrUtil.hasLen(mediaContent.getURL()))
			return null;

		//TODO: confirm MediaEncoding, set other fields

		return new ShowFormatExt(mediaContent.getURL(), mediaContent.getType());
	}
}
