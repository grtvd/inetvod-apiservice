/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;

public class Rss20 implements Readable
{
	/* Constants */
	public static final Constructor<Rss20> CtorDataReader = DataReader.getCtor(Rss20.class);

	/* Fields */
	private Channel fChannel;

	/* Getters and Setters */
	public Channel getChannel() { return fChannel; }

	/* Construction */
	public Rss20(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fChannel = reader.readObject("channel", Channel.CtorDataReader);
	}

//	public void writeTo(DataWriter writer) throws Exception
//	{
//		writer.writeObject("channel", fChannel);
//	}
}
