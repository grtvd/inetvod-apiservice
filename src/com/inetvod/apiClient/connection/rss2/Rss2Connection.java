/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2;

import com.inetvod.apiClient.ShowData;
import com.inetvod.apiClient.ShowDataList;
import com.inetvod.apiClient.connection.BaseConnection;
import com.inetvod.apiClient.connection.rss2.data.Channel;
import com.inetvod.apiClient.connection.rss2.data.Enclosure;
import com.inetvod.apiClient.connection.rss2.data.Item;
import com.inetvod.apiClient.connection.rss2.data.Rss20;
import com.inetvod.common.core.Logger;
import com.inetvod.common.data.MediaEncoding;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.ShowCost;
import com.inetvod.common.data.ShowCostType;
import com.inetvod.common.data.ShowFormat;
import com.inetvod.common.data.ShowRental;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;

public class Rss2Connection extends BaseConnection
{
	/* Constants */
	private static final String ProviderNameField = "provider.name";
	private static final String ChannelTitleField = "channel.title";
	private static final String ChannelDescriptionField = "channel.description";
	private static final String ItemTitleField = "item.title";

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
			Enclosure enclosure;
			ShowRental showRental;
			ShowFormat showFormat;

			ShowCost showCost = new ShowCost();
			showCost.setShowCostType(ShowCostType.Free);
			showCost.setCostDisplay("Free");

			//TODO: check channel.getLastBuildDate() to see if new content, if not new, can skip update

			for(Item item : channel.getItemList())
			{
				enclosure = item.getEnclosure();
				if(enclosure == null)
					continue;
				//TODO: support more video formats
				if(!"video/x-ms-wmv".equals(enclosure.getType()))
				{
					Logger.logInfo(this, "process", String.format("Skipping enclosure type(%s)", enclosure.getType()));
					continue;
				}

				showData = new ShowData();
				showData.setProviderShowID(new ProviderShowID(item.getGuid()));
				showData.setName(getShowName(channel, item));
				showData.setEpisodeName(getEpisodeName(channel, item));
				showData.setReleasedOn(item.getPubDate());
				showData.setDescription(item.getDescription().getText());

				showData.getCategoryIDList().copy(item.getCategoryIDList());

				showData.setShowURL(enclosure.getURL());

				showFormat = new ShowFormat();
				//TODO: confirm WMV9, set other fields
				showFormat.setMediaEncoding(MediaEncoding.WMV9);

				showRental = new ShowRental();
				showRental.getShowFormatList().add(showFormat);
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

	private String getShowName(Channel channel, Item item)
	{
		String field = fProviderConnection.getUseFieldForName();
		if(field == null)
			return channel.getTitle();
		return getFieldValue(field, channel, item);
	}

	private String getEpisodeName(Channel channel, Item item)
	{
		String field = fProviderConnection.getUseFieldForEpisodeName();
		if(field == null)
			return item.getTitle();
		return getFieldValue(field, channel, item);
	}

	private String getFieldValue(String field, Channel channel, Item item)
	{
		if(ProviderNameField.equals(field))
			return fProvider.getName();
		if(ChannelTitleField.equals(field))
			return channel.getTitle();
		if(ChannelDescriptionField.equals(field))
			return channel.getDescription();
		if(ItemTitleField.equals(field))
			return item.getTitle();

		throw new IllegalArgumentException(String.format("Unknown field(%s)", field));
	}
}
