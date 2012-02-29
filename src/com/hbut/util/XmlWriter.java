package com.hbut.util;

import java.io.StringWriter;
import java.util.List;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;

public class XmlWriter {

	public static boolean hasPath(){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
			return true;
		else
			return false;
	}
	public static String writePGradeXml(List<PersonSbj> pList, PersonInf pi) {

		StringWriter xmlWriter = new StringWriter();
		String nmsp = "";
		try {
			// create xml parser
			XmlPullParserFactory pullFactory = XmlPullParserFactory
					.newInstance();
			XmlSerializer xmlSerializer = pullFactory.newSerializer();
			// bind
			xmlSerializer.setOutput(xmlWriter);
			// begin write
			xmlSerializer.startDocument("GBK", true);
			xmlSerializer.startTag(nmsp,XmlTag.eStu);
			xmlSerializer.attribute(nmsp, XmlTag.aStuID, pi.getID());
			xmlSerializer.attribute(nmsp, XmlTag.aStuName, pi.getName());
			xmlSerializer.attribute(nmsp, XmlTag.aStuPwd, pi.getPwd());
			xmlSerializer.attribute(nmsp, XmlTag.aStuCls, pi.getCls());

			for (PersonSbj n : pList) {
				// <subject>
				xmlSerializer.startTag(nmsp, XmlTag.eSub);
				// <id></id>
				xmlSerializer.startTag(nmsp, XmlTag.eSubId);
				xmlSerializer.text(n.getSbjID());
				xmlSerializer.endTag(nmsp, XmlTag.eSubId);
				// <name></name>
				xmlSerializer.startTag(nmsp, XmlTag.eSubName);
				xmlSerializer.text(n.getSbjName());
				xmlSerializer.endTag(nmsp, "name");
				// <grade></grade>
				xmlSerializer.startTag(nmsp, XmlTag.eSubGrade);
//				if (n.getSbjLevel() == null)
					xmlSerializer.text(Integer.toString(n.getSbjGrade()));
//				else
//					xmlSerializer.text("");
				xmlSerializer.endTag(nmsp, XmlTag.eSubGrade);
				// <note></note>
				xmlSerializer.startTag(nmsp, XmlTag.eSubNote);
				xmlSerializer.text(n.getSbjNote());
				xmlSerializer.endTag(nmsp, XmlTag.eSubNote);
				// <times></times>
				xmlSerializer.startTag(nmsp, XmlTag.eSubTimes);
				xmlSerializer.text(Integer.toString(n.getSbjTimes()));
				xmlSerializer.endTag(nmsp, XmlTag.eSubTimes);
				// <level></level>
//				xmlSerializer.startTag(nmsp, XmlTag.eSubLevel);
//				if (n.getSbjLevel() != null)
//					xmlSerializer.text(n.getSbjLevel());
//				else
//					xmlSerializer.text("");
//				xmlSerializer.endTag(nmsp, XmlTag.eSubLevel);
				// </subject>
				xmlSerializer.endTag(nmsp, XmlTag.eSub);
			}
			xmlSerializer.endTag(nmsp, XmlTag.eStu);
			xmlSerializer.endDocument();
			return xmlWriter.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}
