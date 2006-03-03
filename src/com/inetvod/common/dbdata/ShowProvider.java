/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.sql.Types;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.ShowCost;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ShowProviderID;

public class ShowProvider extends DatabaseObject
{
	/* Fields */
	private ShowProviderID fShowProviderID;
	private ShowID fShowID;
	private ProviderID fProviderID;

	private ProviderShowID fProviderShowID;
	private ShowCost fShowCost;

	private static DatabaseAdaptor<ShowProvider, ShowProviderList> fDatabaseAdaptor =
		new DatabaseAdaptor<ShowProvider, ShowProviderList>(ShowProvider.class, ShowProviderList.class);
	public static DatabaseAdaptor<ShowProvider, ShowProviderList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ShowProviderID getShowProviderID() { return fShowProviderID; }

	public ShowID getShowID() { return fShowID; }

	public ProviderID getProviderID() { return fProviderID; }

	public ProviderShowID getProviderShowID() { return fProviderShowID; }

	public ShowCost getShowCost() { return fShowCost; }
	public void setShowCost(ShowCost showCost) { fShowCost = showCost; }

	/* Construction */
	private ShowProvider(ShowID showID, ProviderID providerID, ProviderShowID providerShowID)
	{
		super(true);
		fShowProviderID = ShowProviderID.newInstance();
		fShowID = showID;
		fProviderID = providerID;
		fProviderShowID = providerShowID;
	}

	public ShowProvider(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static ShowProvider newInstance(ShowID showID, ProviderID providerID, ProviderShowID providerShowID)
	{
		return new ShowProvider(showID, providerID, providerShowID);
	}

	private static ShowProvider loadByShowIDProviderID(ShowID showID, ProviderID providerID, DataExists dataExists)
		throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[2];

		params[0] = new DatabaseProcParam(Types.VARCHAR, showID.toString());
		params[1] = new DatabaseProcParam(Types.VARCHAR, providerID.toString());

		return fDatabaseAdaptor.selectByProc("ShowProvider_GetByShowIDProviderID", params, dataExists);
	}

	public static ShowProvider findByShowIDProviderID(ShowID showID, ProviderID providerID) throws Exception
	{
		return loadByShowIDProviderID(showID, providerID, DataExists.MayNotExist);
	}

	public static ShowProvider getByShowIDProviderID(ShowID showID, ProviderID providerID) throws Exception
	{
		return loadByShowIDProviderID(showID, providerID, DataExists.MustExist);
	}

	private static ShowProvider loadByProviderIDProviderShowID(ProviderID providerID, ProviderShowID providerShowID, DataExists dataExists)
		throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[2];

		params[0] = new DatabaseProcParam(Types.VARCHAR, providerID.toString());
		params[1] = new DatabaseProcParam(Types.VARCHAR, providerShowID.toString());

		return fDatabaseAdaptor.selectByProc("ShowProvider_GetByProviderIDProviderShowID", params, dataExists);
	}

	public static ShowProvider findByProviderIDProviderShowID(ProviderID providerID, ProviderShowID providerShowID) throws Exception
	{
		return loadByProviderIDProviderShowID(providerID, providerShowID, DataExists.MayNotExist);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowProviderID = reader.readDataID("ShowProviderID", ShowProviderID.MaxLength, ShowProviderID.CtorString);
		fShowID = reader.readDataID("ShowID", ShowID.MaxLength, ShowID.CtorString);
		fProviderID = reader.readDataID("ProviderID", ProviderID.MaxLength, ProviderID.CtorString);

		fProviderShowID = reader.readDataID("ProviderShowID", ProviderShowID.MaxLength, ProviderShowID.CtorString);
		fShowCost = reader.readObject("ShowCost", ShowCost.CtorDataReader);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ShowProviderID", fShowProviderID, ShowProviderID.MaxLength);
		writer.writeDataID("ShowID", fShowID, ShowID.MaxLength);
		writer.writeDataID("ProviderID", fProviderID, ProviderID.MaxLength);

		writer.writeDataID("ProviderShowID", fProviderShowID, ProviderShowID.MaxLength);
		writer.writeObject("ShowCost", fShowCost);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fShowProviderID);
	}
}
