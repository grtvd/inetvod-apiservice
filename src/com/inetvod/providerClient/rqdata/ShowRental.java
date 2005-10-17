/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.rqdata;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.dbdata.ShowCost;
import com.inetvod.common.dbdata.ShowCostList;
import com.inetvod.common.dbdata.ShowFormat;
import com.inetvod.common.dbdata.ShowFormatList;

public class ShowRental implements Readable, Writeable
{
	/* Constants */
	public static Constructor<ShowRental> CtorDataReader = DataReader.getCtor(ShowRental.class);

	/* Fields */
	private ShowFormatList fShowFormatList;
	private ShowCostList fShowCostList;

	/* Getters & Setters */
	public ShowFormatList getShowFormatList() { return fShowFormatList; }
	public ShowCostList getShowCostList() { return fShowCostList; }

	/* Construction */
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
