package com.ecochain.ledger.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Http操作辅助工具
 */
public class HttpTool {
	/**
	 * 发送请求数据
	 * @param url url地址
	 * @param data 发送内容 key=value形式
	 * @return 返回结果
	 * @throws Exception
	 */
	public static boolean sendData(String url, NameValuePair[] data) {
	    HttpClient client = new HttpClient(); 
        PostMethod method = new PostMethod(url); 
        //client.getParams().setContentCharset("GBK");      
        client.getParams().setContentCharset("UTF-8");
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
        method.setRequestBody(data);
        try {
            client.executeMethod(method);   
            
            String SubmitResult =method.getResponseBodyAsString();
                    
            //System.out.println(SubmitResult);

            Document doc = DocumentHelper.parseText(SubmitResult); 
            Element root = doc.getRootElement();


            String code = root.elementText("code"); 
            String msg = root.elementText("msg");   
            String smsid = root.elementText("smsid");   
            
            
            System.out.println(code);
            System.out.println(msg);
            System.out.println(smsid);
                        
             if("2".equals(code)){
                System.out.println("短信提交成功");
                return true;
            }else{
                return false;
            }
            
        } catch (HttpException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
	}

    public static String doPost(String strUrl, String paramentStr) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(strUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            httpConn.setConnectTimeout(60000);
            httpConn.setReadTimeout(60000);
            httpConn.setRequestProperty("Content-Type", "applicatoin/json");
            //httpConn.setRequestProperty("Content-Length", String.valueOf(paramentMap.toString().getBytes().length));
            if(paramentStr!=null){
                httpConn.getOutputStream().write(paramentStr.toString().getBytes());
            }
            httpConn.getOutputStream().flush();
            httpConn.getOutputStream().close();
            InputStream is = httpConn.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String tr = "";
            while ((tr = bf.readLine()) != null) {
                sb.append("\n").append(tr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().replaceFirst("\n", "");
    }

    public static void main(String[] args) throws Exception{
        Map<String, String> paramentMap =new HashMap<String, String>();
        /*paramentMap.put("address","f1gCgjsEhW8KyKcB18bPQzvJjeAVEgmBN6ubzdcR1njE");
        paramentMap.put("data","test");
        paramentMap.put("sign","AN1rKvtaZqkg1NiKBzCPRrXpyxzmpuJ57sJaZZNXqjYwBG1X51Uc5auqhKCNTagNqJUGkRzqk94mDzBUjMsWm9E3mcVaQsQzD");
        String result =doPost("http://192.168.10.47:8332/send_data_to_sys",com.alibaba.fastjson.JSON.toJSONString(paramentMap));
        System.out.println("send_data_to_sys --------->"+result);
        Map m =(Map)com.alibaba.fastjson.JSON.parse(result);
        if(StringUtil.isNotEmpty(m.get("result").toString())){
            StringBuffer stringBuffer =new StringBuffer().append("\"").append(m.get("result").toString()).append("\"");
            String getBlockInfo =doPost("http://192.168.10.47:8332/get_data_from_sys",stringBuffer.toString());
            System.out.println("get_data_from_sys -------------->"+getBlockInfo);
        }*/

        StringBuffer stringBuffer =new StringBuffer().append("\"").append("e3230f30c2b53f8d6b6a44dd2eb5a5eef255b1234dfcbcd5899396f77ac5def1").append("\"");
        String getBlockInfo =doPost("http://192.168.10.47:8332/get_data_from_sys",stringBuffer.toString());
        Map m =(Map)com.alibaba.fastjson.JSON.parse(getBlockInfo);
        Map m3 =(Map)com.alibaba.fastjson.JSON.parse(com.alibaba.fastjson.JSON.parse(m.get("result").toString()).toString());
        System.out.println(m);
        System.out.println(m3);
    }
}