/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;

public class ShowCategory extends  DatabaseObject
{
	/* Constants */
	public static final int NumFields = 3;

	/* Fields */
	protected ShowCategoryID fShowCategoryID;
	protected ShowID fShowID;
	protected CategoryID fCategoryID;

	private static DatabaseAdaptor<ShowCategory, ShowCategoryList> fDatabaseAdaptor =
		new DatabaseAdaptor<ShowCategory, ShowCategoryList>(ShowCategory.class, ShowCategoryList.class, NumFields);
	public static DatabaseAdaptor<ShowCategory, ShowCategoryList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ShowCategoryID getShowCategoryID() { return fShowCategoryID; }
	public ShowID getShowID() { return fShowID; }
	public CategoryID getCategoryID() { return fCategoryID; }

	/* Constuction Methods */
	public ShowCategory(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	/* Streamable Members */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowCategoryID = reader.readDataID("ShowCategoryID", ShowCategoryID.MaxLength, ShowCategoryID.CtorString);
		fShowID = reader.readDataID("ShowID", ShowID.MaxLength, ShowID.CtorString);
		fCategoryID = reader.readDataID("CategoryID", CategoryID.MaxLength, CategoryID.CtorString);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ShowCategoryID", fShowCategoryID, ShowCategoryID.MaxLength);
		writer.writeDataID("ShowID", fShowID, ShowID.MaxLength);
		writer.writeDataID("CategoryID", fCategoryID, CategoryID.MaxLength);
	}

}
