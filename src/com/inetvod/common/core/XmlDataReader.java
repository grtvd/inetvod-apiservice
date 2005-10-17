/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.core;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDataReader extends DataReader
{
	protected Document fDocument;
	protected ArrayList<Node> fCurNodeList;

	public XmlDataReader(InputStream stream) throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//dbf.setIgnoringElementContentWhitespace(true);
		//dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		fDocument = db.parse(stream);
		fCurNodeList = new ArrayList<Node>();
		fCurNodeList.add(fDocument);
	}

	protected Node findChildNode(String fieldName) throws Exception
	{
		if(fCurNodeList.size() == 0)
			throw new Exception("No current node");

		NodeList nodeList = fCurNodeList.get(fCurNodeList.size() - 1).getChildNodes();
		Node node;

		for(int i = 0; i < nodeList.getLength(); i++)
		{
			node = nodeList.item(i);
			if(node.getNodeName().equals(fieldName))
				return node;
		}

		return null;
	}

	protected ArrayList<Node> findChildNodes(String fieldName) throws Exception
	{
		if(fCurNodeList.size() == 0)
			throw new Exception("No current node");

		NodeList nodeList = fCurNodeList.get(fCurNodeList.size() - 1).getChildNodes();
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node node;

		for(int i = 0; i < nodeList.getLength(); i++)
		{
			node = nodeList.item(i);
			if(node.getNodeName().equals(fieldName))
				nodes.add(node);
		}

		return nodes;
	}

	protected String getNodeText(Node node)
	{
		NodeList nodeList = node.getChildNodes();
		Node childNode;

		for(int i = 0; i < nodeList.getLength(); i++)
		{
			childNode = nodeList.item(i);
			if(childNode.getNodeType() == Node.TEXT_NODE)
				return childNode.getNodeValue();
		}

		return null;
	}
	/**
	 * Read a Byte.
	 * @param fieldName
	 * @return may return null
	 */
	public Byte readByte(String fieldName) throws Exception
	{
		String data = readString(fieldName);

		if(data == null)
			return null;

		return Byte.decode(data);
	}

	/**
	 * Read a Short.
	 * @param fieldName
	 * @return may return null
	 */
	public Short readShort(String fieldName) throws Exception
	{
		String data = readString(fieldName);

		if(data == null)
			return null;

		return Short.decode(data);
	}

	/**
	 * Read a Integer.
	 * @param fieldName
	 * @return may return null
	 */
	public Integer readInt(String fieldName) throws Exception
	{
		String data = readString(fieldName);

		if(data == null)
			return null;

		return Integer.decode(data);
	}

	/**
	 * Read a Float.
	 * @param fieldName
	 * @return may return null
	 */
	public Float readFloat(String fieldName) throws Exception
	{
		String data = readString(fieldName);

		if(data == null)
			return null;

		return Float.valueOf(data);
	}

	/**
	 * Read a Double.
	 * @param fieldName
	 * @return may return null
	 */
	public Double readDouble(String fieldName) throws Exception
	{
		String data = readString(fieldName);

		if(data == null)
			return null;

		return Double.valueOf(data);
	}

	/**
	 * Internal read a String.
	 * @param fieldName
	 * @return may return null
	 */
	protected String readString(String fieldName) throws Exception
	{
		Node node = findChildNode(fieldName);
		if(node == null)
			return null;

		String data = getNodeText(node);
		if (data == null)
			return null;

		if(data.length() == 0)
			return null;

		return data;
	}

	/**
	 * Read a String.
	 * @param fieldName
	 * @param maxLength
	 * @return may return null
	 */
	public String readString(String fieldName, int maxLength) throws Exception
	{
		String data = readString(fieldName);
		if(data == null)
			return null;
		int len = data.length();

		if(len > maxLength)
			throw new Exception("invalid len(" + len + "), maxLength(" + maxLength + ")");

		return data;
	}

	/**
	 * Read a Date, no Time component.
	 * @param fieldName
	 * @return may return null
	 */
	public Date readDate(String fieldName) throws Exception
	{
		String data = readString(fieldName);

		if(data == null)
			return null;

		return (new ISO8601DateFormat()).parse(data);
	}

	/**
	 * Read a Date with a Time compnent.
	 * @param fieldName
	 * @return may return null
	 */
	public Date readDateTime(String fieldName) throws Exception
	{
		String data = readString(fieldName);

		if(data == null)
			return null;

		return (new ISO8601DateTimeFormat()).parse(data);
	}

	/**
	 * Read a Boolean.
	 * @param fieldName
	 * @return may return null
	 */
	public Boolean readBoolean(String fieldName) throws Exception
	{
		String data = readString(fieldName);

		if(data == null)
			return null;

		return Boolean.parseBoolean(data);
	}

	/**
	 * Read an Object.
	 * @param fieldName
	 * @param ctorDataReader
	 * @return may return null
	 */
	public <T extends Readable> T readObject(String fieldName, Constructor<T> ctorDataReader) throws Exception
	{
		Node node = findChildNode(fieldName);
		if(node == null)
			return null;

		fCurNodeList.add(node);
		T readable = ctorDataReader.newInstance(new Object[] { this });
		fCurNodeList.remove(fCurNodeList.size() - 1);

		return readable;
	}

	/**
	 * Read a list of complex Objects.
	 * @param fieldName
	 * @param listCtor
	 * @param itemCtorDataReader
	 * @return will never return null, may return an empty list
	 */
	public <T, L extends List<T>> L readList(String fieldName, Constructor<L> listCtor, Constructor<T> itemCtorDataReader) throws Exception
	{
		L list = listCtor.newInstance(new Object[] {});

		ArrayList<Node> nodes = findChildNodes(fieldName);
		if(nodes.size() == 0)
			return list;

		for(Node node: nodes)
		{
			fCurNodeList.add(node);
			T item = itemCtorDataReader.newInstance(new Object[] { this });
			list.add(item);
			fCurNodeList.remove(fCurNodeList.size() - 1);
		}

		return list;
	}

	/**
	 * Read a list of Strings (or non-complex items than can be constructed from a sting).
	 * @param fieldName
	 * @param maxLength
	 * @param listCtor
	 * @param itemCtorString
	 * @return will never return null, may return an empty list
	 */
	public <T, L extends List<T>> L readStringList(String fieldName, int maxLength, Constructor<L> listCtor,
		Constructor<T> itemCtorString) throws Exception
	{
		L list = listCtor.newInstance(new Object[] {});

		ArrayList<Node> nodes = findChildNodes(fieldName);
		if(nodes.size() == 0)
			return list;

		for(Node node: nodes)
		{
			T item = itemCtorString.newInstance(new Object[] { getNodeText(node) });
			list.add(item);
		}

		return list;
	}

	/**
	 * Read a DataID object.
	 * @param fieldName
	 * @param maxLength
	 * @param ctorString
	 * @return may return null
	 */
	public <T extends DataID> T readDataID(String fieldName, int maxLength, Constructor<T> ctorString) throws Exception
	{
		String data = readString(fieldName, maxLength);

		if (data == null)
			return null;

		return ctorString.newInstance(new Object[] { data });
	}
}
