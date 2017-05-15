package com.ecochain.ledger.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SmsSend {

	//验证码、通知短信参数
	private static String notice_url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	private static String account="cf_dingyao";
	private static String password="654321";
	
	//营销短信参数
	private static String marketing_url = "http://api.yx.ihuyi.cn/webservice/sms.php?method=Submit";
	private static String apikey="50fdb22f1ffbba5982f3eeab83e6a741";
	
	//发送验证码
	public static  String sendSms(String phone) {
		
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(notice_url); 
			
		//client.getParams().setContentCharset("GBK");		
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		
		int mobile_code = (int)((Math.random()*9+1)*100000);

		//System.out.println(mobile);
		
	    String content = new String("您的验证码是：【" + mobile_code + "】,30分钟内有效，如非本人操作，请忽略本短信。");

		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", account), 
//			    new NameValuePair("password", "密码"), //密码可以使用明文密码或使用32位MD5加密
			    new NameValuePair("password",  MD5Util.getMd5Code(password)),
			    new NameValuePair("mobile", phone), 
			    new NameValuePair("content", content),
		};
		
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
				return String.valueOf(mobile_code);
			}else{
				return "0";
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		}	
		
	}
	//发送通知短信
	public static boolean sendSms(String phone,String content) {
	    NameValuePair[] data = PackNoticeParas(phone, content);// 封装手机参数
        return HttpTool.sendData(notice_url,data);
    }
	//发送营销短信
	public static boolean sendMarketingSms(String phoneStr,String content) {
	    NameValuePair[] data = PackMarketingParas(phoneStr, content);// 封装手机参数
	    return HttpTool.sendData(marketing_url,data);
	}
	// 封装通知短信手机参数
	public static NameValuePair[] PackNoticeParas(String phone, String content){
	    System.out.println("发送通知短信参数：phone="+phone+",content="+content);
	    NameValuePair[] data = {//提交短信
                new NameValuePair("account", account), 
                new NameValuePair("password",  MD5Util.getMd5Code(password)),
                new NameValuePair("mobile", phone), 
                new NameValuePair("content", content),
        };
	    return data;
	}
	//封装发送营销短信手机参数
	public static NameValuePair[] PackMarketingParas(String phoneStr, String content){
        System.out.println("发送营销短信参数：phone="+phoneStr+",content="+content);
        NameValuePair[] data = {//提交短信
                new NameValuePair("account", account), 
                new NameValuePair("password",  apikey),
                new NameValuePair("mobile", phoneStr), 
                new NameValuePair("content", content),
        };
        return data;
    }
	public static void main(String [] args) {
		
//		String vcode =sendSms("13522737092");
	    String content = new String("尊敬的【李硕】会员，您好。退订回TD"); 
//        SmsSend.sendSms("18618382548", "尊敬的【三界宝】会员您好，您已成功向三界链钱包账户【三界宝】转出【三界宝】三界宝数字资产。");
//        SmsSend.sendMarketingSms("18618382548,15011478695", content);
        content = content.substring(0, content.indexOf("【")+1)+"18618382548"+content.substring(content.indexOf("】"));
        System.out.println("content=="+content);
	}
	
	
}
