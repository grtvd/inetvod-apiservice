/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.MemberID;

public class Member extends DatabaseObject
{
	/* Constants */
	private static final int FirstNameMaxLength = 32;
	private static final int LastNameMaxLength = 32;

	/* Fields */
	private MemberID fMemberID;
	private String fFirstName;
	private String fLastName;

	private static DatabaseAdaptor<Member, MemberList> fDatabaseAdaptor
		= new DatabaseAdaptor<Member, MemberList>(Member.class, MemberList.class);
	public static DatabaseAdaptor<Member, MemberList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public MemberID getMemberID() { return fMemberID; }

	public String getFirstName() { return fFirstName; }
	public void setFirstName(String firstName) { fFirstName = firstName; }

	public String getLastName() { return fLastName; }
	public void setLastName(String lastName) { fLastName = lastName; }

	/* Construction */
	protected Member()
	{
		super(true);
		fMemberID = MemberID.newInstance();
	}

	public Member(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static Member newInstance()
	{
		return new Member();
	}

	protected static Member load(MemberID memberID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(memberID, exists);
	}

	public static Member get(MemberID memberID) throws Exception
	{
		return load(memberID, DataExists.MustExist);
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fMemberID = reader.readDataID("MemberID", MemberID.MaxLength, MemberID.CtorString);
		fFirstName = reader.readString("FirstName", FirstNameMaxLength);
		fLastName = reader.readString("LastName", LastNameMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeString("FirstName", fFirstName, FirstNameMaxLength);
		writer.writeString("LastName", fLastName, LastNameMaxLength);
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
