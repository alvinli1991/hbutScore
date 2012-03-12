package com.hbut.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.util.Log;

public class XmlWriter {

	/*
	 * check if the SD card is available
	 * */
	public static boolean hasPath() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			return true;
		else
			return false;
	}

	/*
	 * Create a  XML file with yes permission and the first date of this year
	 * when user open the APP the first time .
	 * this process can give  user  access to the app's functionality
	 * and generate the first permission check after finishing downloading 
	 * the class grades.
	 * @param versionID the version of this app
	 * */
	public static String writeFirstVersionXml(String versionID) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = 0;
		int day = 1;
		StringWriter xmlWriter = new StringWriter();
		String nmsp = "";
		try {
			// create xml parser
			XmlPullParserFactory pullFactory = XmlPullParserFactory
					.newInstance();
			XmlSerializer xmlSerializer = pullFactory.newSerializer();
			// bind
			xmlSerializer.setOutput(xmlWriter);
			// <>
			xmlSerializer.startDocument("UTF-8", true);
			// <app>
			xmlSerializer.startTag(nmsp, XmlTag.app);
			// <version></version>
			xmlSerializer.startTag(nmsp, XmlTag.version);
			xmlSerializer.text(versionID);
			xmlSerializer.endTag(nmsp, XmlTag.version);
			// <allow></allow>
			xmlSerializer.startTag(nmsp, XmlTag.allow);
			xmlSerializer.text("true");
			xmlSerializer.endTag(nmsp, XmlTag.allow);
			// <year></year>
			xmlSerializer.startTag(nmsp, XmlTag.year);
			xmlSerializer.text(Integer.toString(year));
			xmlSerializer.endTag(nmsp, XmlTag.year);
			// <month></month>
			xmlSerializer.startTag(nmsp, XmlTag.month);
			xmlSerializer.text(Integer.toString(month));
			xmlSerializer.endTag(nmsp, XmlTag.month);
			// <day></day>
			xmlSerializer.startTag(nmsp, XmlTag.day);
			xmlSerializer.text(Integer.toString(day));
			xmlSerializer.endTag(nmsp, XmlTag.day);
			// </app>
			xmlSerializer.endTag(nmsp, XmlTag.app);
			//<>
			xmlSerializer.endDocument();
			return xmlWriter.toString();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/*
	 * create a XML file after downloading new version.xml file from server
	 * @param versionID the version of the APP
	 * @param element the information download from update server
	 * @param allow the permission download from update server
	 * */
	
	public static String writeVersionXml(String versionID, String[] element,
			String allow) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		StringWriter xmlWriter = new StringWriter();
		String nmsp = "";
		try {
			// create xml parser
			XmlPullParserFactory pullFactory = XmlPullParserFactory
					.newInstance();
			XmlSerializer xmlSerializer = pullFactory.newSerializer();
			// bind
			xmlSerializer.setOutput(xmlWriter);
			// <>
			xmlSerializer.startDocument("UTF-8", true);
			// <app>
			xmlSerializer.startTag(nmsp, XmlTag.app);
			// <version></version>
			xmlSerializer.startTag(nmsp, XmlTag.version);
			xmlSerializer.text(versionID);
			xmlSerializer.endTag(nmsp, XmlTag.version);
			// <allow></allow>
			xmlSerializer.startTag(nmsp, XmlTag.allow);
			xmlSerializer.text(allow);
			xmlSerializer.endTag(nmsp, XmlTag.allow);
			// <year></year>
			xmlSerializer.startTag(nmsp, XmlTag.year);
			xmlSerializer.text(Integer.toString(year));
			xmlSerializer.endTag(nmsp, XmlTag.year);
			// <month></month>
			xmlSerializer.startTag(nmsp, XmlTag.month);
			xmlSerializer.text(Integer.toString(month));
			xmlSerializer.endTag(nmsp, XmlTag.month);
			// <day></day>
			xmlSerializer.startTag(nmsp, XmlTag.day);
			xmlSerializer.text(Integer.toString(day));
			xmlSerializer.endTag(nmsp, XmlTag.day);
			// <note>
			xmlSerializer.startTag(nmsp, XmlTag.note);
			//<element></element>
			for (int i = 0; i < element.length; i++) {
				xmlSerializer.startTag(nmsp, XmlTag.element);
				xmlSerializer.text(element[i]);
				xmlSerializer.endTag(nmsp, XmlTag.element);
			}
			//</note>
			xmlSerializer.endTag(nmsp, XmlTag.note);
			//</app>
			xmlSerializer.endTag(nmsp, XmlTag.app);
			// <>
			xmlSerializer.endDocument();
			Log.v("xmlWriter", xmlWriter.toString());
			return xmlWriter.toString();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	/*
	 * 
	 * Create a xml containing a particular student's grades 
	 * @param pList the list contains a student's whole course's grade
	 * @param pi the student's information
	 * */
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
			// <>
			xmlSerializer.startDocument("GBK", true);
			// <student id="" name="" pwd="" cls="" count=""></student>
			xmlSerializer.startTag(nmsp, XmlTag.stu);
			xmlSerializer.attribute(nmsp, XmlTag.id, pi.getID());
			xmlSerializer.attribute(nmsp, XmlTag.name, pi.getName());
			xmlSerializer.attribute(nmsp, XmlTag.pwd, pi.getPwd());
			xmlSerializer.attribute(nmsp, XmlTag.cls, pi.getCls());
			xmlSerializer.attribute(nmsp, XmlTag.count,
					Integer.toString(pi.getSbjCount()));
			for (PersonSbj n : pList) {
				// <subject>
				xmlSerializer.startTag(nmsp, XmlTag.sbj);
				// <id></id>
				xmlSerializer.startTag(nmsp, XmlTag.id);
				xmlSerializer.text(n.getSbjID());
				xmlSerializer.endTag(nmsp, XmlTag.id);
				// <name></name>
				xmlSerializer.startTag(nmsp, XmlTag.name);
				xmlSerializer.text(n.getSbjName());
				xmlSerializer.endTag(nmsp, "name");
				// <grade></grade>
				xmlSerializer.startTag(nmsp, XmlTag.grade);
				// if (n.getSbjLevel() == null)
				xmlSerializer.text(Integer.toString(n.getSbjGrade()));
				// else
				// xmlSerializer.text("");
				xmlSerializer.endTag(nmsp, XmlTag.grade);
				// <note></note>
				xmlSerializer.startTag(nmsp, XmlTag.note);
				xmlSerializer.text(n.getSbjNote());
				xmlSerializer.endTag(nmsp, XmlTag.note);
				// <times></times>
				xmlSerializer.startTag(nmsp, XmlTag.times);
				xmlSerializer.text(Integer.toString(n.getSbjTimes()));
				xmlSerializer.endTag(nmsp, XmlTag.times);
				xmlSerializer.endTag(nmsp, XmlTag.sbj);
			}
			xmlSerializer.endTag(nmsp, XmlTag.stu);
			xmlSerializer.endDocument();
			return xmlWriter.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * create a xml containing all grades information of his class
	 * 
	 * @param clsMap string is the id of a course ,list contains the student and his grade
	 * @param clsId the id of the class
	 * */
	public static String writeCGradeXml(Map<String, List<ClsStuSbj>> clsMap,
			String clsId) {
		StringWriter xmlWriter = new StringWriter();
		String nmsp = "";
		try {
			XmlPullParserFactory pullFactory = XmlPullParserFactory
					.newInstance();
			XmlSerializer xmlSlr = pullFactory.newSerializer();
			xmlSlr.setOutput(xmlWriter);
			// begin writer
			xmlSlr.startDocument("GBK", true);
			// <cls id ="">
			xmlSlr.startTag(nmsp, XmlTag.cls);
			xmlSlr.attribute(nmsp, XmlTag.id, clsId);

			Iterator<?> it = clsMap.entrySet().iterator();
			String key = null;
			List<ClsStuSbj> list = null;
			Map.Entry<String, List<ClsStuSbj>> entry = null;
			while (it.hasNext()) {
				entry = (Entry<String, List<ClsStuSbj>>) it.next();
				key = entry.getKey();
				// <subject id = "">
				xmlSlr.startTag(nmsp, XmlTag.sbj);
				xmlSlr.attribute(nmsp, XmlTag.id, key);

				list = entry.getValue();
				for (ClsStuSbj n : list) {
					// <student>
					xmlSlr.startTag(nmsp, XmlTag.stu);
					// <name></name>
					xmlSlr.startTag(nmsp, XmlTag.name);
					xmlSlr.text(n.getStuName());
					xmlSlr.endTag(nmsp, XmlTag.name);
					// <grade></grade>
					xmlSlr.startTag(nmsp, XmlTag.grade);
					xmlSlr.text(Integer.toString(n.getStuGrade()));
					xmlSlr.endTag(nmsp, XmlTag.grade);
					// </student>
					xmlSlr.endTag(nmsp, XmlTag.stu);
				}
				// </subject>
				xmlSlr.endTag(nmsp, XmlTag.sbj);
			}
			// </cls>
			xmlSlr.endTag(nmsp, XmlTag.cls);
			xmlSlr.endDocument();
			return xmlWriter.toString();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

}
