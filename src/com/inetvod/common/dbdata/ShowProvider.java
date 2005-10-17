/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.sql.Types;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;

public class ShowProvider extends DatabaseObject
{
	/* Constants */
	public static final int NumFields = 6;

	/* Properties */
	protected ShowProviderID fShowProviderID;
	protected ShowID fShowID;
	protected ProviderID fProviderID;
	protected ProviderShowID fProviderShowID;
	protected ShowCost fShowCost;

	private static DatabaseAdaptor<ShowProvider, ShowProviderList> fDatabaseAdaptor =
		new DatabaseAdaptor<ShowProvider, ShowProviderList>(ShowProvider.class, ShowProviderList.class, NumFields);
	public static DatabaseAdaptor<ShowProvider, ShowProviderList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ShowProviderID getShowProviderID() { return fShowProviderID; }

	public ShowID getShowID() { return fShowID; }

	public ProviderID getProviderID() { return fProviderID; }

	public ProviderShowID getProviderShowID() { return fProviderShowID; }

	public ShowCost getShowCost() { return fShowCost; }

	/* Constuction Methods */
	public ShowProvider(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

//		protected static ShowProvider Load(ShowProviderID showProviderID, bool mayNotExist)
//		{
//			SqlCommand command = new SqlCommand("sp_ShowProvider_Get");
//
//			command.CommandType = CommandType.StoredProcedure;
//			command.Parameters.Add("@ShowID", showID.ToString());
//			command.Parameters.Add("@ProviderID", providerID.ToString());
//
//			return DatabaseAdaptor.Select(command, mayNotExist) as ShowProvider;
//		}
//
//		public static ShowProvider Get(ShowProviderID showProviderID)
//		{
//			return Load(showProviderID, false);
//		}

	protected static ShowProvider loadByShowIDProviderID(ShowID showID, ProviderID providerID, DataExists dataExists)
		throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[2];

		params[0] = new DatabaseProcParam(Types.VARCHAR, showID.toString());
		params[1] = new DatabaseProcParam(Types.VARCHAR, providerID.toString());

		return fDatabaseAdaptor.selectByProc("ShowProvider_GetByShowIDProviderID", params, dataExists);
	}

	public static ShowProvider getByShowIDProviderID(ShowID showID, ProviderID providerID) throws Exception
	{
		return loadByShowIDProviderID(showID, providerID, DataExists.MustExist);
	}

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
}
