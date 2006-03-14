/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

public class ShowCostType
{
	public static final int MaxLength = 32;

	public static final ShowCostType Free = new ShowCostType("Free");
	public static final ShowCostType Subscription = new ShowCostType("Subscription");
	public static final ShowCostType PayPerView = new ShowCostType("PayPerView");

	private final String fValue;

	private ShowCostType(String value)
	{
		fValue = value;
	}

	public String toString()
	{
		return fValue;
	}

	public static ShowCostType convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(Free.fValue.equals(value))
			return Free;
		if(Subscription.fValue.equals(value))
			return Subscription;
		if(PayPerView.fValue.equals(value))
			return PayPerView;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(ShowCostType value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
