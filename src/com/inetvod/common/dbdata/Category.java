/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.CategoryID;

public class Category extends DatabaseObject
{
	/* Constants */
	private static final int NameMaxLength = 64;

	/* Fields */
	private CategoryID fCategoryID;
	private String fName;

	private static DatabaseAdaptor<Category, CategoryList> fDatabaseAdaptor
		= new DatabaseAdaptor<Category, CategoryList>(Category.class, CategoryList.class);
	public static DatabaseAdaptor<Category, CategoryList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters & Setters */
	public CategoryID getCategoryID() { return fCategoryID; }

	/* Construction */
	public Category(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	protected static Category load(CategoryID categoryID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(categoryID, exists);
	}

	public static Category get(CategoryID categoryID) throws Exception
	{
		return load(categoryID, DataExists.MustExist);
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fCategoryID = reader.readDataID("CategoryID", CategoryID.MaxLength, CategoryID.CtorString);
		fName = reader.readString("Name", NameMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("CategoryID", fCategoryID, CategoryID.MaxLength);
		writer.writeString("Name", fName, NameMaxLength);
	}
}
