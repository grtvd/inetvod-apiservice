/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.apiservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import com.inetvod.common.core.CurrencyID;
import com.inetvod.common.core.DataID;
import com.inetvod.common.core.ISO8601DateFormat;
import com.inetvod.common.core.LanguageID;
import com.inetvod.common.core.Money;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.MediaContainer;
import com.inetvod.common.data.MediaEncoding;
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.data.ProviderIDList;
import com.inetvod.common.data.RatingID;
import com.inetvod.common.data.ShowCost;
import com.inetvod.common.data.ShowCostType;
import com.inetvod.common.data.ShowFormat;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.dbdata.Show;
import com.inetvod.common.dbdata.ShowCategory;
import com.inetvod.common.dbdata.ShowCategoryList;
import com.inetvod.common.dbdata.ShowProvider;
import com.inetvod.common.dbdata.ShowProviderList;

public class CreateDataXml
{
	private Writer fWriter;

	private CreateDataXml(Writer writer, String characterEncoding) throws Exception
	{
		fWriter = writer;
		writeStartDocument(characterEncoding);
	}

	public static void doIt(ProviderID providerID) throws Exception
	{
		File file = new File(String.format("c:\\temp\\inetvod\\data_%s.xml", providerID.toString()));
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter writer = null;
		try
		{
			writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
			CreateDataXml createDataXml = new CreateDataXml(writer, "UTF-8");

			createDataXml.writeShows(providerID);
		}
		finally
		{
			if(writer != null)
				writer.flush();
			fileOutputStream.flush();
			fileOutputStream.close();
		}
	}

//	public static void confirm(ProviderID providerID)
//	{
//		File file = new File(String.format("c:\\temp\\inetvod\\data_%s.xml", providerID.toString()));
//		try
//		{
//			DataManager.load(file);
//		}
//		catch(Exception e)
//		{
//			Logger.logErr(CreateDataXml.class, "confirm", e);
//		}
//	}

	@SuppressWarnings({"MagicNumber"})
	private void writeShows(ProviderID providerID) throws Exception
	{
		ProviderIDList providerIDList = new ProviderIDList();
		providerIDList.add(providerID);
		ShowProviderList showProviderList = ShowProviderList.findByProviderIDList(providerIDList);
		Show show;
		ShowCategoryList showCategoryList;

		ShowFormat showFormat1 = new ShowFormat();
		showFormat1.setMediaEncoding(MediaEncoding.WMV9);
		showFormat1.setMediaContainer(MediaContainer.ASF);
		showFormat1.setHorzResolution((short)600);
		showFormat1.setVertResolution((short)480);
		showFormat1.setFramesPerSecond((short)30);
		showFormat1.setBitRate((short)750);

		ShowFormat showFormat2 = new ShowFormat();
		showFormat2.setMediaEncoding(MediaEncoding.DivX5);
		showFormat2.setMediaContainer(MediaContainer.AVI);
		showFormat2.setHorzResolution((short)600);
		showFormat2.setVertResolution((short)480);
		showFormat2.setFramesPerSecond((short)30);
		showFormat2.setBitRate((short)750);

		writeStartElement("DataManager");
		for(ShowProvider showProvider : showProviderList)
		{
			show = Show.get(showProvider.getShowID());
			showCategoryList = ShowCategoryList.findByShowID(show.getShowID());

			writeStartElement("Show");
			writeDataID("ShowID", showProvider.getProviderShowID(), ShowID.MaxLength);
			writeString("Name", show.getName(), Show.NameMaxLength);
			writeString("EpisodeName", show.getEpisodeName(), Show.EpisodeNameMaxLength);
			writeString("EpisodeNumber", show.getEpisodeNumber(), Show.EpisodeNumberMaxLength);
			writeDate("ReleasedOn", show.getReleasedOn());
			writeShort("ReleasedYear", show.getReleasedYear());
			writeString("Description", show.getDescription(), Show.DescriptionMaxLength);
			writeShort("RunningMins", show.getRunningMins());

			writeCategoryList(showCategoryList);

			writeString("PictureURL", show.getPictureURL(), Show.PictureURLMaxLength);
			writeDataID("RatingID", show.getRatingID(), RatingID.MaxLength);
			writeDataID("LanguageID", LanguageID.English, LanguageID.MaxLength);
			writeBoolean("IsAdult", show.getIsAdult());

			// ShowFormat 1
			writeShowRental(showFormat1, showProvider.getShowCost());

			// ShowFormat 2
			writeShowRental(showFormat2, showProvider.getShowCost());

			writeEndElement("Show");
		}
		writeEndElement("DataManager");
	}

	private void writeStartDocument(String encoding) throws Exception
	{
		writeString("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
	}

	private void writeString(String data) throws IOException
	{
		fWriter.write(data);
	}

	private String encodeString(String data)
	{
		String newVal = data;

		if((data.indexOf("&") >= 0) || (data.indexOf("<") >= 0) || (data.indexOf("<") >= 0))
		{
			newVal = newVal.replaceAll("&", "&amp;");
			newVal = newVal.replaceAll("<", "&lt;");
			newVal = newVal.replaceAll(">", "&lt;");
		}

		return newVal;
	}

	/**
	 * Write an opending XML element tag
	 *
	 * @param name
	 */
	private void writeStartElement(String name) throws Exception
	{
		writeString("<");
		writeString(name);
		writeString(">");
	}

	/**
	 * Write a closing XML element tag
	 *
	 * @param name
	 */
	private void writeEndElement(String name) throws Exception
	{
		writeString("</");
		writeString(name);
		writeString(">");
	}

	private void writeElement(String name, String value) throws Exception
	{
		if ((value == null) || (value.length() == 0))
			return;

		writeStartElement(name);
		writeString(encodeString(value));
		writeEndElement(name);
	}

	/**
	 * Write a Short
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeShort(String fieldName, Short data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write a String
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 * @throws Exception
	 */
	public void writeString(String fieldName, String data, int maxLength) throws Exception
	{
		int len = (data != null) ? data.length() : 0;
		if(len > maxLength)
			throw new Exception("invalid len(" + len + "), maxLength(" + maxLength + ")");

		writeElement(fieldName, data);
	}

	/**
	 * Write a date, no Time component
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeDate(String fieldName, Date data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, (new ISO8601DateFormat()).format(data));
	}

	/**
	 * Write a Double
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeDouble(String fieldName, Double data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write a Boolean
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeBoolean(String fieldName, Boolean data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write a DataID Object
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 * @throws Exception
	 */
	public void writeDataID(String fieldName, DataID data, int maxLength) throws Exception
	{
		writeString(fieldName, (data != null) ? data.toString() : null, maxLength);
	}

	private void writeCategoryList(ShowCategoryList showCategoryList) throws Exception
	{
		for(ShowCategory showCategory : showCategoryList)
		{
			if(!CategoryID.Featured.equals(showCategory.getCategoryID()))
				writeDataID("CategoryID", showCategory.getCategoryID(), CategoryID.MaxLength);
		}
	}

	private void writeShowRental(ShowFormat showFormat, ShowCost showCost) throws Exception
	{
		writeStartElement("ShowRental");

		writeShowFormat(showFormat);
		writeShowCost(showCost);

		writeEndElement("ShowRental");
	}

	private void writeShowFormat(ShowFormat showFormat) throws Exception
	{
		writeStartElement("ShowFormat");

		writeString("MediaEncoding", MediaEncoding.convertToString(showFormat.getMediaEncoding()), MediaEncoding.MaxLength);
		writeString("MediaContainer", MediaContainer.convertToString(showFormat.getMediaContainer()), MediaContainer.MaxLength);
		writeShort("HorzResolution", showFormat.getHorzResolution());
		writeShort("VertResolution", showFormat.getVertResolution());
		writeShort("FramesPerSecond", showFormat.getFramesPerSecond());
		writeShort("BitRate", showFormat.getBitRate());

		writeEndElement("ShowFormat");
	}

	private void writeShowCost(ShowCost showCost) throws Exception
	{
		writeStartElement("ShowCost");

		writeString("ShowCostType", ShowCostType.convertToString(showCost.getShowCostType()), ShowCostType.MaxLength);

		Money cost = showCost.getCost();
		if((cost != null) && (cost.getCurrencyID() != null) && (cost.getAmount() != null))
		{
			writeStartElement("Cost");
			writeDataID("CurrencyID", cost.getCurrencyID(), CurrencyID.MaxLength);
			writeDouble("Amount", cost.getAmount());
			writeEndElement("Cost");
		}

		writeString("CostDisplay", showCost.getCostDisplay(), ShowCost.DescriptionMaxLength);
		writeShort("RentalHours", showCost.getRentalHours());

		writeEndElement("ShowCost");
	}
}
