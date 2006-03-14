/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.RatingID;

public class Rating extends DatabaseObject
{
	/* Constants */
	private static final int NameMaxLength = 64;

	 /* Fields */
	private RatingID fRatingID;
	private String fName;

	private static DatabaseAdaptor<Rating, RatingList> fDatabaseAdaptor =
		new DatabaseAdaptor<Rating, RatingList>(Rating.class, RatingList.class);
	public static DatabaseAdaptor<Rating, RatingList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */


	/* Construction */
	public Rating(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	protected static Rating load(RatingID ratingID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(ratingID, exists);
	}

	public static Rating get(RatingID ratingID) throws Exception
	{
		return load(ratingID, DataExists.MustExist);
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fRatingID = reader.readDataID("RatingID", RatingID.MaxLength, RatingID.CtorString);
		fName = reader.readString("Name", NameMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("RatingID", fRatingID, RatingID.MaxLength);
		writer.writeString("Name", fName, NameMaxLength);
	}
}
