package com.hbut.util;

import java.io.File;
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
//	
//	public static String getCGradePath(){
//		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
//			File cacheDir = Environment.getExternalStorageDirectory();
//			String path = cacheDir.getPath()+"/hbutscore"+"/class";
//			return path;
//		}
//		else
//			return null;
//	}
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
			xmlSerializer.startTag(nmsp, "student");
			xmlSerializer.attribute(nmsp, "id", pi.getID());
			xmlSerializer.attribute(nmsp, "name", pi.getName());
			xmlSerializer.attribute(nmsp, "pwd", pi.getPwd());
			xmlSerializer.attribute(nmsp, "cls", pi.getCls());

			for (PersonSbj n : pList) {
				// <subject>
				xmlSerializer.startTag(nmsp, "subject");
				// <id></id>
				xmlSerializer.startTag(nmsp, "id");
				xmlSerializer.text(n.getSbjID());
				xmlSerializer.endTag(nmsp, "id");
				// <name></name>
				xmlSerializer.startTag(nmsp, "name");
				xmlSerializer.text(n.getSbjName());
				xmlSerializer.endTag(nmsp, "name");
				// <grade></grade>
				xmlSerializer.startTag(nmsp, "grade");
				if (n.getSbjLevel() == null)
					xmlSerializer.text(Integer.toString(n.getSbjGrade()));
				else
					xmlSerializer.text("");
				xmlSerializer.endTag(nmsp, "grade");
				// <note></note>
				xmlSerializer.startTag(nmsp, "note");
				xmlSerializer.text(n.getSbjNote());
				xmlSerializer.endTag(nmsp, "note");
				// <times></times>
				xmlSerializer.startTag(nmsp, "times");
				xmlSerializer.text(Integer.toString(n.getSbjTimes()));
				xmlSerializer.endTag(nmsp, "times");
				// <level></level>
				xmlSerializer.startTag(nmsp, "level");
				if (n.getSbjLevel() != null)
					xmlSerializer.text(n.getSbjLevel());
				else
					xmlSerializer.text("");
				xmlSerializer.endTag(nmsp, "level");
				// </subject>
				xmlSerializer.endTag(nmsp, "subject");
			}
			xmlSerializer.endTag(nmsp, "student");
			xmlSerializer.endDocument();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return xmlWriter.toString();
	}
}
