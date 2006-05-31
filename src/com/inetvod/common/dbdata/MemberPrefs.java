/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.IncludeAdult;
import com.inetvod.common.data.MemberID;
import com.inetvod.common.data.RatingIDList;
import com.inetvod.common.data.ConnectionSpeed;
import com.inetvod.common.data.RatingID;

public class MemberPrefs extends DatabaseObject
{
	/* Constants */
	private static final int AdultPINMaxLength = 32;
	private static final int IncludeRatingIDListMaxLength = 126;

	/* Fields */
	private MemberID fMemberID;
	private IncludeAdult fIncludeAdult;
	private String fAdultPIN;
	private RatingIDList fIncludeRatingIDList = new RatingIDList();
	private boolean fIncludeDownload;
	private boolean fIncludeStreaming;
	private ConnectionSpeed fConnectionSpeed;

	private static DatabaseAdaptor<MemberPrefs, MemberPrefsList> fDatabaseAdaptor =
		new DatabaseAdaptor<MemberPrefs, MemberPrefsList>(MemberPrefs.class, MemberPrefsList.class);
	public static DatabaseAdaptor<MemberPrefs, MemberPrefsList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public MemberID getMemberID() { return fMemberID; }

	public IncludeAdult getIncludeAdult() { return fIncludeAdult; }
	public void setIncludeAdult(IncludeAdult includeAdult) { fIncludeAdult = includeAdult; }

	public String getAdultPIN() { return fAdultPIN; }
	public void setAdultPIN(String adultPIN) { fAdultPIN = adultPIN; }

	public RatingIDList getIncludeRatingIDList() { return fIncludeRatingIDList; }

	public boolean getIncludeDownload() { return fIncludeDownload; }
	public void setIncludeDownload(boolean includeDownload) { fIncludeDownload = includeDownload; }

	public boolean getIncludeStreaming() { return fIncludeStreaming; }
	public void setIncludeStreaming(boolean includeStreaming) { fIncludeStreaming = includeStreaming; }

	public ConnectionSpeed getConnectionSpeed() { return fConnectionSpeed; }
	public void setConnectionSpeed(ConnectionSpeed connectionSpeed) { fConnectionSpeed = connectionSpeed; }

	/* Construction */
	private MemberPrefs(MemberID memberID)
	{
		super(true);
		fMemberID = memberID;
		fIncludeAdult = IncludeAdult.Never;
	}

	public MemberPrefs(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static MemberPrefs newInstance(MemberID memberID)
	{
		//TODO: move default MemberPrefs to config file
		MemberPrefs memberPrefs = new MemberPrefs(memberID);
		memberPrefs.setIncludeAdult(IncludeAdult.Never);

		RatingIDList ratingIDList = memberPrefs.getIncludeRatingIDList();
		ratingIDList.clear();
		ratingIDList.add(RatingID.NotRated);
		ratingIDList.add(new RatingID("g"));
		ratingIDList.add(new RatingID("pg"));
		ratingIDList.add(new RatingID("pg13"));
		ratingIDList.add(new RatingID("tvy"));
		ratingIDList.add(new RatingID("tvy7"));
		ratingIDList.add(new RatingID("tvy7fv"));
		ratingIDList.add(new RatingID("tvpg"));
		ratingIDList.add(new RatingID("tv14"));

		memberPrefs.setIncludeDownload(true);
		memberPrefs.setIncludeStreaming(true);
		memberPrefs.setConnectionSpeed(ConnectionSpeed.S1500K);

		return memberPrefs;
	}

	private static MemberPrefs load(MemberID memberID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(memberID, exists);
	}

	public static MemberPrefs getCreate(MemberID memberID) throws Exception
	{
		MemberPrefs memberPrefs = load(memberID, DataExists.MayNotExist);

		if(memberPrefs == null)
			memberPrefs = newInstance(memberID);

		return memberPrefs;
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fMemberID = reader.readDataID("MemberID", MemberID.MaxLength, MemberID.CtorString);
		fIncludeAdult = IncludeAdult.convertFromString(reader.readString("IncludeAdult", IncludeAdult.MaxLength));
		fAdultPIN = reader.readString("AdultPIN", AdultPINMaxLength);
		fIncludeRatingIDList = reader.readStringList("IncludeRatingIDList", RatingID.MaxLength, RatingIDList.Ctor, RatingID.CtorString);
		fIncludeDownload = reader.readBooleanValue("IncludeDownload");
		fIncludeStreaming = reader.readBooleanValue("IncludeStreaming");
		fConnectionSpeed = ConnectionSpeed.convertFromString(reader.readString("ConnectionSpeed", ConnectionSpeed.MaxLength));
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeString("IncludeAdult", IncludeAdult.convertToString(fIncludeAdult), IncludeAdult.MaxLength);
		writer.writeString("AdultPIN", fAdultPIN, AdultPINMaxLength);
		writer.writeStringList("IncludeRatingIDList", fIncludeRatingIDList, IncludeRatingIDListMaxLength);
		writer.writeBooleanValue("IncludeDownload", fIncludeDownload);
		writer.writeBooleanValue("IncludeStreaming", fIncludeStreaming);
		writer.writeString("ConnectionSpeed", ConnectionSpeed.convertToString(fConnectionSpeed), ConnectionSpeed.MaxLength);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fMemberID);
	}

	static public void delete(MemberID memberID) throws Exception
	{
		fDatabaseAdaptor.delete(memberID);
	}
}
