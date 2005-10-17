/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.DataExists;

public class Rating extends DatabaseObject
{
	/* Constants */
	public static final int NumFields = 2;
	public static final int NameMaxLength = 64;

 	/* Properties */
	RatingID fRatingID;
	String fName;

	private static DatabaseAdaptor<Rating, RatingList> fDatabaseAdaptor =
		new DatabaseAdaptor<Rating, RatingList>(Rating.class, RatingList.class, NumFields);
	public static DatabaseAdaptor<Rating, RatingList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */


	/* Constuction Methods */
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
