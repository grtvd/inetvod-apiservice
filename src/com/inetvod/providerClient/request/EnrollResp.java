/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;

public class EnrollResp implements Readable
{
	/* Contruction */
	public EnrollResp(DataReader reader)
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader)
	{
		// No Fields
	}
}
