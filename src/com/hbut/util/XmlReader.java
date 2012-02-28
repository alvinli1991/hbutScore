package com.hbut.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XmlReader {


	public PersonCombine getPc() {
		return pc;
	}

	public void setPc(PersonCombine pc) {
		this.pc = pc;
	}

	// store person inf and person subjects inf
	 PersonCombine pc = null;
	 ArrayList<PersonSbj> pSbjList = null;
	 PersonInf pi = null;
	// parser xml data
	public PersonCombine pGradeXmlParser(InputStream inStream) {
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
				}
					break;
				case XmlPullParser.START_TAG: {
					tagName = xmlPullParser.getName();
					if (tagName.equalsIgnoreCase(XmlTag.eStu)) {
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuID))
								pi.setID(xmlPullParser.getAttributeValue(i));
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuName))
								pi.setName(xmlPullParser.getAttributeValue(i));
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuPwd))
								pi.setPwd(xmlPullParser.getAttributeValue(i));
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(XmlTag.aStuCls))
								pi.setCls(xmlPullParser.getAttributeValue(i));
						}
					}
					if(tagName.equalsIgnoreCase(XmlTag.eSub)){
						psbj = new PersonSbj();
					}
					if(tagName.equalsIgnoreCase(XmlTag.eSubId)){
						psbj.setSbjID(xmlPullParser.nextText());
					}
					if(tagName.equalsIgnoreCase(XmlTag.eSubName)){
						psbj.setSbjName(xmlPullParser.nextText());
					}
					if(tagName.equalsIgnoreCase(XmlTag.eSubNote)){
						psbj.setSbjNote(xmlPullParser.nextText());
					}
					if(tagName.equalsIgnoreCase(XmlTag.eSubTimes)){
						psbj.setSbjTimes(Integer.parseInt(xmlPullParser.nextText()));
						}
					if(tagName.equalsIgnoreCase(XmlTag.eSubGrade)){
						psbj.setSbjGrade(Integer.parseInt(xmlPullParser.nextText()));
					}
					
					
				}
					break;
				case XmlPullParser.END_TAG: {
					tagName = xmlPullParser.getName();
					if(tagName.equalsIgnoreCase(XmlTag.eSub))
						pSbjList.add(psbj);
				}
					break;
				case XmlPullParser.END_DOCUMENT:{
					pc.setPi(pi);
					pc.setpSbjList(pSbjList);
				}
				break;
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v("pull", "End");
		return pc;

	}

	
}
