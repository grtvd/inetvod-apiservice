/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;

public class Provider extends DatabaseObject
{
	/* Constants */
	public static final int NameMaxLength = 64;
	private static final int RequestURLMaxLength = 4096;
	private static final int AdminUserIDMaxLength = 128;	//64 if not encrypted
	private static final int AdminPasswordMaxLength = 32;	//16 if not encrypted

	/* Fields */
	private ProviderID fProviderID;
	private String fName;

	private String fRequestURL;
	private String fAdminUserID;
	private String fAdminPassword;

	private static DatabaseAdaptor<Provider, ProviderList> fDatabaseAdaptor =
		new DatabaseAdaptor<Provider, ProviderList>(Provider.class, ProviderList.class);
	public static DatabaseAdaptor<Provider, ProviderList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ProviderID getProviderID() { return fProviderID; }
	public String getName() { return fName; }

	public String getRequestURL() { return fRequestURL; }
	public String getAdminUserID() { return fAdminUserID; }
	public String getAdminPassword() { return fAdminPassword; }

	/* Constuction */
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

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fProviderID = reader.readDataID("ProviderID", ProviderID.MaxLength, ProviderID.CtorString);
		fName = reader.readString("Name", NameMaxLength);

		fRequestURL = reader.readString("RequestURL", RequestURLMaxLength);
		fAdminUserID = reader.readString("AdminUserID", AdminUserIDMaxLength);	//TODO: decrypt after reading
		fAdminPassword = reader.readString("AdminPassword", AdminPasswordMaxLength);	//TODO: decrypt after reading
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ProviderID", fProviderID, ProviderID.MaxLength);
		writer.writeString("Name", fName, NameMaxLength);

		writer.writeString("RequestURL", fRequestURL, RequestURLMaxLength);
		writer.writeString("AdminUserID", fAdminUserID, AdminUserIDMaxLength);	//TODO: encrypt before writing
		writer.writeString("AdminPassword", fAdminPassword, AdminPasswordMaxLength);	//TODO: encrypt before writing
	}
}
