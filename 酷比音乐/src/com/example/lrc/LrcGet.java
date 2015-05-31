package com.example.lrc;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * @author wgq
 */
public class LrcGet {
//	public static void main(String[] args) throws Exception {
//
//		System.out.println(query("你把爱情给了谁", "王强"));
//
//	}

	public static String query(String title, String artist) {
		String URL = "http://ttlrcct.qianqian.com/dll/lyricsvr.dll?sh?Artist={ar}&Title={ti}&Flags=0";
		URL = URL.replace("{ar}", encode(artist))
		.replace("{ti}", encode(title));
		String result = "";
		int j = 0;

		try {

			result = HttpGet.get(URL);

			Document doc = new SAXReader().read(new StringReader(result));

			org.dom4j.Element root = doc.getRootElement();

			for (Iterator<?> i = root.elementIterator("lrc"); i.hasNext();) {

				j++;

				org.dom4j.Element e = (org.dom4j.Element) i.next();

				result = e.attributeValue("id");

				artist = e.attributeValue("artist");

				title = e.attributeValue("title");

				break;

			}

		} catch (IOException e1) {

			e1.printStackTrace();

			return null;

		} catch (DocumentException e1) {

			e1.printStackTrace();

			return null;

		}

		if (j == 0) {

			return null;
		}

		URL = "http://ttlrcct2.qianqian.com/dll/lyricsvr.dll?dl?Id={id}&Code={code}";

		try {

			URL = URL.replace("{id}", result).replace("{code}",

			verifyCode(artist, title, Integer.parseInt(result, 10)));

			return HttpGet.get(URL);

		} catch (NumberFormatException e) {

			e.printStackTrace();

			return null;

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

			return null;

		} catch (IOException e) {

			e.printStackTrace();

			return null;

		}
	}

	/**
	 * 后期文档清楚
	 * 
	 * @param artist
	 * @param title
	 * @param lrcId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String verifyCode(String artist, String title, int lrcId)
			throws UnsupportedEncodingException {

		byte[] bytes = (artist + title).getBytes("UTF-8");//

		int[] song = new int[bytes.length];

		for (int i = 0; i < bytes.length; i++) {

			song[i] = bytes[i] & 0xff;

		}

		int intVal1 = 0, intVal2 = 0, intVal3 = 0;

		intVal1 = (lrcId & 0xFF00) >> 8;

		if ((lrcId & 0xFF0000) == 0) {

			intVal3 = 0xFF & ~intVal1;

		} else {

			intVal3 = 0xFF & ((lrcId & 0x00FF0000) >> 16);

		}

		intVal3 = intVal3 | ((0xFF & lrcId) << 8);

		intVal3 = intVal3 << 8;

		intVal3 = intVal3 | (0xFF & intVal1);

		intVal3 = intVal3 << 8;

		if ((lrcId & 0xFF000000) == 0) {

			intVal3 = intVal3 | (0xFF & (~lrcId));

		} else {

			intVal3 = intVal3 | (0xFF & (lrcId >> 24));

		}

		int uBound = bytes.length - 1;

		while (uBound >= 0) {

			int c = song[uBound];

			if (c >= 0x80) {

				c = c - 0x100;
			}

			intVal1 = c + intVal2;

			intVal2 = intVal2 << (uBound % 2 + 4);

			intVal2 = intVal1 + intVal2;

			uBound -= 1;
		}

		uBound = 0;

		intVal1 = 0;

		while (uBound <= bytes.length - 1) {

			int c = song[uBound];

			if (c >= 128) {

				c = c - 256;

			}

			int intVal4 = c + intVal1;

			intVal1 = intVal1 << (uBound % 2 + 3);

			intVal1 = intVal1 + intVal4;

			uBound += 1;

		}

		int intVal5 = intVal2 ^ intVal3;

		intVal5 = intVal5 + (intVal1 | lrcId);

		intVal5 = intVal5 * (intVal1 | intVal3);

		intVal5 = intVal5 * (intVal2 ^ lrcId);

		return String.valueOf(intVal5);
	}

	private static String encode(String value) {
		if (value == null) {

			value = "";

		}

		value = value.replace(" ", "").replace("'", "").toLowerCase();

		byte[] bytes = null;

		try {

			bytes = value.getBytes("UTF-16LE");

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

			bytes = value.getBytes();

		}

		return stringify(bytes);
	}

	final private static char[] digit = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String stringify(byte[] bytes) {

		char[] str = new char[2];

		StringBuilder builder = new StringBuilder();

		for (byte byteValue : bytes) {

			str[0] = digit[(byteValue >>> 4) & 0X0F];

			str[1] = digit[byteValue & 0X0F];

			builder.append(str);

		}

		return builder.toString();
	}
}