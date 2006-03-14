/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;

public class ShowRental implements Readable, Writeable
{
	/* Constants */
	public static Constructor<ShowRental> CtorDataReader = DataReader.getCtor(ShowRental.class);

	/* Fields */
	private ShowFormatList fShowFormatList;
	private ShowCostList fShowCostList;

	/* Getters & Setters */
	public ShowFormatList getShowFormatList()
	{
		if(fShowFormatList == null)
			fShowFormatList = new ShowFormatList();
		return fShowFormatList;
	}

	public ShowCostList getShowCostList()
	{
		if(fShowCostList == null)
			fShowCostList = new ShowCostList();
		return fShowCostList;
	}

	/* Construction */
	public ShowRental()
	{
	}

	public ShowRental(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementations */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowFormatList = reader.readList("ShowFormat", ShowFormatList.Ctor, ShowFormat.CtorDataReader);
		fShowCostList = reader.readList("ShowCost", ShowCostList.Ctor, ShowCost.CtorDataReader);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeList("ShowFormat", fShowFormatList);
		writer.writeList("ShowCost", fShowCostList);
	}
}
