/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.DataExists;
import com.inetvod.common.data.ProviderConnectionID;
import com.inetvod.common.data.ProviderConnectionType;
import com.inetvod.common.data.ProviderID;

public class ProviderConnection extends DatabaseObject
{
	/* Constants */
	private static final int ConnectionURLMaxLength = 4096;
	private static final int AdminUserIDMaxLength = 128;	//64 if not encrypted
	private static final int AdminPasswordMaxLength = 32;	//16 if not encrypted
	private static final int UseFieldForNameMaxLength = 32;
	private static final int UseFieldForEpisodeNameMaxLength = 32;

	/* Fields */
	private ProviderConnectionID fProviderConnectionID;
	private ProviderID fProviderID;
	private ProviderConnectionType fProviderConnectionType;
	private boolean fDisabled;

	private String fConnectionURL;
	private String fAdminUserID;
	private String fAdminPassword;
	private String fUseFieldForName;
	private String fUseFieldForEpisodeName;

	private static DatabaseAdaptor<ProviderConnection, ProviderConnectionList> fDatabaseAdaptor =
		new DatabaseAdaptor<ProviderConnection, ProviderConnectionList>(ProviderConnection.class, ProviderConnectionList.class);
	public static DatabaseAdaptor<ProviderConnection, ProviderConnectionList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ProviderConnectionID getProviderConnectionID() { return fProviderConnectionID; }
	public ProviderID getProviderID() { return fProviderID; }
	public ProviderConnectionType getProviderConnectionType() { return fProviderConnectionType; }
	public boolean isEnabled() { return !fDisabled; }

	public String getConnectionURL() { return fConnectionURL; }
	public String getAdminUserID() { return fAdminUserID; }
	public String getAdminPassword() { return fAdminPassword; }
	public String getUseFieldForName() { return fUseFieldForName; }
	public String getUseFieldForEpisodeName() { return fUseFieldForEpisodeName; }

	/* Construction */
	public ProviderConnection(ProviderID providerID, ProviderConnectionType providerConnectionType)
	{
		super(true);
		fProviderConnectionID = ProviderConnectionID.newInstance();
		fProviderID = providerID;
		fProviderConnectionType = providerConnectionType;
	}

	public ProviderConnection(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static ProviderConnection newInstance(ProviderID providerID, ProviderConnectionType providerConnectionType)
	{
		return new ProviderConnection(providerID, providerConnectionType);
	}

	private static ProviderConnection load(ProviderConnectionID providerConnectionID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(providerConnectionID, exists);
	}

	public static ProviderConnection get(ProviderConnectionID providerConnectionID) throws Exception
	{
		return load(providerConnectionID, DataExists.MustExist);
	}

	public static ProviderConnection findByProviderIDConnectionType(ProviderID providerID,
		ProviderConnectionType providerConnectionType) throws Exception
	{
		ProviderConnectionList providerConnectionList = ProviderConnectionList.findByProviderIDConnectionType(
			providerID, providerConnectionType);

		if(providerConnectionList.size() == 1)
			return providerConnectionList.get(0);

		if(providerConnectionList.size() == 0)
			return null;

		throw new SearchException("Too Many Records Found");
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fProviderConnectionID = reader.readDataID("ProviderConnectionID", ProviderConnectionID.MaxLength,
			ProviderConnectionID.CtorString);
		fProviderID = reader.readDataID("ProviderID", ProviderID.MaxLength, ProviderID.CtorString);
		fProviderConnectionType = reader.readDataID("ProviderConnectionType", ProviderConnectionType.MaxLength, ProviderConnectionType.CtorString);
		fDisabled = reader.readBooleanValue("Disabled");

		fConnectionURL = reader.readString("ConnectionURL", ConnectionURLMaxLength);
		fAdminUserID = reader.readString("AdminUserID", AdminUserIDMaxLength);	//TODO: decrypt after reading
		fAdminPassword = reader.readString("AdminPassword", AdminPasswordMaxLength);	//TODO: decrypt after reading
		fUseFieldForName = reader.readString("UseFieldForName", UseFieldForNameMaxLength);
		fUseFieldForEpisodeName = reader.readString("UseFieldForEpisodeName", UseFieldForEpisodeNameMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ProviderConnectionID", fProviderConnectionID, ProviderConnectionID.MaxLength);
		writer.writeDataID("ProviderID", fProviderID, ProviderID.MaxLength);
		writer.writeDataID("ProviderConnectionType", fProviderConnectionType, ProviderConnectionType.MaxLength);
		writer.writeBooleanValue("Disabled", fDisabled);

		writer.writeString("ConnectionURL", fConnectionURL, ConnectionURLMaxLength);
		writer.writeString("AdminUserID", fAdminUserID, AdminUserIDMaxLength);	//TODO: encrypt before writing
		writer.writeString("AdminPassword", fAdminPassword, AdminPasswordMaxLength);	//TODO: encrypt before writing
		writer.writeString("UseFieldForName", fUseFieldForName, UseFieldForNameMaxLength);
		writer.writeString("UseFieldForEpisodeName", fUseFieldForEpisodeName, UseFieldForEpisodeNameMaxLength);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fProviderConnectionID);
	}
}
