package com.hbut.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriUtil {
	
	/*
	 * get the completely correct URI
	 * @param rawUri the raw URI
	 * @param replacer the string which will replace the chosen string in the raw
	 * 
	 * */
	public static String getRealUri(String rawUri, String replacer) {
		String realReplacer = "'" + replacer + "'";
		Pattern p = Pattern.compile("replace");
		Matcher m = p.matcher(rawUri);
		StringBuffer realUri = new StringBuffer();
		if (m.find()) {
			m.appendReplacement(realUri, realReplacer);
			m.appendTail(realUri);
		}
		return realUri.toString();
	}

	/*
	 * translate the Chinese characters into hexString using GBK encoding
	 */
	public static String gbkUriEncode(String text) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 0 && c <= 255) {
				result.append(c);
			} else {
				byte[] b = new byte[0];
				try {
					b = Character.toString(c).getBytes("GBK");
				} catch (Exception ex) {
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return result.toString();
	}
}
