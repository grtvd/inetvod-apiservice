/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.ProviderConnectionID;
import com.inetvod.common.data.ProviderConnectionType;
import com.inetvod.common.data.ProviderID;

public class ProviderConnection extends DatabaseObject
{
	/* Constants */
	private static final int ConnectionURLMaxLength = 4096;

	/* Fields */
	private ProviderConnectionID fProviderConnectionID;
	private ProviderID fProviderID;
	private ProviderConnectionType fProviderConnectionType;

	private String fConnectionURL;

	private static DatabaseAdaptor<ProviderConnection, ProviderConnectionList> fDatabaseAdaptor =
		new DatabaseAdaptor<ProviderConnection, ProviderConnectionList>(ProviderConnection.class, ProviderConnectionList.class);
	public static DatabaseAdaptor<ProviderConnection, ProviderConnectionList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ProviderConnectionID getProviderConnectionID() { return fProviderConnectionID; }
	public ProviderID getProviderID() { return fProviderID; }
	public ProviderConnectionType getProviderConnectionType() { return fProviderConnectionType; }

	public String getConnectionURL() { return fConnectionURL; }
	public void setConnectionURL(String connectionURL) { fConnectionURL = connectionURL; }

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

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fProviderConnectionID = reader.readDataID("ProviderConnectionID", ProviderConnectionID.MaxLength,
			ProviderConnectionID.CtorString);
		fProviderID = reader.readDataID("ProviderID", ProviderID.MaxLength, ProviderID.CtorString);
		fProviderConnectionType = reader.readDataID("ProviderConnectionType", ProviderConnectionType.MaxLength, ProviderConnectionType.CtorString);

		fConnectionURL = reader.readString("ConnectionURL", ConnectionURLMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ProviderConnectionID", fProviderConnectionID, ProviderConnectionID.MaxLength);
		writer.writeDataID("ProviderID", fProviderID, ProviderID.MaxLength);
		writer.writeDataID("ProviderConnectionType", fProviderConnectionType, ProviderConnectionType.MaxLength);

		writer.writeString("ConnectionURL", fConnectionURL, ConnectionURLMaxLength);
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
