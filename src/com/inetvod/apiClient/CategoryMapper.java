/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.XmlDataReader;
import com.inetvod.common.data.CategoryID;

public class CategoryMapper implements Readable
{
	/* Constants */
	public static final Constructor<CategoryMapper> CtorDataReader = DataReader.getCtor(CategoryMapper.class);

	/* Fields */
	private static CategoryMapper fTheCategoryMapper;

	private HashMap<String, CategoryID> fCategoryMapHash;

	/* Getters and Setters */
	public static CategoryMapper getThe() { return fTheCategoryMapper; }

	//public ShowList getShowList() { return fShowList; }

	/* Construction */
	public CategoryMapper(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public static void load(String categoryMapperXml) throws Exception
	{
		FileInputStream inputStream = new FileInputStream(new File(categoryMapperXml));
		XmlDataReader dataReader = new XmlDataReader(inputStream);

		fTheCategoryMapper = dataReader.readObject("CategoryMapper", CtorDataReader);
	}

	public void readFrom(DataReader reader) throws Exception
	{
		CategoryMapList categoryMapList = reader.readList("CategoryMap", CategoryMapList.Ctor, CategoryMap.CtorDataReader);

		fCategoryMapHash = new HashMap<String, CategoryID>();
		for(CategoryMap categoryMap : categoryMapList)
			fCategoryMapHash.put(categoryMap.getCategory(), categoryMap.getCategoryID());
	}

	public CategoryID mapCategory(String category)
	{
		return fCategoryMapHash.get(category);
	}
}