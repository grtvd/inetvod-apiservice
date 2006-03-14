/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.rqdata;

public class PaymentType
{
	public static final int MaxLength = 32;

	public static final PaymentType ChargeAccount = new PaymentType("ChargeAccount");
	public static final PaymentType CreditCard = new PaymentType("CreditCard");

	private final String fValue;

	private PaymentType(String name)
	{
		fValue = name;
	}

	public String toString()
	{
		return fValue;
	}

	public static PaymentType convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(ChargeAccount.fValue.equals(value))
			return ChargeAccount;
		if(CreditCard.fValue.equals(value))
			return CreditCard;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(PaymentType value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
