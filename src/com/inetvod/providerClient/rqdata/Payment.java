/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.rqdata;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.data.CreditCard;

public class Payment implements Readable, Writeable
{
	/* Constants */
	public static Constructor<Payment> CtorDataReader = DataReader.getCtor(Payment.class);

	/* Fields */
	private PaymentType fPaymentType;
	private CreditCard fCreditCard;

	/* Construction */
	private Payment(PaymentType paymentType, CreditCard creditCard)
	{
		if(paymentType == null)
			throw new IllegalArgumentException("PaymentType is null");
		if(PaymentType.CreditCard.equals(paymentType) && (creditCard == null))
			throw new IllegalArgumentException("CreditCard is null");

		fPaymentType = paymentType;
		fCreditCard = creditCard;
	}

	public Payment(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	public static Payment newInstance(PaymentType paymentType, CreditCard creditCard)
	{
		return new Payment(paymentType, creditCard);
	}

	public void readFrom(DataReader reader) throws Exception
	{
		fPaymentType = PaymentType.convertFromString(reader.readString("PaymentType", PaymentType.MaxLength));
		fCreditCard = reader.readObject("CreditCard", CreditCard.CtorDataReader);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("PaymentType", PaymentType.convertToString(fPaymentType), PaymentType.MaxLength);
		writer.writeObject("CreditCard", fCreditCard);
	}
}
