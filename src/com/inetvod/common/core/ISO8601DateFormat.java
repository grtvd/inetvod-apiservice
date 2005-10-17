/* Copyright 2002, 2004 (C) The Skaringa team. All Rights Reserved. */

package com.inetvod.common.core;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Format and parse an ISO 8601 DateFormat used in XML documents.
 * The lexical representation for date is the reduced (right truncated)
 * lexical representation for dateTime: CCYY-MM-DD.
 * No left truncation is allowed.
 * An optional following time zone qualifier is allowed as for dateTime.
 */
public class ISO8601DateFormat extends ISO8601DateTimeFormat
{

	/**
	 * Construct a new ISO8601DateFormat using the default time zone.
	 */
	public ISO8601DateFormat()
	{
		setCalendar(Calendar.getInstance());
	}

	/**
	 * Construct a new ISO8601DateFormat using a specific time zone.
	 * @param tz The time zone used to format and parse the date.
	 */
	public ISO8601DateFormat(TimeZone tz)
	{
		setCalendar(Calendar.getInstance(tz));
	}

	/**
	 * @see DateFormat#parse(String, ParsePosition)
	 */
	public Date parse(String text, ParsePosition pos)
	{

		int i = pos.getIndex();

		try
		{
			int year = Integer.valueOf(text.substring(i, i + 4));
			i += 4;

			if(text.charAt(i) != '-')
			{
				throw new NumberFormatException();
			}
			i++;

			int month = (Integer.valueOf(text.substring(i, i + 2))) - 1;
			i += 2;

			if(text.charAt(i) != '-')
			{
				throw new NumberFormatException();
			}
			i++;

			int day = Integer.valueOf(text.substring(i, i + 2));
			i += 2;

			calendar.set(year, month, day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0); // no parts of a second

			i = parseTZ(i, text);

		}
		catch(NumberFormatException ex)
		{
			pos.setErrorIndex(i);
			i = 0;
			return null;
		}
		catch(IndexOutOfBoundsException ex)
		{
			pos.setErrorIndex(i);
			i = 0;
			return null;
		}
		finally
		{
			pos.setIndex(i);
		}

		return calendar.getTime();
	}

	/**
	 * @see DateFormat#format(Date, StringBuffer, FieldPosition)
	 */
	public StringBuffer format(
		Date date,
		StringBuffer sbuf,
		FieldPosition fieldPosition)
	{

		calendar.setTime(date);

		writeCCYYMM(sbuf);

		//writeTZ(sbuf);

		return sbuf;
	}
}
