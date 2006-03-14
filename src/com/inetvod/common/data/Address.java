/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CountryID;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;

public class Address implements Readable, Writeable
{
	/* Constants */
	public static Constructor<Address> CtorDataReader = DataReader.getCtor(Address.class);

	private static final int AddrStreetMaxLength = 64;
	private static final int CityMaxLength = 64;
	private static final int StateMaxLength = 64;
	private static final int PostalCodeMaxLength = 32;
	private static final int PhoneMaxLength = 32;

	/* Fields */
	private String fAddrStreet1;
	private String fAddrStreet2;
	private String fCity;
	private String fState;
	private String fPostalCode;
	private CountryID fCountry;
	private String fPhone;

	/* Getters and Setters */
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

	public CountryID getCountry() { return fCountry; }
	public void setCountry(CountryID country) { fCountry = country; }

	public String getPhone() { return fPhone; }
	public void setPhone(String phone) { fPhone = phone; }

	/* Construction */
	public Address()
	{
	}

	public Address(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fAddrStreet1 = reader.readString("AddrStreet1", AddrStreetMaxLength);
		fAddrStreet2 = reader.readString("AddrStreet2", AddrStreetMaxLength);
		fCity = reader.readString("City", CityMaxLength);
		fState = reader.readString("State", StateMaxLength);
		fPostalCode = reader.readString("PostalCode", PostalCodeMaxLength);
		fCountry = reader.readDataID("Country", CountryID.MaxLength, CountryID.CtorString);
		fPhone = reader.readString("Phone", PhoneMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("AddrStreet1", fAddrStreet1, AddrStreetMaxLength);
		writer.writeString("AddrStreet2", fAddrStreet2, AddrStreetMaxLength);
		writer.writeString("City", fCity, CityMaxLength);
		writer.writeString("State", fState, StateMaxLength);
		writer.writeString("PostalCode", fPostalCode, PostalCodeMaxLength);
		writer.writeDataID("Country", fCountry, CountryID.MaxLength);
		writer.writeString("Phone", fPhone, PhoneMaxLength);
	}
}
