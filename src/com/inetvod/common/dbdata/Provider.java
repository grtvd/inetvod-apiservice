/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.DataExists;

public class Provider extends DatabaseObject
{
	/* Constants */
	public static final int NumFields = 2;
	public static final int NameMaxLength = 64;

	/* Properties */
	ProviderID fProviderID;
	String fName;

	private static DatabaseAdaptor<Provider, ProviderList> fDatabaseAdaptor =
		new DatabaseAdaptor<Provider, ProviderList>(Provider.class, ProviderList.class, NumFields);
	public static DatabaseAdaptor<Provider, ProviderList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ProviderID getProviderID() { return fProviderID; }
	public String getName() { return fName; }

	/* Constuction Methods */
	public Provider(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	protected static Provider load(ProviderID providerID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(providerID, exists);
	}

	public static Provider get(ProviderID providerID) throws Exception
	{
		return load(providerID, DataExists.MustExist);
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fProviderID = reader.readDataID("ProviderID", ProviderID.MaxLength, ProviderID.CtorString);
		fName = reader.readString("Name", NameMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ProviderID", fProviderID, ProviderID.MaxLength);
		writer.writeString("Name", fName, NameMaxLength);
	}
}
