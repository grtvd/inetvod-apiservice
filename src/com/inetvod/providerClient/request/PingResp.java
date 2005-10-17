/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;

public class PingResp implements Readable
{
	/* Contruction */
	public PingResp(DataReader reader)
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader)
	{
		// No Fields
	}
}
