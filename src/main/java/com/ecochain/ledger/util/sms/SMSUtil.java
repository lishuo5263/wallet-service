package com.ecochain.ledger.util.sms;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ecochain.ledger.util.MD5Util;


/**
 * 测试
 * @author my
 *
 */
public class SMSUtil {
    /*private static String username = "mmb888";
    private static String password = "j2mJTEpC";*/
    public static String username = "sj888";
    public static String password = "E7SS4Ibz";
//  private static String username = "sanjie";
//  private static String password = "San16888";
    public static String notice_productid = "887362";//管理商通知专用
    public static String vcode_productid = "676767";//优质验证码专用
    public static String market_productid = "435227";//商超会员营销
//  private static String url = "http://www.ztsms.cn:8800/sendNSms.do";
    private static String url = "http://www.isanjie.com/ztsms/sendNSms.do";
    //http://www.ztsms.cn:8800/sendManyNSms.do  多号码，多内容
    //http://www.ztsms.cn:8800/sendNSms.do      同内容，不同号码
    public static boolean sendSMS_ChinaNet1(String mobile,String content,String productid) throws Exception{
        
        Map paramentMap = new LinkedHashMap();
        paramentMap.put("username", username);//用户名
        String strtime = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
//        String pass = MD5Gen.getMD5(MD5Gen.getMD5("密码")+strtime);
        String pass =MD5Util.getMd5Code(MD5Util.getMd5Code(password)+strtime);
        paramentMap.put("tkey",  strtime);
        paramentMap.put("password", pass);//加密后密码
        paramentMap.put("productid", productid);//产品id
        paramentMap.put("mobile", mobile);//号码
        paramentMap.put("content",  content);//内容
        paramentMap.put("xh",  "");
        String status = "";
        status = sendHttpRequest16(url, paramentMap, "UTF-8", "POST");
        System.out.println("短信发送状态-status："+status);
        if(status.contains(",")&&status.substring(0, 1).equals("1")){
            return true;
        }
        return false;
    }
    
    public static String sendSMS(String mobile,String content,String productid) throws Exception{
        Map paramentMap = new LinkedHashMap();
        paramentMap.put("username", username);//用户名
        String strtime = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
//        String pass = MD5Gen.getMD5(MD5Gen.getMD5("密码")+strtime);
        String pass =MD5Util.getMd5Code(MD5Util.getMd5Code(password)+strtime);
        paramentMap.put("tkey",  strtime);
        paramentMap.put("password", pass);//加密后密码
        paramentMap.put("productid", productid);//产品id
        paramentMap.put("mobile", mobile);//号码
        paramentMap.put("content",  content);//内容
        paramentMap.put("xh",  "");
        String status = "";
        status = sendHttpRequest16(url, paramentMap, "UTF-8", "POST");
        System.out.println("短信发送状态-status："+status);
        return status;
    }
    
    public static String sendHttpRequest16(String strUrl, Map<String, String> paramentMap, String anaycle, String presendway) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(strUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod(presendway);
            httpConn.setConnectTimeout(60000);
            httpConn.setReadTimeout(60000);

            StringBuffer parament = new StringBuffer();
            for (Map.Entry entry : paramentMap.entrySet()) {
                if (("".equals(entry.getKey())) || (entry.getKey() == null)) {
                    parament.append(URLEncoder.encode((String) entry.getValue(), anaycle) + "&");
                } else if ((((String) entry.getKey()).equalsIgnoreCase("MOBILE")) || (((String) entry.getKey()).equalsIgnoreCase("phone")) || 
                        (((String) entry.getKey()).equalsIgnoreCase("mb")) || (((String) entry.getKey()).equalsIgnoreCase("tele"))) {
                    if (((String) entry.getValue()).endsWith(",")) {// 手机号以,结尾
                        parament.append((String) entry.getKey() + "=" + ((String) entry.getValue()).substring(0, ((String) entry.getValue()).length() - 1) + "&");
                    } else {
                        parament.append((String) entry.getKey() + "=" + ((String) entry.getValue()) + "&");
                    }
                } else if ((((String) entry.getKey()).equalsIgnoreCase("Content")) || (((String) entry.getKey()).equalsIgnoreCase("Message")) || (((String) entry.getKey()).equalsIgnoreCase("ms"))
                        || (((String) entry.getKey()).equalsIgnoreCase("msg_content")) || (((String) entry.getKey()).equalsIgnoreCase("msg")) || (((String) entry.getKey()).equalsIgnoreCase("sms"))
                        || (((String) entry.getKey()).equalsIgnoreCase("smscontent"))|| (((String) entry.getKey()).equalsIgnoreCase("smsg")))
                    parament.append((String) entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(), anaycle) + "&");
                else {
                    parament.append((String) entry.getKey() + "=" + (String) entry.getValue() + "&");
                }
            }
            //log.info(url+"?"+parament.toString());
            if (("".equals(parament.toString())) || (parament.toString() == null))
                return "9999";
            if (parament.toString().endsWith("&")) {
                parament.setLength(parament.length() - 1);
            }

            //httpConn.setRequestProperty("Content-Length", String.valueOf(parament.toString().getBytes().length));
            httpConn.setRequestProperty("Content-Length", String.valueOf(parament.toString().getBytes().length));
            System.out.println("parament.toString()="+parament.toString());
            httpConn.getOutputStream().write(parament.toString().getBytes());
            httpConn.getOutputStream().flush();
            httpConn.getOutputStream().close();
            InputStream is = httpConn.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is,anaycle));
            String tr = "";
            while ((tr = bf.readLine()) != null) {
                sb.append("\n").append(tr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return sb.toString().replaceFirst("\n", "");
    }
    public static void main(String[] args) throws Exception {
        
        //for (int i = 0; i < 1; i++) {
            //new Testdd("---" + i).start();
        //}
        System.out.println("start tiime:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date(System.currentTimeMillis())));
        SMSUtil.sendSMS_ChinaNet1("18618382548",  "欢迎您注册三界生活账号，您的验证码是：123456，打死也不能告诉别人哦！",vcode_productid);
        System.out.println("start tiime:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date(System.currentTimeMillis())));
//        SMSUtil.sendSMS_ChinaNet1("022-123123123",  "欢迎您注册三界生活账号，您的验证码是：654321，打死也不能告诉别人哦！",vcode_productid);
        /*SMSUtil.sendSMS_ChinaNet1("18618382548",  
                "欢迎光临三界生活，您正在注册账号18618382548，验证码是：325581，打死也不能告诉别人哦！",vcode_productid);
        System.out.println((int)((Math.random()*9+1)*100000)+"");*/
    }
}
