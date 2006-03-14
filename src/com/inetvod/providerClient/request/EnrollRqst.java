/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import java.util.Date;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.data.Address;

public class EnrollRqst implements Writeable
{
	/* Constants */
	private static final int UserIDMaxLength = 64;
	private static final int PasswordMaxLength = 32;
	private static final int FirstNameMaxLength = 32;
	private static final int LastNameMaxLength = 32;
	private static final int EmailMaxLength = 64;

	/* Fields */
	private String fUserID;
	private String fPassword;
	private String fFirstName;
	private String fLastName;
	private String fEmail;

	private Date fBirthDate;
	private Address fShippingAddress;
	private Address fBillingAddress;

	/* Getters and Setters */
	public void setBirthDate(Date birthDate) { fBirthDate = birthDate; }
	public void setShippingAddress(Address shippingAddress) { fShippingAddress = shippingAddress; }
	public void setBillingAddress(Address billingAddress) { fBillingAddress = billingAddress; }

	/* Construction */
	public EnrollRqst(String userID, String password, String firstName, String lastName, String email)
	{
		fUserID = userID;
		fPassword = password;
		fFirstName = firstName;
		fLastName = lastName;
		fEmail = email;
	}

	public static EnrollRqst newInstance(String userID, String password, String firstName, String lastName, String email)
	{
		return new EnrollRqst(userID, password, firstName, lastName, email);
	}

	/* Implementation */
	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("UserID", fUserID, UserIDMaxLength);
		writer.writeString("Password", fPassword, PasswordMaxLength);
		writer.writeString("FirstName", fFirstName, FirstNameMaxLength);
		writer.writeString("LastName", fLastName, LastNameMaxLength);
		writer.writeString("Email", fEmail, EmailMaxLength);

		writer.writeDate("BirthDate", fBirthDate);
		writer.writeObject("ShippingAddress", fShippingAddress);
		writer.writeObject("BillingAddress", fBillingAddress);
	}
}
