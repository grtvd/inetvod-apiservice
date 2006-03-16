/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.sql.Timestamp;

public class DateUtil
{
	/* Fields */
	public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	/* Implementation */

	/**
	 * Drops the time component from date, using default time zone, returning SQL date
	 * @param date
	 * @return SQL date w/o time
	 */
	public static java.sql.Date convertToDBDate(Date date)
	{
		if(date == null)
			return null;

		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		cal.clear();
		cal.set(year, month, day);
		return new java.sql.Date(cal.getTime().getTime());
	}

	/**
	 * For storing all date/times in DB as if GMT.
	 * @param date
	 * @return converted Timestamp as if GMT
	 */
	public static Timestamp convertToDBTimestamp(Date date)
	{
		if(date == null)
			return null;

		TimeZone timeZone = TimeZone.getDefault();
		int offsetGMT = timeZone.getOffset(date.getTime());

		Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(date);
		cal.add(Calendar.MILLISECOND, -offsetGMT);

		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * For storing all date/times in DB as if GMT.
	 * @param date
	 * @return converted Timestamp as if GMT
	 */
	public static Date convertFromDBTimestamp(Timestamp date)
	{
		if(date == null)
			return null;

		TimeZone timeZone = TimeZone.getDefault();
		int offsetGMT = timeZone.getOffset(date.getTime());

		Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(date);
		cal.add(Calendar.MILLISECOND, offsetGMT);

		return cal.getTime();
	}
}
