/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2;

import com.inetvod.apiClient.ShowData;
import com.inetvod.apiClient.ShowDataList;
import com.inetvod.apiClient.ShowFormatExt;
import com.inetvod.apiClient.connection.BaseConnection;
import com.inetvod.apiClient.connection.rss2.data.Channel;
import com.inetvod.apiClient.connection.rss2.data.Enclosure;
import com.inetvod.apiClient.connection.rss2.data.Item;
import com.inetvod.apiClient.connection.rss2.data.MediaContent;
import com.inetvod.apiClient.connection.rss2.data.MediaGroup;
import com.inetvod.apiClient.connection.rss2.data.Rss20;
import com.inetvod.common.core.Logger;
import com.inetvod.common.core.StrUtil;
import com.inetvod.common.data.MediaEncoding;
import com.inetvod.common.data.MediaMIME;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.ShowCost;
import com.inetvod.common.data.ShowCostType;
import com.inetvod.common.data.ShowFormat;
import com.inetvod.common.data.ShowFormatList;
import com.inetvod.common.data.ShowRental;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import com.inetvod.common.dbdata.Show;

public class Rss2Connection extends BaseConnection
{
	/* Constants */
	private static final String ProviderNameField = "provider.name";
	private static final String ChannelTitleField = "channel.title";
	private static final String ChannelDescriptionField = "channel.description";
	private static final String ItemTitleField = "item.title";
	private static final String ItemMediaTitleField = "item.media:title";
	private static final String ItemMediaDescriptionField = "item.media:decription";
	private static final String MediaGroupMediaTitleField = "mediagroup.media:title";
	private static final String MediaGroupMediaDescriptionField = "mediagroup.media:decription";
	private static final String MediaContentMediaTitleField = "mediacontent.media:title";
	private static final String MediaContentMediaDescriptionField = "mediacontent.media:decription";

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
				showData.setProviderShowID(new ProviderShowID(item.getGuid()));
				showData.setName(confirmMaxLength(getShowName(channel, item, mediaGroup, mediaContent),
					Show.NameMaxLength));
				showData.setEpisodeName(confirmMaxLength(getEpisodeName(channel, item, mediaGroup, mediaContent),
					Show.EpisodeNameMaxLength));
				showData.setReleasedOn(item.getPubDate());
				showData.setDescription(confirmMaxLength(getDescription(channel, item, mediaGroup, mediaContent),
					Show.DescriptionMaxLength));
				showData.setRunningMins(getRunningMins(mediaGroup, mediaContent));

				showData.getCategoryIDList().copy(item.getCategoryIDList());

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
			Logger.logErr(this, "process", e);
		}

		return null;
	}

	private String getShowName(Channel channel, Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		String field = fProviderConnection.getUseFieldForName();
		if(field != null)
			return getFieldValue(field, channel, item, mediaGroup, mediaContent);

		return channel.getTitle();
	}

	private String getEpisodeName(Channel channel, Item item, MediaGroup mediaGroup, MediaContent mediaContent)
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

	private String getDescription(Channel channel, Item item, MediaGroup mediaGroup, MediaContent mediaContent)
	{
		if((mediaContent != null) && (mediaContent.getMediaDescription() != null))
			return mediaContent.getMediaDescription().toString();
		if((mediaGroup != null) && (mediaGroup.getMediaDescription() != null))
			return mediaGroup.getMediaDescription().toString();

		if(item.getMediaDescription() != null)
			return item.getMediaDescription().toString();
		if(channel.getMediaDescription() != null)
			return channel.getMediaDescription().toString();

		if(item.getDescription() != null)
			return item.getDescription().getText();

		return null;
	}

	private Short getRunningMins(MediaGroup mediaGroup, MediaContent mediaContent)
	{
		Integer durationSecs = null;

		if((mediaContent != null) && (mediaContent.getDurationSecs() != null))
			durationSecs = mediaContent.getDurationSecs();
		if((mediaGroup != null) && (mediaGroup.getMediaContentList().size() > 0))
			durationSecs = mediaGroup.getMediaContentList().get(0).getDurationSecs();

		if(durationSecs == null)
			return null;

		//noinspection MagicNumber
		return (short)((durationSecs.doubleValue() / 60.0) + 0.5);
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

		Logger.logInfo(this, "confirmMaxLength", String.format("Trunking (%s) to maxLen(%d)", str, maxLen));
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

	private ShowFormat getShowFormatFromItem(Item item)
	{
		Enclosure enclosure = item.getEnclosure();
		if(enclosure == null)
			return null;

		if(!StrUtil.hasLen(enclosure.getURL()))
			return null;

		MediaEncoding mediaEncoding = determineMediaEncodingFromMIME(enclosure.getType());
		if(mediaEncoding == null)
			return null;

		//TODO: confirm MediaEncoding, set other fields

		ShowFormatExt showFormatExt = new ShowFormatExt();
		showFormatExt.setShowURL(enclosure.getURL());
		showFormatExt.setMediaEncoding(mediaEncoding);
		return showFormatExt;
	}

	private ShowFormat getShowFormatFromMediaContent(MediaContent mediaContent)
	{
		if(!StrUtil.hasLen(mediaContent.getURL()))
			return null;

		MediaEncoding mediaEncoding = determineMediaEncodingFromMIME(mediaContent.getType());
		if(mediaEncoding == null)
			return null;

		//TODO: confirm MediaEncoding, set other fields

		ShowFormatExt showFormatExt = new ShowFormatExt();
		showFormatExt.setShowURL(mediaContent.getURL());
		showFormatExt.setMediaEncoding(mediaEncoding);
		return showFormatExt;
	}

	private MediaEncoding determineMediaEncodingFromMIME(String type)
	{
		//TODO: support more video formats
		if(MediaMIME.video_x_ms_wmv.equals(type))
			return MediaEncoding.WMV9;
		if(MediaMIME.video_x_msvideo.equals(type))
			return MediaEncoding.DivX5;
		if(MediaMIME.video_mp4.equals(type))
			return MediaEncoding.SVQ3;

		Logger.logInfo(this, "determineMediaEncodingFromMIME", String.format("Skipping type(%s)", type));
		return null;
	}
}
