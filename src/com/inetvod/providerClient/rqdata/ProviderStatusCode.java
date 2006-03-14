/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.rqdata;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"MagicNumber"})
public class ProviderStatusCode
{
	public static final ProviderStatusCode sc_Success = new ProviderStatusCode(0);

	public static final ProviderStatusCode sc_RequestNotSupported = new ProviderStatusCode(1);
	public static final ProviderStatusCode sc_RequestMissingRequired = new ProviderStatusCode(2);
	public static final ProviderStatusCode sc_RequestInvalid = new ProviderStatusCode(3);

	public static final ProviderStatusCode sc_InvalidMemberUserID = new ProviderStatusCode(1000);
	public static final ProviderStatusCode sc_InvalidAdminUserID = new ProviderStatusCode(1001);
	public static final ProviderStatusCode sc_UserIDInUse = new ProviderStatusCode(1002);

	public static final ProviderStatusCode sc_ShowNoAccess = new ProviderStatusCode(1100);
	public static final ProviderStatusCode sc_ShowLevelInsufficient = new ProviderStatusCode(1101);
	public static final ProviderStatusCode sc_ShowNotRented = new ProviderStatusCode(1102);
	public static final ProviderStatusCode sc_ShowRentExpired = new ProviderStatusCode(1103);
	public static final ProviderStatusCode sc_ShowPaymentDenied = new ProviderStatusCode(1104);

	// NOTE: These errors are not returned by from the Provider
	public static final ProviderStatusCode sc_ProviderConnectionError = new ProviderStatusCode(9000);
	// NOTE: These errors are not returned by from the Provider

	public static final ProviderStatusCode sc_GeneralError = new ProviderStatusCode(9999);

	private static List<ProviderStatusCode> fAllValues = Arrays.asList(new ProviderStatusCode[]
		{
			sc_Success,
			sc_RequestNotSupported,
			sc_RequestMissingRequired,
			sc_RequestInvalid,
			sc_InvalidMemberUserID,
			sc_InvalidAdminUserID,
			sc_UserIDInUse,
			sc_ShowNoAccess,
			sc_ShowLevelInsufficient,
			sc_ShowNotRented,
			sc_ShowRentExpired,
			sc_ShowPaymentDenied,
			sc_ProviderConnectionError,
			sc_GeneralError
		});

	protected int fValue;

	private ProviderStatusCode(int value)
	{
		fValue = value;
	}

	public static ProviderStatusCode convertFromInt(Integer value)
	{
		if(value == null)
			return sc_Success;

		for(ProviderStatusCode statusCode : fAllValues)
			if(statusCode.fValue == value)
				return statusCode;

		return sc_GeneralError;
	}

	public static Integer convertToInt(ProviderStatusCode value)
	{
		if(value == null)
			return sc_Success.fValue;

		return value.fValue;
	}
}
