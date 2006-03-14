/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.util.Date;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.SystemConfiguation;
import com.inetvod.common.data.MemberID;
import com.inetvod.common.data.MemberSessionID;
import com.inetvod.common.data.PlayerID;

public class MemberSession extends DatabaseObject
{
	/* Fields */
	private MemberSessionID fMemberSessionID;
	private MemberID fMemberID;
	private PlayerID fPlayerID;
	private Date fStartedOn;
	private Date fExpiresAt;
	private boolean fShowAdult;

	private static DatabaseAdaptor<MemberSession, MemberSessionList> fDatabaseAdaptor =
		new DatabaseAdaptor<MemberSession, MemberSessionList>(MemberSession.class, MemberSessionList.class);
	public static DatabaseAdaptor<MemberSession, MemberSessionList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public MemberSessionID getMemberSessionID() { return fMemberSessionID; }

	public MemberID getMemberID() { return fMemberID; }

	public Date getStartedOn() { return fStartedOn; }
	public Date getExpiresAt() { return fExpiresAt; }

	public boolean getShowAdult() { return fShowAdult; }
	public void setShowAdult(boolean showAdult) { fShowAdult = showAdult; }

	/* Construction */
	protected MemberSession(MemberID memberID, PlayerID playerID)
	{
		super(true);
		fMemberSessionID = MemberSessionID.newInstance();
		fMemberID = memberID;
		fPlayerID = playerID;
		fStartedOn = new Date();
		fExpiresAt = new Date(fStartedOn.getTime() + SystemConfiguation.getThe().getSessionTimeoutMillis());
		fShowAdult = false;
	}

	public MemberSession(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static MemberSession newInstance(MemberID memberID, PlayerID providerID)
	{
		return new MemberSession(memberID, providerID);
	}

	protected static MemberSession load(MemberSessionID memberSessionID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(memberSessionID, exists);
	}

	public static MemberSession find(MemberSessionID memberSessionID) throws Exception
	{
		return load(memberSessionID, DataExists.MayNotExist);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fMemberSessionID = reader.readDataID("MemberSessionID", MemberSessionID.MaxLength, MemberSessionID.CtorString);
		fMemberID = reader.readDataID("MemberID", MemberID.MaxLength, MemberID.CtorString);
		fPlayerID = reader.readDataID("PlayerID", PlayerID.MaxLength, PlayerID.CtorString);
		fStartedOn = reader.readDateTime("StartedOn");
		fExpiresAt = reader.readDateTime("ExpiresAt");
		fShowAdult = reader.readBooleanValue("ShowAdult");
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("MemberSessionID", fMemberSessionID, MemberSessionID.MaxLength);
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeDataID("PlayerID", fPlayerID, PlayerID.MaxLength);
		writer.writeDateTime("StartedOn", fStartedOn);
		writer.writeDateTime("ExpiresAt", fExpiresAt);
		writer.writeBooleanValue("ShowAdult", fShowAdult);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fMemberSessionID);
	}
}
