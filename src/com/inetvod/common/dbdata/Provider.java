/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.ProviderID;

public class Provider extends DatabaseObject
{
	/* Constants */
	public static final int NameMaxLength = 64;

	/* Fields */
	private ProviderID fProviderID;
	private String fName;

	private static DatabaseAdaptor<Provider, ProviderList> fDatabaseAdaptor =
		new DatabaseAdaptor<Provider, ProviderList>(Provider.class, ProviderList.class);
	public static DatabaseAdaptor<Provider, ProviderList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ProviderID getProviderID() { return fProviderID; }
	public String getName() { return fName; }

	/* Constuction */
	public Provider(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	private static Provider load(ProviderID providerID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(providerID, exists);
	}

	public static Provider get(ProviderID providerID) throws Exception
	{
		return load(providerID, DataExists.MustExist);
	}

	/* Implementation */
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
