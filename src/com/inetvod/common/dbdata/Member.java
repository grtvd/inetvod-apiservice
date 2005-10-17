/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.util.Date;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;

public class Member extends DatabaseObject
{
	/* Constants */
	public static final int NumFields = 11;
	public static final int FirstNameMaxLength = 32;
	public static final int LastNameMaxLength = 32;
	public static final int AddrStreetMaxLength = 64;
	public static final int CityMaxLength = 64;
	public static final int StateMaxLength = 64;
	public static final int PostalCodeMaxLength = 32;
	public static final int CountryMaxLength = 64;
	public static final int PhoneMaxLength = 32;

	/* Properties */
	protected MemberID fMemberID;
	protected String fFirstName;
	protected String fLastName;
	protected String fAddrStreet1;
	protected String fAddrStreet2;
	protected String fCity;
	protected String fState;
	protected String fPostalCode;
	protected String fCountry;
	protected String fPhone;
	protected Date fBirthDate;

	private static DatabaseAdaptor<Member, MemberList> fDatabaseAdaptor
		= new DatabaseAdaptor<Member, MemberList>(Member.class, MemberList.class, NumFields);
	public static DatabaseAdaptor<Member, MemberList> getDatabaseAdaptor() { return fDatabaseAdaptor; }

	/* Getters and Setters */
	public MemberID getMemberID() { return fMemberID; }

	public String getFirstName() { return fFirstName; }
	public void setFirstName(String firstName) { fFirstName = firstName; }

	public String getLastName() { return fLastName; }
	public void setLastName(String lastName) { fLastName = lastName; }

	public String getAddrStreet1() { return fAddrStreet1; }
	public void setAddrStreet1(String addrStreet1) { fAddrStreet1 = addrStreet1; }

	public String getAddrStreet2() { return fAddrStreet2; }
	public void setAddrStreet2(String addrStreet2) { fAddrStreet2 = addrStreet2; }

	public String getCity() { return fCity; }
	public void setCity(String city) { fCity = city; }

	public String getState() { return fState; }
	public void setState(String state) { fState = state; }

	public String getPostalCode() { return fPostalCode; }
	public void setPostalCode(String postalCode) { fPostalCode = postalCode; }

	public String getCountry() { return fCountry; }
	public void setCountry(String country) { fCountry = country; }

	public String getPhone() { return fPhone; }
	public void setPhone(String phone) { fPhone = phone; }

	public Date getBirthDate() { return fBirthDate; }
	public void setBirthDate(Date birthDate) { fBirthDate = birthDate; }

	/* Constuction Methods */
	protected Member()
	{
		super(true);
		fMemberID = MemberID.newInstance();
	}

	public Member(DataReader reader) throws Exception
	{
		super(reader);
		readFrom(reader);
	}

	public static Member newInstance()
	{
		return new Member();
	}

	protected static Member load(MemberID memberID, DataExists exists) throws Exception
	{
		return fDatabaseAdaptor.selectByKey(memberID, exists);
	}

	public static Member get(MemberID memberID) throws Exception
	{
		return load(memberID, DataExists.MustExist);
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fMemberID = reader.readDataID("MemberID", MemberID.MaxLength, MemberID.CtorString);
		fFirstName = reader.readString("FirstName", FirstNameMaxLength);
		fLastName = reader.readString("LastName", LastNameMaxLength);
		fAddrStreet1 = reader.readString("AddrStreet1", AddrStreetMaxLength);
		fAddrStreet2 = reader.readString("AddrStreet2", AddrStreetMaxLength);
		fCity = reader.readString("City", CityMaxLength);
		fState = reader.readString("State", StateMaxLength);
		fPostalCode = reader.readString("PostalCode", PostalCodeMaxLength);
		fCountry = reader.readString("Country", CountryMaxLength);
		fPhone = reader.readString("Phone", PhoneMaxLength);
		fBirthDate = reader.readDate("BirthDate");
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("MemberID", fMemberID, MemberID.MaxLength);
		writer.writeString("FirstName", fFirstName, FirstNameMaxLength);
		writer.writeString("LastName", fLastName, LastNameMaxLength);
		writer.writeString("AddrStreet1", fAddrStreet1, AddrStreetMaxLength);
		writer.writeString("AddrStreet2", fAddrStreet2, AddrStreetMaxLength);
		writer.writeString("City", fCity, CityMaxLength);
		writer.writeString("State", fState, StateMaxLength);
		writer.writeString("PostalCode", fPostalCode, PostalCodeMaxLength);
		writer.writeString("Country", fCountry, CountryMaxLength);
		writer.writeString("Phone", fPhone, PhoneMaxLength);
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
