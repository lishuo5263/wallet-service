package com.ecochain.ledger.util;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
/**
 * Http操作辅助工具
 */
public class HttpUtil {
    private final static String url = "http://a1.easemob.com/dongadong/dongadong/users";
    private final static String params = "{\"username\":\"goucai5\",\"password\":\"goucai5\",\"nickname\":\"goucai5\"}";
    private final static int CONNECT_TIMEOUT = 5000; // in milliseconds  
    private final static String DEFAULT_ENCODING = "UTF-8";  
	/**
	 * 发送GET请求数据
	 * @param get_url url地址
	 * @param content 发送内容 key=value形式
	 * @return 返回结果
	 * @throws Exception
	 */
	public static String sendGetData(String get_url, String content) throws Exception {
		//String result = "";
		StringBuffer result=new StringBuffer("");
		URL getUrl = null;
		BufferedReader reader = null;
		String lines = "";
		HttpURLConnection connection = null;
		try {
			if (content != null && !content.equals(""))
				get_url = get_url + "?" + content;
			//	get_url = get_url + "?" + URLEncoder.encode(content, "utf-8");
			getUrl = new URL(get_url);
			connection = (HttpURLConnection) getUrl.openConnection();
			connection.connect();
			// 取得输入流，并使用Reader读取
			reader = new BufferedReader(new InputStreamReader(connection
					.getInputStream(), "utf-8"));// 设置编码
			//reader = new BufferedReader(new InputStreamReader(connection
			//.getInputStream(), "utf-8"));
			while ((lines = reader.readLine()) != null) {
				result.append(lines);
			}
			return result.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
			connection.disconnect();
		}
	}
	public static String sendGetData(String get_url) throws Exception {
		//String result = "";
		StringBuffer result=new StringBuffer("");
		URL getUrl = null;
		BufferedReader reader = null;
		String lines = "";
		HttpURLConnection connection = null;
		try {
			
			getUrl = new URL(get_url);
			connection = (HttpURLConnection) getUrl.openConnection();
			connection.connect();
			// 取得输入流，并使用Reader读取
			reader = new BufferedReader(new InputStreamReader(connection
					.getInputStream(), "utf-8"));// 设置编码
			while ((lines = reader.readLine()) != null) {
				result.append(lines).append("\n");
			}
			return result.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
			connection.disconnect();
		}
	}
	
	
	/**
	 * 发送POST请求数据
	 * @param POST_URL url地址
	 * @param content 发送内容  key=value形式
	 * @return 返回结果
	 * @throws Exception
	 */
	public static String sendPostData(String POST_URL, String content)
			throws Exception {
		HttpURLConnection connection=null;
		DataOutputStream out=null;
		BufferedReader reader=null;
		String line = "";
		StringBuffer result=new StringBuffer("");
		try {
			URL postUrl = new URL(POST_URL);
			connection= (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			// Post 请求不能使用缓存
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.connect();
			// 发送流数据
			out = new DataOutputStream(connection.getOutputStream());
			//content = URLEncoder.encode(content, "utf-8");
			// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
			out.writeBytes(content);
			out.flush();
			out.close();
			//获取结果
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));// 设置编码
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}		
			return result.toString();
		} catch (Exception e) {
			throw e;
		}finally
		{
			if(out!=null)
			{
				out.close();
				out=null;				
			}
			if(reader!=null)
			{
				reader.close();
				reader=null;				
			}
			connection.disconnect();
		}
	}
	
	public static String postData(String urlStr, String data, String contentType){  
        BufferedReader reader = null;  
        try {  
            URL url = new URL(urlStr);  
            URLConnection conn = url.openConnection();  
            conn.setDoOutput(true);  
            conn.setConnectTimeout(CONNECT_TIMEOUT);  
            conn.setReadTimeout(CONNECT_TIMEOUT);  
            if(contentType != null)  
                conn.setRequestProperty("content-type", contentType);  
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);  
            if(data == null)  
                data = "";  
            writer.write(data);   
            writer.flush();  
            writer.close();    
  
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));  
            StringBuilder sb = new StringBuilder();  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                sb.append(line);  
                sb.append("\r\n");  
            }  
            return sb.toString();  
        } catch (IOException e) {  
            //logger.error("Error connecting to " + urlStr + ": " + e.getMessage());  
            System.out.println("Error connecting to " + urlStr + ": " + e.getMessage());  
        } finally {  
            try {  
                if (reader != null)  
                    reader.close();  
            } catch (IOException e) {  
            }  
        }  
        return null;  
    } 
	
	/*
	 * 过滤掉html里不安全的标签，不允许用户输入这些标签
	 */
	public static String htmlFilter(String inputString) {
		//return inputString;
		  String htmlStr = inputString; // 含html标签的字符串
		  String textStr = "";
	    //  java.util.regex.Pattern p_script;
		// java.util.regex.Matcher m_script;
		 	
		
		  htmlStr = htmlStr.replace("<", "&lt;");
		  htmlStr = htmlStr.replaceAll( ">", "&gt;");
		  
		  try {
		   String regEx_script = "<[\\s]*?(script|style)[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?(script|style)[\\s]*?>"; 
		   String regEx_onevent="on[^\\s]+=\\s*";
		   String regEx_hrefjs="href=javascript:";
		   String regEx_iframe="<[\\s]*?(iframe|frameset)[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?(iframe|frameset)[\\s]*?>";
		   String regEx_link="<[\\s]*?link[^>]*?/>";
		  
		   htmlStr = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE).matcher(htmlStr).replaceAll(""); 
		   htmlStr=Pattern.compile(regEx_onevent, Pattern.CASE_INSENSITIVE).matcher(htmlStr).replaceAll("");
		   htmlStr=Pattern.compile(regEx_hrefjs, Pattern.CASE_INSENSITIVE).matcher(htmlStr).replaceAll("");
		   htmlStr=Pattern.compile(regEx_iframe, Pattern.CASE_INSENSITIVE).matcher(htmlStr).replaceAll("");
		   htmlStr=Pattern.compile(regEx_link, Pattern.CASE_INSENSITIVE).matcher(htmlStr).replaceAll("");
		  
		   //p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		  // m_html = p_html.matcher(htmlStr);
		  // htmlStr = m_html.replaceAll(""); // 过滤html标签

		   textStr = htmlStr;

		  } catch (Exception e) {
		   System.err.println("Html2Text: " + e.getMessage());
		  }

		  return textStr;
		}
	/**
	 * @describe:以post方式发送json格式数据
	 * @author: zhangchunming
	 * @date: 2016年10月7日上午10:32:41
	 * @param strURL
	 * @param params
	 * @return: String
	 */
	public static String postJson(String strURL, String params) {
        String back = "";
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            InputStream is = connection.getInputStream();
            try {
                byte[] data = readStream(is);
                back = new String(data, "utf-8");
                System.out.println(back);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return back;// 自定义错误信息
    }
    public static byte[] readStream(InputStream is) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            bout.write(buffer, 0, len);
        }
        bout.close();
        is.close();
        return bout.toByteArray();
    }
    public static void main(String[] args) {
        String post = postJson(url, params);
        System.out.println(post);
    }
}