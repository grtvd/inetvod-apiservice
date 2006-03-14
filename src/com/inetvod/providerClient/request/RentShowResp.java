/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import java.util.Date;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.data.License;

public class RentShowResp implements Readable
{
	/* Fields */
	private Date fAvailableUntil;
	private License fLicense;

	/* Getters and Setters */
	public Date getAvailableUntil() { return fAvailableUntil; }
	public License getLicense() { return fLicense; }

	/* Construction */
	public RentShowResp(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fAvailableUntil = reader.readDateTime("AvailableUntil");
		fLicense = reader.readObject("License", License.CtorDataReader);
	}
}
