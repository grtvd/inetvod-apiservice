/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient;

import com.inetvod.common.data.ShowFormat;

public class ShowFormatExt extends ShowFormat
{
	/* Fields */
	private String fShowURL;
	private String fShowFormatMime;

	/* Getters and Setters */
	public String getShowURL() { return fShowURL; }
	public void setShowURL(String showURL) { fShowURL = showURL; }

	public String getShowFormatMime() { return fShowFormatMime; }
	public void setShowFormatMime(String showFormatMime) { fShowFormatMime = showFormatMime; }
}
