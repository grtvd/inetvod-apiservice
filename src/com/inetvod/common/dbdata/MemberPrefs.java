/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.IncludeAdult;
import com.inetvod.common.data.MemberID;

public class MemberPrefs extends DatabaseObject
{
	/* Properties */
	private MemberID fMemberID;
	private IncludeAdult fIncludeAdult;

	private static DatabaseAdaptor<MemberPrefs, MemberPrefsList> fDatabaseAdaptor =
		new DatabaseAdaptor<MemberPrefs, MemberPrefsList>(MemberPrefs.class, MemberPrefsList.class);
	public static DatabaseAdaptor<MemberPrefs, MemberPrefsList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public MemberID getMemberID() { return fMemberID; }

	public IncludeAdult getIncludeAdult() { return fIncludeAdult; }
	public void setIncludeAdult(IncludeAdult includeAdult) { fIncludeAdult = includeAdult; }

	/* Constuction Methods */
	protected MemberPrefs(MemberID memberID)
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
		return new MemberPrefs(memberID);
	}

	protected static MemberPrefs load(MemberID memberID, DataExists exists) throws Exception
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
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeString("IncludeAdult", IncludeAdult.convertToString(fIncludeAdult), IncludeAdult.MaxLength);
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
