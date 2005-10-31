/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;

public class SerialNumber extends DatabaseObject
{
	/* Constants */
	private static final int SerialNumberIDMaxLength = 64;
	private static final int PINMaxLength = 16;

	/* Fields */
	private String fSerialNumberID;
	private boolean fActive;
	private MemberID fMemberID;
	private String fPIN;

	private static DatabaseAdaptor<SerialNumber, SerialNumberList> fDatabaseAdaptor =
		new DatabaseAdaptor<SerialNumber, SerialNumberList>(SerialNumber.class, SerialNumberList.class);
	public static DatabaseAdaptor<SerialNumber, SerialNumberList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public String getSerialNumberID() { return fSerialNumberID; }

	public boolean getActive() { return fActive; }

	public MemberID getMemberID() { return fMemberID; }

	public String getPIN() { return fPIN; }
	public void setPIN(String PIN) { fPIN = PIN; }

	/* Constuction Methods */
	public SerialNumber(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	protected static SerialNumber load(String serialNumberID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(serialNumberID, exists);
	}

	public static SerialNumber get(String serialNumberID) throws Exception
	{
		return load(serialNumberID, DataExists.MustExist);
	}

	public static SerialNumber find(String serialNumberID) throws Exception
	{
		return load(serialNumberID, DataExists.MayNotExist);
	}

	/* Activation */
	public void SetActive(MemberID memberID, String pin)
	{
		fMemberID = memberID;
		fActive = true;
		fPIN = pin;
	}

	public void SetInactive()
	{
		fMemberID = null;
		fActive = false;
		fPIN = null;
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fSerialNumberID = reader.readString("SerialNumberID", SerialNumberIDMaxLength);
		fActive = reader.readBooleanValue("Active");
		fMemberID = reader.readDataID("MemberID", MemberID.MaxLength, MemberID.CtorString);
		fPIN = reader.readString("PIN", PINMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("SerialNumberID", fSerialNumberID, SerialNumberIDMaxLength);
		writer.writeBooleanValue("Active", fActive);
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeString("PIN", fPIN, PINMaxLength);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fSerialNumberID);
	}
}
