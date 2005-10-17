/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.core.DataReader;

public abstract class DatabaseObject implements Readable, Writeable
{
	protected boolean fIsNewRecord;

	protected DatabaseObject(boolean isNewRecord)
	{
		fIsNewRecord = isNewRecord;
	}

	protected DatabaseObject(DataReader reader)
	{
		fIsNewRecord = false;
	}

	public boolean isNewRecord()
	{
		return fIsNewRecord;
	}

	public void setNewRecord(boolean newRecord)
	{
		fIsNewRecord = newRecord;
	}
}
