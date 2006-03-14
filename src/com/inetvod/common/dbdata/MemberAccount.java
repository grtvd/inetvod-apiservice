/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.util.Date;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.data.Address;
import com.inetvod.common.data.CreditCard;
import com.inetvod.common.data.MemberID;

public class MemberAccount extends DatabaseObject
{
	/* Fields */
	private MemberID fMemberID;
	private Address fHomeAddress;
	private CreditCard fCreditCard;
	private Date fBirthDate;

	private static DatabaseAdaptor<MemberAccount, MemberAccountList> fDatabaseAdaptor
		= new DatabaseAdaptor<MemberAccount, MemberAccountList>(MemberAccount.class, MemberAccountList.class);
	public static DatabaseAdaptor<MemberAccount, MemberAccountList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public MemberID getMemberID() { return fMemberID; }

	public Address getHomeAddress() { return fHomeAddress; }
	public void setHomeAddress(Address homeAddress) { fHomeAddress = homeAddress; }

	public CreditCard getCreditCard() { return fCreditCard; }
	public void setCreditCard(CreditCard creditCard) { fCreditCard = creditCard; }

	public Date getBirthDate() { return fBirthDate; }
	public void setBirthDate(Date birthDate) { fBirthDate = birthDate; }

	/* Constuction */
	private MemberAccount(MemberID memberID)
	{
		super(true);
		fMemberID = memberID;
	}

	public MemberAccount(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static MemberAccount newInstance(MemberID memberID)
	{
		return new MemberAccount(memberID);
	}

	private static MemberAccount load(MemberID memberID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(memberID, exists);
	}

	public static MemberAccount getCreate(MemberID memberID) throws Exception
	{
		MemberAccount memberAccount = load(memberID, DataExists.MayNotExist);

		if(memberAccount == null)
			memberAccount = newInstance(memberID);

		return memberAccount;
	}

	public static MemberAccount find(MemberID memberID) throws Exception
	{
		return load(memberID, DataExists.MayNotExist);
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fMemberID = reader.readDataID("MemberID", MemberID.MaxLength, MemberID.CtorString);
		fHomeAddress = reader.readObject("HomeAddress", Address.CtorDataReader);
		fCreditCard = reader.readObject("CreditCard", CreditCard.CtorDataReader);
		fBirthDate = reader.readDate("BirthDate");
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeObject("HomeAddress", fHomeAddress);
		writer.writeObject("CreditCard", fCreditCard);
		writer.writeDate("BirthDate", fBirthDate);
	}

	public void update() throws Exception
	{
		fDatabaseAdaptor.update(this);
	}

	public void delete() throws Exception
	{
		fDatabaseAdaptor.delete(fMemberID);
	}

	static public void delete(MemberID memberID) throws Exception
	{
		fDatabaseAdaptor.delete(memberID);
	}
}
