package com.ecochain.ledger.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HtmlUtil {

	public static String readHtml(String filepath,String filename){
		StringBuffer text = new StringBuffer();
		InputStreamReader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(new FileInputStream(filepath), "UTF-8");
			br = new BufferedReader(reader);
			String line = null;
			while((line=br.readLine())!=null){
				text.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text.toString();
	}
	public static String readHtmlByPath(String filepath){
		StringBuffer text = new StringBuffer();
		InputStreamReader reader = null;
		BufferedReader br = null;
		try {
			System.out.println(filepath);
			reader = new InputStreamReader(new FileInputStream(filepath), "UTF-8");
			br = new BufferedReader(reader);
			String line = null;
			while((line=br.readLine())!=null){
				text.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text.toString();
	}
	public static void writeHtml(String filepath, String xml){
		File f = new File(filepath);
		if(f.exists()){
			f.delete();
		}
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8");
			//生成html关闭下面页面指令，生成jsp打开下面页面指令
			writer.write(xml);
			//writer.write("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>"+xml);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
}
