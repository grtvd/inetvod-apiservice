/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.sql.Types;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.MemberID;
import com.inetvod.common.data.MemberProviderID;
import com.inetvod.common.data.ProviderID;

public class MemberProvider extends DatabaseObject
{
	/* Constants */
	public static final int EncryptedUserIDMaxLength = 128;
	public static final int EncryptedPasswordMaxLength = 32;

	/* Fields */
	private MemberProviderID fMemberProviderID;
	private MemberID fMemberID;
	private ProviderID fProviderID;
	private String fEncryptedUserName;	//TODO: rename to UserID
	private String fEncryptedPassword;

	private static DatabaseAdaptor<MemberProvider, MemberProviderList> fDatabaseAdaptor =
		new DatabaseAdaptor<MemberProvider, MemberProviderList>(MemberProvider.class, MemberProviderList.class);
	public static DatabaseAdaptor<MemberProvider, MemberProviderList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public MemberProviderID getMemberProviderID() { return fMemberProviderID; }

	public MemberID getMemberID() { return fMemberID; }
	public ProviderID getProviderID() { return fProviderID; }

	public String getEncryptedUserName() { return fEncryptedUserName; }
	public void setEncryptedUserName(String encryptedUserName) { fEncryptedUserName = encryptedUserName; }

	public String getEncryptedPassword() { return fEncryptedPassword; }
	public void setEncryptedPassword(String encryptedPassword) { fEncryptedPassword = encryptedPassword; }

	/* Construction */
	protected MemberProvider(MemberID memberID, ProviderID providerID)
	{
		super(true);
		fMemberProviderID = MemberProviderID.newInstance();
		fMemberID = memberID;
		fProviderID = providerID;
	}

	public MemberProvider(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static MemberProvider newInstance(MemberID memberID, ProviderID providerID)
	{
		return new MemberProvider(memberID, providerID);
	}

	protected static MemberProvider load(MemberProviderID memberProviderID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(memberProviderID, exists);
	}

	public static MemberProvider get(MemberProviderID memberProviderID) throws Exception
	{
		return load(memberProviderID, DataExists.MustExist);
	}

	protected static MemberProvider loadByMemberIDProviderID(MemberID memberID, ProviderID providerID, DataExists dataExists)
		throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[2];

		params[0] = new DatabaseProcParam(Types.VARCHAR, memberID.toString());
		params[1] = new DatabaseProcParam(Types.VARCHAR, providerID.toString());

		return fDatabaseAdaptor.selectByProc("MemberProvider_GetByMemberIDProviderID", params, dataExists);
	}

	public static MemberProvider findByMemberIDProviderID(MemberID memberID, ProviderID providerID) throws Exception
	{
		return loadByMemberIDProviderID(memberID, providerID, DataExists.MayNotExist);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fMemberProviderID = reader.readDataID("MemberProviderID", MemberProviderID.MaxLength,
			MemberProviderID.CtorString);
		fMemberID = reader.readDataID("MemberID", MemberID.MaxLength, MemberID.CtorString);
		fProviderID = reader.readDataID("ProviderID", ProviderID.MaxLength, ProviderID.CtorString);
		fEncryptedUserName = reader.readString("EncryptedUserName", EncryptedUserIDMaxLength);
		fEncryptedPassword = reader.readString("EncryptedPassword", EncryptedPasswordMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("MemberProviderID", fMemberProviderID, MemberProviderID.MaxLength);
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeDataID("ProviderID", fProviderID, ProviderID.MaxLength);
		writer.writeString("EncryptedUserName", fEncryptedUserName, EncryptedUserIDMaxLength);
		writer.writeString("EncryptedPassword", fEncryptedPassword, EncryptedPasswordMaxLength);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fMemberProviderID);
	}
}
