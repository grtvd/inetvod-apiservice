/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient;

import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderShowID;
import com.inetvod.common.dbdata.ShowCostList;
import com.inetvod.providerClient.request.CheckShowAvailResp;
import com.inetvod.providerClient.request.CheckShowAvailRqst;
import com.inetvod.providerClient.request.DataRequestor;
import com.inetvod.providerClient.rqdata.ProviderStatusCode;

public class ProviderRequestor
{
	/* Fields */
	private Provider fProvider;
	private String fProviderRequestURL;
	private String fProviderAdminUserID;
	private String fProviderAdminPassword;
	private String fProviderMemberUserID;
	private String fProviderMemberPassword;

	private ProviderStatusCode fStatusCode;

	/* Getters and Setters */
	public void setMemberUser(String memberUserID, String memberPassword)
	{
		fProviderMemberUserID = memberUserID;
		fProviderMemberPassword = memberPassword;
	}

	public ProviderStatusCode getStatusCode() { return fStatusCode; }

	/* Construction */
	private ProviderRequestor(Provider provider)
	{
		fProvider = provider;
		fProviderRequestURL = "http://api.inetvod.com/provider/providerapi";	//TODO: get from Provider
		fProviderAdminUserID = "super";	//TODO: get from Provider
		fProviderAdminPassword = "superpassword";	//TODO: get from Provider
	}

	public static ProviderRequestor newInstance(Provider provider)
	{
		return new ProviderRequestor(provider);
	}

	/* Implementation */
	private DataRequestor newDataRequestor()
	{
		return DataRequestor.newInstance(fProviderRequestURL, fProviderAdminUserID, fProviderAdminPassword,
			fProviderMemberUserID, fProviderMemberPassword);
	}

	public boolean pingServer()
	{
		DataRequestor dataRequestor = newDataRequestor();
		dataRequestor.pingServer();

		fStatusCode = dataRequestor.getStatusCode();
		return ProviderStatusCode.sc_Success.equals(fStatusCode);
	}

	public ShowCostList checkShowAvail(ProviderShowID providerShowID)
	{
		DataRequestor dataRequestor = newDataRequestor();
		CheckShowAvailRqst checkShowAvailRqst = CheckShowAvailRqst.newInstance(providerShowID);
		CheckShowAvailResp checkShowAvailResp = dataRequestor.checkShowAvail(checkShowAvailRqst);

		fStatusCode = dataRequestor.getStatusCode();
		if(checkShowAvailResp != null)
			return checkShowAvailResp.getShowCostList();
		return null;
	}
}
