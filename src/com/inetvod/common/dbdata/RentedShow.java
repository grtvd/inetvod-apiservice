/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.util.Date;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.MemberID;
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.data.RentedShowID;
import com.inetvod.common.data.ShowCost;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ProviderConnectionID;

public class RentedShow extends DatabaseObject
{
	/* Fields */
	private RentedShowID fRentedShowID;
	private MemberID fMemberID;
	private ShowID fShowID;
	private ProviderID fProviderID;
	private ProviderConnectionID fProviderConnectionID;

	private String fShowURL;
	private ShowCost fShowCost;
	private Date fRentedOn;
	private Date fAvailableUntil;

	private static DatabaseAdaptor<RentedShow, RentedShowList> fDatabaseAdaptor =
		new DatabaseAdaptor<RentedShow, RentedShowList>(RentedShow.class, RentedShowList.class);
	public static DatabaseAdaptor<RentedShow, RentedShowList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public RentedShowID getRentedShowID() { return fRentedShowID; }

	public MemberID getMemberID() { return fMemberID; }
	public ShowID getShowID() { return fShowID; }
	public ProviderID getProviderID() { return fProviderID; }
	public ProviderConnectionID getProviderConnectionID() { return fProviderConnectionID; }

	public String getShowURL() { return fShowURL; }
	public void setShowURL(String showURL) { fShowURL = showURL; }

	public ShowCost getShowCost() { return fShowCost; }
	public void setShowCost(ShowCost showCost) { fShowCost = showCost; }

	public Date getRentedOn() { return fRentedOn; }
	public void setRentedOn(Date rentedOn) { fRentedOn = rentedOn; }

	public Date getAvailableUntil() { return fAvailableUntil; }
	public void setAvailableUntil(Date availableUntil) { fAvailableUntil = availableUntil; }

	/* Construction */
	public RentedShow(MemberID memberID, ShowID showID, ProviderID providerID, ProviderConnectionID providerConnectionID)
	{
		super(true);
		fRentedShowID = RentedShowID.newInstance();
		fMemberID = memberID;
		fShowID = showID;
		fProviderID = providerID;
		fProviderConnectionID = providerConnectionID;
	}

	public RentedShow(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static RentedShow newInstance(MemberID memberID, ShowID showID, ProviderID providerID,
		ProviderConnectionID providerConnectionID)
	{
		return new RentedShow(memberID, showID, providerID, providerConnectionID);
	}

	protected static RentedShow load(RentedShowID rentedShowID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(rentedShowID, exists);
	}

	public static RentedShow get(RentedShowID rentedShowID) throws Exception
	{
		return load(rentedShowID, DataExists.MustExist);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fRentedShowID = reader.readDataID("RentedShowID", RentedShowID.MaxLength, RentedShowID.CtorString);
		fMemberID = reader.readDataID("MemberID", MemberID.MaxLength, MemberID.CtorString);
		fShowID = reader.readDataID("ShowID", ShowID.MaxLength, ShowID.CtorString);
		fProviderID = reader.readDataID("ProviderID", ProviderID.MaxLength, ProviderID.CtorString);
		fProviderConnectionID = reader.readDataID("ProviderConnectionID", ProviderConnectionID.MaxLength,
			ProviderConnectionID.CtorString);

		fShowURL = reader.readString("ShowURL", Show.ShowURLMaxLength);
		fShowCost = reader.readObject("ShowCost", ShowCost.CtorDataReader);
		fRentedOn = reader.readDateTime("RentedOn");
		fAvailableUntil = reader.readDateTime("AvailableUntil");
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("RentedShowID", fRentedShowID, RentedShowID.MaxLength);
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeDataID("ShowID", fShowID, ShowID.MaxLength);
		writer.writeDataID("ProviderID", fProviderID, ProviderID.MaxLength);
		writer.writeDataID("ProviderConnectionID", fProviderConnectionID, ProviderConnectionID.MaxLength);

		writer.writeString("ShowURL", fShowURL, Show.ShowURLMaxLength);
		writer.writeObject("ShowCost", fShowCost);
		writer.writeDateTime("RentedOn", fRentedOn);
		writer.writeDateTime("AvailableUntil", fAvailableUntil);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fRentedShowID);
	}

	static public void delete(RentedShowID rentedShowID) throws Exception
	{
		fDatabaseAdaptor.delete(rentedShowID);
	}
}
