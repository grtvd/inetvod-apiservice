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

public class Authenticate implements Readable, Writeable
{
	/* Constants */
	public static final Constructor<Authenticate> CtorDataReader = DataReader.getCtor(Authenticate.class);

	private static final int AdminUserIDMaxLength = 64;
	private static final int AdminPasswordMaxLength = 16;
	private static final int MemberUserIDMaxLength = 64;
	private static final int MemberPasswordMaxLength = 16;

	/* Fields */
	private String fAdminUserID;
	private String fAdminPassword;
	private String fMemberUserID;
	private String fMemberPassword;

	/* Construction */
	private Authenticate(String adminUserID, String adminPassword, String memberUserID, String memberPassword)
	{
		fAdminUserID = adminUserID;
		fAdminPassword = adminPassword;
		fMemberUserID = memberUserID;
		fMemberPassword = memberPassword;
	}

	public Authenticate(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	public static Authenticate newInstance(String adminUserID, String adminPassword, String memberUserID, String memberPassword)
	{
		return new Authenticate(adminUserID, adminPassword, memberUserID, memberPassword);
	}

	/* Getters and Setters */
	public void setMemberUser(String memberUserID, String memberPassword)
	{
		fMemberUserID = memberUserID;
		fMemberPassword = memberPassword;
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fAdminUserID = reader.readString("AdminUserID", AdminUserIDMaxLength);
		fAdminPassword = reader.readString("AdminPassword", AdminPasswordMaxLength);
		fMemberUserID = reader.readString("MemberUserID", MemberUserIDMaxLength);
		fMemberPassword = reader.readString("MemberPassword", MemberPasswordMaxLength);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("AdminUserID", fAdminUserID, AdminUserIDMaxLength);
		writer.writeString("AdminPassword", fAdminPassword, AdminPasswordMaxLength);
		writer.writeString("MemberUserID", fMemberUserID, MemberUserIDMaxLength);
		writer.writeString("MemberPassword", fMemberPassword, MemberPasswordMaxLength);
	}
}
