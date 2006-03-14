/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.ShowCategoryID;
import com.inetvod.common.data.ShowID;

public class ShowCategory extends DatabaseObject
{
	/* Fields */
	private ShowCategoryID fShowCategoryID;
	private ShowID fShowID;
	private CategoryID fCategoryID;

	private static DatabaseAdaptor<ShowCategory, ShowCategoryList> fDatabaseAdaptor =
		new DatabaseAdaptor<ShowCategory, ShowCategoryList>(ShowCategory.class, ShowCategoryList.class);
	public static DatabaseAdaptor<ShowCategory, ShowCategoryList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public ShowCategoryID getShowCategoryID() { return fShowCategoryID; }
	public ShowID getShowID() { return fShowID; }
	public CategoryID getCategoryID() { return fCategoryID; }

	/* Construction */
	private ShowCategory(ShowID showID, CategoryID categoryID)
	{
		super(true);
		fShowCategoryID = ShowCategoryID.newInstance();
		fShowID = showID;
		fCategoryID = categoryID;
	}

	public ShowCategory(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static ShowCategory newInstance(ShowID showID, CategoryID categoryID)
	{
		return new ShowCategory(showID, categoryID);
	}

	/* Implementation */
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

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fShowCategoryID);
	}
}
