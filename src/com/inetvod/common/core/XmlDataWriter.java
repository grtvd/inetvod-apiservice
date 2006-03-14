/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

public class XmlDataWriter extends DataWriter
{
	protected OutputStreamWriter fOutputStreamWriter;
	protected PrintWriter fPrintWriter;

	public XmlDataWriter(OutputStream stream) throws Exception
	{
		String characterEncoding = "UTF-8";
		fOutputStreamWriter = new OutputStreamWriter(stream, characterEncoding);
		writeStartDocument(characterEncoding);
	}

	public XmlDataWriter(PrintWriter printWriter, String characterEncoding) throws Exception
	{
		fPrintWriter = printWriter;
		writeStartDocument(characterEncoding);
	}

	public void close() throws Exception
	{
		if(fOutputStreamWriter != null)
			fOutputStreamWriter.close();
		fOutputStreamWriter = null;
	}

	public void flush() throws Exception
	{
		if(fOutputStreamWriter != null)
			fOutputStreamWriter.flush();
		if(fPrintWriter != null)
			fPrintWriter.flush();
	}

	protected void writeStartDocument(String encoding) throws Exception
	{
		writeString("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
	}

	protected void writeString(String data) throws IOException
	{
		if(fOutputStreamWriter != null)
			fOutputStreamWriter.write(data);
		if(fPrintWriter != null)
			fPrintWriter.print(data);
	}

	protected String encodeString(String data)
	{
		String newVal = data;

		if((data.indexOf("&") >= 0) || (data.indexOf("<") >= 0) || (data.indexOf("<") >= 0))
		{
			newVal = newVal.replaceAll("&", "&amp;");
			newVal = newVal.replaceAll("<", "&lt;");
			newVal = newVal.replaceAll(">", "&gt;");
		}

		return newVal;
	}

	/**
	 * Write an opending XML element tag
	 *
	 * @param name
	 */
	protected void writeStartElement(String name) throws Exception
	{
		writeString("<");
		writeString(name);
		writeString(">");
	}

	/**
	 * Write a closing XML element tag
	 *
	 * @param name
	 */
	protected void writeEndElement(String name) throws Exception
	{
		writeString("</");
		writeString(name);
		writeString(">");
	}

	/**
	 * Write an empty XML element
	 *
	 * @param name
	 * @throws Exception
	 */
//	protected void writeEmptyElement(String name) throws Exception
//	{
//		fOutputStreamWriter.write("<");
//		fOutputStreamWriter.write(name);
//		fOutputStreamWriter.write(" />");
//	}
	/**
	 * Write opening and closing XML element with value
	 *
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	protected void writeElement(String name, String value) throws Exception
	{
		if ((value == null) || (value.length() == 0))
			return;

		writeStartElement(name);
		writeString(encodeString(value));
		writeEndElement(name);
	}

	/**
	 * Write a Byte
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeByte(String fieldName, Byte data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write a Short
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeShort(String fieldName, Short data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write an Integer
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeInt(String fieldName, Integer data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write a Float
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeFloat(String fieldName, Float data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write a Double
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeDouble(String fieldName, Double data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write a String
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 * @throws Exception
	 */
	public void writeString(String fieldName, String data, int maxLength) throws Exception
	{
		int len = (data != null) ? data.length() : 0;
		if(len > maxLength)
			throw new Exception("invalid len(" + len + "), maxLength(" + maxLength + ")");

		writeElement(fieldName, data);
	}

	/**
	 * Write a date, no Time component
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeDate(String fieldName, Date data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, (new ISO8601DateFormat()).format(data));
	}

	/**
	 * Write a Date with a Time component
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeDateTime(String fieldName, Date data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, (new ISO8601DateTimeFormat()).format(data));
	}

	/**
	 * Write a Boolean
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeBoolean(String fieldName, Boolean data) throws Exception
	{
		if(data == null)
			return;

		writeElement(fieldName, data.toString());
	}

	/**
	 * Write a complex Object
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeObject(String fieldName, Writeable data) throws Exception
	{
		if(data == null)
			return;

		writeStartElement(fieldName);
		data.writeTo(this);
		writeEndElement(fieldName);
	}

	/**
	 * Write a list of complex Objects
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeList(String fieldName, List data) throws Exception
	{
		for(Object item : data)
			writeObject(fieldName, (Writeable)item);
	}

	/**
	 * Write a list of Strings (or non-complex items than can be converted to a sting)
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 */
	public void writeStringList(String fieldName, List data, int maxLength) throws Exception
	{
		for(Object item : data)
			writeString(fieldName, item.toString(), maxLength);
	}

	/**
	 * Write a DataID Object
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 * @throws Exception
	 */
	public void writeDataID(String fieldName, DataID data, int maxLength) throws Exception
	{
		writeString(fieldName, (data != null) ? data.toString() : null, maxLength);
	}

}
