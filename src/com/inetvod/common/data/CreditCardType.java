/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

public class CreditCardType
{
	public static final int MaxLength = 16;

	public static final CreditCardType Visa = new CreditCardType("Visa");
	public static final CreditCardType MasterCard = new CreditCardType("MasterCard");
	public static final CreditCardType AmericanExpress = new CreditCardType("AmericanExpress");

	private final String fValue;

	private CreditCardType(String name)
	{
		fValue = name;
	}

	public String toString()
	{
		return fValue;
	}

	public static CreditCardType convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(Visa.fValue.equals(value))
			return Visa;
		if(MasterCard.fValue.equals(value))
			return MasterCard;
		if(AmericanExpress.fValue.equals(value))
			return AmericanExpress;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(CreditCardType value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
