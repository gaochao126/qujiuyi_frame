package com.jiuyi.frame.conf;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description xml 读取辅助类，用于简化jdom读取xml。
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class XmlReader {

	public static Document loadCfgFile(String cfgFile) {
		InputStream is;
		try {
			is = new FileInputStream(cfgFile);
		} catch (FileNotFoundException e1) {
			error("Can't open configuration file.", e1);
			throw new IllegalArgumentException("Parse xml config file error.");
		}
		return loadCfgByInputStream(is);
	}

	public static Document loadCfgString(String strXml) {
		return loadCfgByInputStream(new ByteArrayInputStream(strXml.getBytes()));
	}

	private static Document loadCfgByInputStream(InputStream is) {
		Document dom = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(is);
		} catch (IOException ioe) {
			error("Can't open configuration file.", ioe);
			throw new Error("Parse xml config file error.");
		} catch (Throwable t) {
			error("Parse xml config file error.", t);
			throw new Error("Parse xml config file error.");
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new Error("Close error.");
			}
		}
		return dom;
	}

	public static Boolean getNodeTextContentBoolean(Element element, String nodeName) {
		Element node = getChildElement(element, nodeName);
		return node == null ? null : Boolean.parseBoolean(node.getTextContent());
	}

	public static Long getNodeTextContentLong(Element element, String nodeName) {
		Element node = getChildElement(element, nodeName);
		return node == null ? null : Long.parseLong(node.getTextContent());
	}

	public static Integer getNodeTextContentInteger(Element element, String nodeName) {
		Element node = getChildElement(element, nodeName);
		return node == null ? null : Integer.parseInt(node.getTextContent());
	}

	public static String getNodeTextContent(Element element, String nodeName) {
		Element node = getChildElement(element, nodeName);
		return node == null ? null : node.getTextContent();
	}

	public static Boolean getNodeAttributeBoolean(Element elem, String nodeName, String attrName) {
		String attrValue = getNodeAttribute(elem, nodeName, attrName);
		return attrValue == null ? null : Boolean.parseBoolean(attrValue);
	}

	public static Long getNodeAttributeLong(Element elem, String nodeName, String attrName) {
		String attrValue = getNodeAttribute(elem, nodeName, attrName);
		return attrValue == null ? null : Long.parseLong(attrValue);
	}

	public static Integer getNodeAttributeInteger(Element elem, String nodeName, String attrName) {
		String attrValue = getNodeAttribute(elem, nodeName, attrName);
		return attrValue == null ? null : Integer.parseInt(attrValue);
	}

	public static String getNodeAttribute(Element elem, String nodeName, String attrName) {
		Element ele = getChildElement(elem, nodeName);
		if (ele == null) {
			return null;
		}
		return ele.getAttribute(attrName);
	}

	public static Element getChildElement(Element elem, String nodeName) {
		NodeList nl = elem.getChildNodes();
		for (int index = 0; index < nl.getLength(); index++) {
			Node node = nl.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(nodeName)) {
				return (Element) node;
			}
		}
		return null;
	}

	public static List<Element> getChildElements(Element elem) {
		NodeList nl = elem.getChildNodes();
		List<Element> eleList = new LinkedList<Element>();
		for (int index = 0; index < nl.getLength(); index++) {
			Node node = nl.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				eleList.add((Element) node);
			}
		}
		return eleList;
	}

	public static List<Element> getChildElements(Element elem, String nodeName) {
		NodeList nl = elem.getChildNodes();
		List<Element> eleList = new LinkedList<Element>();
		for (int index = 0; index < nl.getLength(); index++) {
			Node node = nl.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(nodeName)) {
				eleList.add((Element) node);
			}
		}
		return eleList;
	}

	private static void error(String msg, Throwable t) {
		System.err.println(msg);
		t.printStackTrace(System.err);
	}
}
