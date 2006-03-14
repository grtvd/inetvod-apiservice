/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDataReader extends DataReader
{
	private static class ReaderItem
	{
		@SuppressWarnings({"FieldCanBeLocal"})
		private XmlClassMeta fXmlClassMeta;

		private Node fNode;
		private Set<String> fAttributeList;

		public Node getNode() { return fNode; }

		public boolean isAttribute(String name)
		{
			if(fAttributeList == null)
				return false;
			return fAttributeList.contains(name);
		}

		public ReaderItem(Node node, Class<?> itemClass)
		{
			fNode = node;

			if(itemClass == null)
				return;

			fXmlClassMeta = itemClass.getAnnotation(XmlClassMeta.class);
			if(fXmlClassMeta != null)
			{
				fAttributeList = new HashSet<String>();
				for(String attribute : fXmlClassMeta.attributeList())
					fAttributeList.add(attribute);
			}
		}
	}

	private ArrayList<ReaderItem> fCurItemList;

	public XmlDataReader(InputStream stream) throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//dbf.setIgnoringElementContentWhitespace(true);
		//dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(stream);
		fCurItemList = new ArrayList<ReaderItem>();
		fCurItemList.add(new ReaderItem(document, null));
	}

	private ReaderItem getCurItem() throws Exception
	{
		if(fCurItemList.size() == 0)
			throw new Exception("No current node");

		return fCurItemList.get(fCurItemList.size() - 1);
	}

	private Node findAttribute(String fieldName) throws Exception
	{
		NamedNodeMap namedNodeMap = getCurItem().getNode().getAttributes();
		if(namedNodeMap == null)
			return null;

		return namedNodeMap.getNamedItem(fieldName);
	}

	private Node findChildNode(String fieldName) throws Exception
	{
		NodeList nodeList = getCurItem().getNode().getChildNodes();
		Node node;

		for(int i = 0; i < nodeList.getLength(); i++)
		{
			node = nodeList.item(i);
			if(node.getNodeName().equals(fieldName))
				return node;
		}

		return null;
	}

	private ArrayList<Node> findChildNodes(String fieldName) throws Exception
	{
		NodeList nodeList = getCurItem().getNode().getChildNodes();
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

	private String getNodeText(Node node)
	{
		NodeList nodeList = node.getChildNodes();
		Node childNode;
		short nodeType;
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < nodeList.getLength(); i++)
		{
			childNode = nodeList.item(i);
			nodeType = childNode.getNodeType();

			if((nodeType == Node.TEXT_NODE) || (nodeType == Node.CDATA_SECTION_NODE))
				sb.append(childNode.getNodeValue());
		}

		if(sb.length() > 0)
			return sb.toString();
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
	private String readString(String fieldName) throws Exception
	{
		Node node;

		if(fieldName != null)
		{
			if(getCurItem().isAttribute(fieldName))
				node = findAttribute(fieldName);
			else
				node = findChildNode(fieldName);
			if(node == null)
				return null;
		}
		else
			node = getCurItem().getNode();

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

		fCurItemList.add(new ReaderItem(node, ctorDataReader.getDeclaringClass()));
		T readable = ctorDataReader.newInstance(this);
		fCurItemList.remove(fCurItemList.size() - 1);

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
		L list = listCtor.newInstance();

		ArrayList<Node> nodes = findChildNodes(fieldName);
		if(nodes.size() == 0)
			return list;

		for(Node node: nodes)
		{
			fCurItemList.add(new ReaderItem(node, itemCtorDataReader.getDeclaringClass()));
			T item = itemCtorDataReader.newInstance(this);
			list.add(item);
			fCurItemList.remove(fCurItemList.size() - 1);
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
		L list = listCtor.newInstance();

		ArrayList<Node> nodes = findChildNodes(fieldName);
		if(nodes.size() == 0)
			return list;

		for(Node node: nodes)
		{
			T item = itemCtorString.newInstance(getNodeText(node));
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

		return ctorString.newInstance(data);
	}
}
