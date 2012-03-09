package com.hbut.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XmlReader {

	public static PersonInf pIXmlParser(InputStream inStream) {
		PersonInf pi = null;
		boolean isEnd = false;
		// create parser
		XmlPullParserFactory pullFactory;
		try {
			pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = pullFactory.newPullParser();
			// begin parser
			xmlPullParser.setInput(inStream, "GBK");
			int eventType = xmlPullParser.getEventType();
			String tagName = null;
			while (eventType != XmlPullParser.END_DOCUMENT && isEnd == false) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT: {
					pi = new PersonInf();
				}
					break;
				case XmlPullParser.START_TAG: {
					tagName = xmlPullParser.getName();
					if (tagName.equalsIgnoreCase(XmlTag.stu)) {
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.id))
								pi.setID(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.name))
								pi.setName(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.pwd))
								pi.setPwd(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.cls))
								pi.setCls(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.count))
								pi.setSbjCount(Integer.parseInt(xmlPullParser
										.getAttributeValue(i)));
						}
					}
					isEnd = true;
				}
				}
				eventType = xmlPullParser.next();
			}

			return pi;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// parser xml data
	public static PersonCombine pGradeXmlParser(InputStream inStream) {
		// store person inf and person subjects inf
		PersonCombine pc = null;
		ArrayList<PersonSbj> pSbjList = null;
		PersonInf pi = null;
		try {
			// create parser
			XmlPullParserFactory pullFactory = XmlPullParserFactory
					.newInstance();
			XmlPullParser xmlPullParser = pullFactory.newPullParser();
			// begin parser
			xmlPullParser.setInput(inStream, "GBK");
			int eventType = xmlPullParser.getEventType();
			String tagName = null;
			PersonSbj psbj = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT: {
					pi = new PersonInf();
					pSbjList = new ArrayList<PersonSbj>();
					pc = new PersonCombine();
				}
					break;
				case XmlPullParser.START_TAG: {
					tagName = xmlPullParser.getName();
					if (tagName.equalsIgnoreCase(XmlTag.stu)) {
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.id))
								pi.setID(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.name))
								pi.setName(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.pwd))
								pi.setPwd(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.cls))
								pi.setCls(xmlPullParser.getAttributeValue(i));
						}
					} else if (tagName.equalsIgnoreCase(XmlTag.sbj)) {
						psbj = new PersonSbj();
					} else if (tagName.equalsIgnoreCase(XmlTag.id)) {
						psbj.setSbjID(xmlPullParser.nextText());
					} else if (tagName.equalsIgnoreCase(XmlTag.name)) {
						psbj.setSbjName(xmlPullParser.nextText());
					} else if (tagName.equalsIgnoreCase(XmlTag.note)) {
						psbj.setSbjNote(xmlPullParser.nextText());
					} else if (tagName.equalsIgnoreCase(XmlTag.times)) {
						psbj.setSbjTimes(Integer.parseInt(xmlPullParser
								.nextText()));
					} else if (tagName.equalsIgnoreCase(XmlTag.grade)) {
						psbj.setSbjGrade(Integer.parseInt(xmlPullParser
								.nextText()));
					}

				}
					break;
				case XmlPullParser.END_TAG: {
					tagName = xmlPullParser.getName();
					if (tagName.equalsIgnoreCase(XmlTag.sbj))
						pSbjList.add(psbj);
					if (tagName.equalsIgnoreCase(XmlTag.stu)) {
						pc.setPi(pi);
						pc.setpSbjList(pSbjList);
						return pc;
					}
				}
					break;
				}
				eventType = xmlPullParser.next();
			}

			return pc;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static Map<String, List<ClsStuSbj>> cGradeXmlParser(
			InputStream inStream) {
		String key = null;
		ClsStuSbj clsStuSbj = null;
		List<ClsStuSbj> clsStuSbjList = null;
		Map<String, List<ClsStuSbj>> clsMap = null;
		try {
			// create parser
			XmlPullParserFactory pullFactory = XmlPullParserFactory
					.newInstance();
			XmlPullParser xmlPullParser = pullFactory.newPullParser();
			// begin parser
			xmlPullParser.setInput(inStream, "GBK");
			int eventType = xmlPullParser.getEventType();
			String tagName = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					clsMap = new HashMap<String, List<ClsStuSbj>>();
					break;
				case XmlPullParser.START_TAG:
					tagName = xmlPullParser.getName();
					if (tagName.equalsIgnoreCase(XmlTag.sbj)) {
						key = xmlPullParser.getAttributeValue(0);
						clsStuSbjList = new ArrayList<ClsStuSbj>();
					} else if (tagName.equalsIgnoreCase(XmlTag.stu)) {
						clsStuSbj = new ClsStuSbj();
					} else if (tagName.equalsIgnoreCase(XmlTag.name)) {
						clsStuSbj.setStuName(xmlPullParser.nextText());
					} else if (tagName.equalsIgnoreCase(XmlTag.grade)) {
						clsStuSbj.setStuGrade(Integer.parseInt(xmlPullParser
								.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					tagName = xmlPullParser.getName();
					if (tagName.equalsIgnoreCase(XmlTag.sbj)) {
						clsMap.put(key, clsStuSbjList);
					} else if (tagName.equalsIgnoreCase(XmlTag.stu)) {
						clsStuSbjList.add(clsStuSbj);
					}
					break;
				}
				eventType = xmlPullParser.next();
			}
			return clsMap;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			Log.v("xmlRead", e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO: handle exception
			Log.v("xmlRead", e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}

	}

	public static VersionInf paserVersionXml(InputStream inStream) {
		VersionInf vi = null;
		XmlPullParserFactory pullFactory;
		List<String> eleList = new ArrayList<String>();
		String[] temp = null;
		boolean isEnd = false;
		try {
			pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = pullFactory.newPullParser();
			// begin parser
			xmlPullParser.setInput(inStream, "UTF-8");
			int eventType = xmlPullParser.getEventType();
			String tagName = null;
			while (eventType != XmlPullParser.END_DOCUMENT && isEnd == false) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					vi = new VersionInf();
					break;
				case XmlPullParser.START_TAG:
					tagName = xmlPullParser.getName();
					if (tagName.equalsIgnoreCase(XmlTag.version))
						vi.setVersion(xmlPullParser.nextText());
					else if (tagName.equalsIgnoreCase(XmlTag.year))
						vi.setYear(Integer.parseInt(xmlPullParser.nextText()));
					else if (tagName.equalsIgnoreCase(XmlTag.month))
						vi.setMonth(Integer.parseInt(xmlPullParser.nextText()));
					else if (tagName.equalsIgnoreCase(XmlTag.day))
						vi.setDay(Integer.parseInt(xmlPullParser.nextText()));
					else if (tagName.equalsIgnoreCase(XmlTag.element))
						eleList.add(xmlPullParser.nextText());
					else if (tagName.equalsIgnoreCase(XmlTag.allow))
						vi.setAllow(xmlPullParser.nextText().trim());
					break;
				case XmlPullParser.END_TAG:
					tagName = xmlPullParser.getName();	
					if (tagName.equalsIgnoreCase(XmlTag.app)) {
						temp = new String[eleList.size()];
						for (int i = 0; i < eleList.size(); i++) {
							temp[i] = eleList.get(i);
						}
						vi.setElement(temp);
						isEnd = true;
					}

					break;
				}
				eventType = xmlPullParser.next();
			}
			return vi;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
