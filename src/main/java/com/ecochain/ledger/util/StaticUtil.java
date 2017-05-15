package com.ecochain.ledger.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
/**
 * 将jsp页面静态化
 * */
public class StaticUtil {
	/**
	 * 将http路径下的jsp模板页http_path得到，替换其中部分内容(格式为 #替换内容# )并生成html页面local_path
	 * 此方法用于创建
	 * */
	@SuppressWarnings("unchecked")
	public static String jsp2sta(String http_path, String local_path,	String fileName, Map<String, String> info)
			throws MalformedURLException, IOException {
		String demo_str = getContent(new URL(http_path));
		if (demo_str.equals("")) {
			return null;
		}
		Iterator iter = info.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			System.out.println("-----key-"+entry.getKey()+"-->>>>>>>>---value-------"+entry.getValue()+"----");
			demo_str = demo_str.replace((String) entry.getKey(),(String) entry.getValue());
		}
		String target_path = local_path + fileName;
		HtmlUtil.writeHtml(target_path, demo_str);
		return fileName;
	}

	/**
	 * 将http路径下的jsp模板页http_path得到，替换其中部分内容(格式为 #替换内容# )并生成html页面local_path,
	 * 其他地方都相同，只有在为html页命名的时候和上一个方法不同，这里是沿用已经创建好的文件名称 此方法用于更新
	 * */
	@SuppressWarnings("unchecked")
	public static void jsp2sta(String http_path, String target_path,
			Map<String, String> info) throws MalformedURLException, IOException {
		String demo_str = getContent(new URL(http_path));
		if (demo_str.equals("")) {
			return;
		}
		// demo_str =
		// demo_str.substring(demo_str.indexOf("<html>"),demo_str.length());
		// demo_str =
		// "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>"
		// + demo_str;

		Iterator iter = info.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			demo_str = demo_str.replace((String) entry.getKey(),
					(String) entry.getValue());
		}
		HtmlUtil.writeHtml(target_path, demo_str);
	}

	public static String getContent(URL url) throws IOException {
		java.net.HttpURLConnection httpConnection = (java.net.HttpURLConnection) url
				.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				httpConnection.getInputStream(), "UTF-8"));
		StringBuffer sb = new StringBuffer();
		String p = null;
		while ((p = br.readLine()) != null) {
			sb.append(p);
		}
		br.close();
		return sb.toString();
	}
}
