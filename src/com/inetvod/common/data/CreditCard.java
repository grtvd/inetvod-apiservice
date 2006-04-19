/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;

public class CreditCard implements Readable, Writeable
{
	/* Constants */
	public static Constructor<CreditCard> CtorDataReader = DataReader.getCtor(CreditCard.class);

	private static final int NameOnCCMaxLength = 64;
	private static final int CCNumberMaxLength = 32;
	private static final int CCSICMaxLength = 16;
	private static final int ExpireDateMaxLength = 6;

	private static final int Century = 100;
	private static final int DefaultMillennia = 2000;

	/* Fields */
	private String fNameOnCC;
	private CreditCardType fCCType;
	private String fCCNumber;
	private String fCCSIC;
	private String fExpireDate;
	private Address fBillingAddress;

	/* Getters and Setters */
	public String getNameOnCC() { return fNameOnCC; }
	public void setNameOnCC(String nameOnCC) { fNameOnCC = nameOnCC; }

	public CreditCardType getCCType() { return fCCType; }
	public void setCCType(CreditCardType CCType) { fCCType = CCType; }

	public String getCCNumber() { return fCCNumber; }
	public void setCCNumber(String CCNumber) { fCCNumber = CCNumber; }

	public String getCCSIC() { return fCCSIC; }
	public void setCCSIC(String CCSIC) { fCCSIC = CCSIC; }

	public int getExpireDateMonth()
	{
		if((fExpireDate == null) || (fExpireDate.length() != 6))
			return 0;

		return Integer.parseInt(fExpireDate.substring(0, 2));
	}

	public int getExpireDateYear()
	{
		if((fExpireDate == null) || (fExpireDate.length() != 6))
			return 0;

		return Integer.parseInt(fExpireDate.substring(2, 6));
	}

	public void setExpireDate(int expireMonth, int expireYear)
	{
		if(expireYear < Century)
			expireYear += DefaultMillennia;
		fExpireDate = String.format("%02d%04d", expireMonth, expireYear);
	}

	public Address getBillingAddress() { return fBillingAddress; }
	public void setBillingAddress(Address billingAddress) { fBillingAddress = billingAddress; }

	/* Constuction */
	public CreditCard()
	{
	}

	public CreditCard(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fNameOnCC = reader.readString("NameOnCC", NameOnCCMaxLength);
		fCCType = CreditCardType.convertFromString(reader.readString("CCType", CreditCardType.MaxLength));
		fCCNumber = reader.readString("CCNumber", CCNumberMaxLength);
		fCCSIC = reader.readString("CCSIC", CCSICMaxLength);
		fExpireDate = reader.readString("ExpireDate", ExpireDateMaxLength);
		fBillingAddress = reader.readObject("BillingAddress", Address.CtorDataReader);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("NameOnCC", fNameOnCC, NameOnCCMaxLength);
		writer.writeString("CCType", CreditCardType.convertToString(fCCType), CreditCardType.MaxLength);
		writer.writeString("CCNumber", fCCNumber, CCNumberMaxLength);
		writer.writeString("CCSIC", fCCSIC, CCSICMaxLength);
		writer.writeString("ExpireDate", fExpireDate, ExpireDateMaxLength);
		writer.writeObject("BillingAddress", fBillingAddress);
	}
}
