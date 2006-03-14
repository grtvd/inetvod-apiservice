/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient;

import com.inetvod.common.data.MemberID;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.ShowCost;
import com.inetvod.common.data.ShowFormat;
import com.inetvod.common.data.ShowIDList;
import com.inetvod.common.dbdata.MemberProvider;
import com.inetvod.common.dbdata.ProviderConnection;
import com.inetvod.providerClient.request.CheckShowAvailResp;
import com.inetvod.providerClient.request.CheckShowAvailRqst;
import com.inetvod.providerClient.request.DataRequestor;
import com.inetvod.providerClient.request.EnrollRqst;
import com.inetvod.providerClient.request.ReleaseShowRqst;
import com.inetvod.providerClient.request.RentShowResp;
import com.inetvod.providerClient.request.RentShowRqst;
import com.inetvod.providerClient.request.ShowDetailResp;
import com.inetvod.providerClient.request.ShowDetailRqst;
import com.inetvod.providerClient.request.ShowListResp;
import com.inetvod.providerClient.request.WatchShowResp;
import com.inetvod.providerClient.request.WatchShowRqst;
import com.inetvod.providerClient.rqdata.Payment;
import com.inetvod.providerClient.rqdata.ProviderStatusCode;
import com.inetvod.providerClient.rqdata.ShowDetailList;

public class ProviderRequestor
{
	/* Fields */
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
	private ProviderRequestor(ProviderConnection providerConnection)
	{
		fProviderRequestURL = providerConnection.getConnectionURL();
		fProviderAdminUserID = providerConnection.getAdminUserID();
		fProviderAdminPassword = providerConnection.getAdminPassword();
	}

	public static ProviderRequestor newInstance(ProviderConnection providerConnection)
	{
		return new ProviderRequestor(providerConnection);
	}

	public static ProviderRequestor newInstance(ProviderConnection providerConnection,
		MemberID memberID) throws Exception
	{
		ProviderRequestor providerRequestor = newInstance(providerConnection);

		MemberProvider memberProvider = MemberProvider.findByMemberIDProviderID(memberID,
			providerConnection.getProviderID());
		//TODO: need to decrypt Member's Provider credentials
		if(memberProvider != null)
			providerRequestor.setMemberUser(memberProvider.getEncryptedUserName(), memberProvider.getEncryptedPassword());

		return providerRequestor;
	}

	/* Implementation */
	private DataRequestor newDataRequestor(boolean memberRequest)
	{
		if(memberRequest)
			return DataRequestor.newInstance(fProviderRequestURL, fProviderAdminUserID, fProviderAdminPassword,
				fProviderMemberUserID, fProviderMemberPassword);
		else
			return DataRequestor.newInstance(fProviderRequestURL, fProviderAdminUserID, fProviderAdminPassword,
				null, null);
	}

	public boolean pingServer()
	{
		DataRequestor dataRequestor = newDataRequestor(false);
		dataRequestor.pingServer();

		fStatusCode = dataRequestor.getStatusCode();
		return ProviderStatusCode.sc_Success.equals(fStatusCode);
	}

	public boolean enroll(EnrollRqst enrollRqst)
	{
		DataRequestor dataRequestor = newDataRequestor(false);
		dataRequestor.enroll(enrollRqst);

		fStatusCode = dataRequestor.getStatusCode();
		return ProviderStatusCode.sc_Success.equals(fStatusCode);
	}

	public ShowIDList showList()
	{
		DataRequestor dataRequestor = newDataRequestor(false);
		ShowListResp showListResp = dataRequestor.showList();

		fStatusCode = dataRequestor.getStatusCode();
		if(showListResp != null)
			return showListResp.getShowIDList();
		return null;
	}

	public ShowDetailList showDetail(ShowIDList showIDList)
	{
		DataRequestor dataRequestor = newDataRequestor(false);
		ShowDetailRqst showDetailRqst = ShowDetailRqst.newInstance(showIDList);
		ShowDetailResp showDetailResp = dataRequestor.showDetail(showDetailRqst);

		fStatusCode = dataRequestor.getStatusCode();
		if(showDetailResp != null)
			return showDetailResp.getShowDetailList();
		return null;
	}

	public ShowCost checkShowAvail(ProviderShowID providerShowID, ShowFormat showFormat, ShowCost showCost)
	{
		DataRequestor dataRequestor = newDataRequestor(true);
		CheckShowAvailRqst checkShowAvailRqst = CheckShowAvailRqst.newInstance(providerShowID, showFormat, showCost);
		CheckShowAvailResp checkShowAvailResp = dataRequestor.checkShowAvail(checkShowAvailRqst);

		fStatusCode = dataRequestor.getStatusCode();
		if(checkShowAvailResp != null)
			return checkShowAvailResp.getShowCost();
		return null;
	}

	public RentShowResp rentShow(ProviderShowID showID, String playerIPAddress, ShowFormat showFormat,
		ShowCost approvedCost, Payment payment)
	{
		DataRequestor dataRequestor = newDataRequestor(true);
		RentShowRqst rentShowRqst = RentShowRqst.newInstance(showID, playerIPAddress, showFormat,
			approvedCost, payment);
		RentShowResp rentShowResp = dataRequestor.rentShow(rentShowRqst);

		fStatusCode = dataRequestor.getStatusCode();
		return rentShowResp;
	}

	public WatchShowResp watchShow(ProviderShowID showID, String playerIPAddress)
	{
		DataRequestor dataRequestor = newDataRequestor(true);
		WatchShowRqst watchShowRqst = WatchShowRqst.newInstance(showID, playerIPAddress);
		WatchShowResp watchShowResp = dataRequestor.watchShow(watchShowRqst);

		fStatusCode = dataRequestor.getStatusCode();
		return watchShowResp;
	}

	public boolean releaseShow(ProviderShowID showID)
	{
		DataRequestor dataRequestor = newDataRequestor(true);
		ReleaseShowRqst releaseShowRqst = ReleaseShowRqst.newInstance(showID);
		dataRequestor.releaseShow(releaseShowRqst);

		fStatusCode = dataRequestor.getStatusCode();
		return ProviderStatusCode.sc_Success.equals(fStatusCode);
	}
}
