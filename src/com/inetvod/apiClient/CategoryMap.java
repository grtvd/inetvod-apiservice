/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.data.CategoryID;

public class CategoryMap implements Readable
{
	/* Constants */
	public static final Constructor<CategoryMap> CtorDataReader = DataReader.getCtor(CategoryMap.class);
	private static final int CategoryMaxLength = 64;

	/* Fields */
	private CategoryID fCategoryID;
	private String fCategory;

	/* Getters and Setters */
	public CategoryID getCategoryID() { return fCategoryID; }
	public String getCategory() { return fCategory; }

	/* Construction */
	public CategoryMap(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fCategoryID = reader.readDataID("CategoryID", CategoryID.MaxLength, CategoryID.CtorString);
		fCategory = reader.readString("Category", CategoryMaxLength);
	}
}
