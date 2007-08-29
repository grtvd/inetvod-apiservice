/**
 * Copyright © 2006-2007 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient;

import com.inetvod.common.core.CompUtil;
import com.inetvod.common.data.ShowFormat;

public class ShowFormatExt extends ShowFormat
{
	/* Fields */
	private String fShowURL;
	private String fShowFormatMime;

	private Integer fHashCode;

	/* Getters and Setters */
	public String getShowURL() { return fShowURL; }
	public String getShowFormatMime() { return fShowFormatMime; }

	/* Constuction */
	public ShowFormatExt(String showURL, String showFormatMime)
	{
		super(null, null, null, null, null, null, null);
		fShowURL = showURL;
		fShowFormatMime = showFormatMime;
	}

	@SuppressWarnings({"NonFinalFieldReferenceInEquals"})
	@Override public boolean equals(Object obj)
	{
		if(!(obj instanceof ShowFormatExt))
			return false;
		ShowFormatExt showFormatExt = (ShowFormatExt)obj;

		return CompUtil.areEqual(fShowURL, showFormatExt.fShowURL)
			&& CompUtil.areEqual(fShowFormatMime, showFormatExt.fShowFormatMime);
	}

	@SuppressWarnings({ "NonFinalFieldReferencedInHashCode" })
	@Override
	public int hashCode()
	{
		if(fHashCode != null)
			return fHashCode;

		StringBuilder sb = new StringBuilder();

		if(fShowURL != null)
			sb.append(fShowURL);
		sb.append("|");
		if(fShowFormatMime != null)
			sb.append(fShowFormatMime);

		fHashCode = sb.toString().hashCode();
		return fHashCode;
	}
}
