package com.example.lrc;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.URL;

import java.net.URLConnection;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

/**
 * 
 * @author wgq
 */

public class HttpGet {

	public static void main(String[] args) throws IOException {

		System.out.println(HttpGet.get("http://www.iscripts.org/", "utf-8"));

	}

	public static String get(String strURL) throws IOException {

		URLConnection conn = HttpGet.openURL(strURL);

		return HttpGet.read(conn.getInputStream(),
				HttpGet.getContentEncoding(conn));

	}

	public static String get(String strURL, String encoding) throws IOException {

		return HttpGet.read(HttpGet.openURL(strURL).getInputStream(), encoding);

	}

	private static URLConnection openURL(String strURL) throws IOException {

		URLConnection conn = new URL(strURL).openConnection();

		conn.connect();

		return conn;

	}

	private static String read(InputStream in, String encoding)
			throws IOException {

		if (encoding == null)
			encoding = "utf-8";

		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				encoding));

		char[] str = new char[4096];

		StringBuilder builder = new StringBuilder();

		for (int len; (len = reader.read(str)) > -1;)

			builder.append(str, 0, len);

		return builder.toString();

	}

	private static String getContentEncoding(URLConnection conn) {

		String contentType = conn.getContentType();

		if (contentType == null)
			return null;

		final Pattern ptnCharset = Pattern.compile("(?i)\\bcharset=([^\\s;]+)");

		Matcher m = ptnCharset.matcher(contentType);

		if (m.find())
			return m.group(1);

		return null;

	}

}