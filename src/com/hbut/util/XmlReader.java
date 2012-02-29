package com.hbut.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
					if (tagName.equalsIgnoreCase(XmlTag.eStu)) {
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuID))
								pi.setID(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuName))
								pi.setName(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuPwd))
								pi.setPwd(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuCls))
								pi.setCls(xmlPullParser.getAttributeValue(i));
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
					if (tagName.equalsIgnoreCase(XmlTag.eStu)) {
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuID))
								pi.setID(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuName))
								pi.setName(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuPwd))
								pi.setPwd(xmlPullParser.getAttributeValue(i));
							else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuCls))
								pi.setCls(xmlPullParser.getAttributeValue(i));
						}
					} else if (tagName.equalsIgnoreCase(XmlTag.eSub)) {
						psbj = new PersonSbj();
					} else if (tagName.equalsIgnoreCase(XmlTag.eSubId)) {
						psbj.setSbjID(xmlPullParser.nextText());
					} else if (tagName.equalsIgnoreCase(XmlTag.eSubName)) {
						psbj.setSbjName(xmlPullParser.nextText());
					} else if (tagName.equalsIgnoreCase(XmlTag.eSubNote)) {
						psbj.setSbjNote(xmlPullParser.nextText());
					} else if (tagName.equalsIgnoreCase(XmlTag.eSubTimes)) {
						psbj.setSbjTimes(Integer.parseInt(xmlPullParser
								.nextText()));
					} else if (tagName.equalsIgnoreCase(XmlTag.eSubGrade)) {
						psbj.setSbjGrade(Integer.parseInt(xmlPullParser
								.nextText()));
					}

				}
					break;
				case XmlPullParser.END_TAG: {
					tagName = xmlPullParser.getName();
					if (tagName.equalsIgnoreCase(XmlTag.eSub))
						pSbjList.add(psbj);
					if (tagName.equalsIgnoreCase(XmlTag.eStu)) {
						Log.v("pull", "End");
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

}
