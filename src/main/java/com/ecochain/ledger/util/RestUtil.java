package com.ecochain.ledger.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class RestUtil {
    
    /**
     * @describe:拼接路径参数，如/param1/param2,以rest方式提交post请求
     * @author: zhangchunming
     * @date: 2017年7月19日下午2:07:25
     * @param url
     * @return: String
     */
    public static String restPostPath(String url) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        String result = restTemplate.postForObject(url, null, String.class);
        System.out.println("url="+url);
        System.out.println("result="+result);
        return result;
    }
    /**
     * @describe:拼接路径参数，如/param1/param2,以rest方式提交get请求
     * @author: zhangchunming
     * @date: 2017年7月19日下午2:07:25
     * @param url
     * @return: String
     */
    public static String restGetPath(String url) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        Map<String, Object> urlVariables = new HashMap<String, Object>();
        String result = restTemplate.getForObject(url, String.class, urlVariables);
        System.out.println("url="+url);
        System.out.println("result="+result);
        return result;
    }
    
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.200.86:8001/{net}/create/{userName}/{passWord}";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        Map<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("net", "test");
        urlVariables.put("userName", "test123112");
        urlVariables.put("passWord", "123456");
        String resultDataDto = restTemplate.postForObject(url, null, String.class, urlVariables);
        System.out.println("resultDataDto="+resultDataDto);
        
        try {
            String jsonStr = RestUtil.restGetPath("http://192.168.200.86:8001/sendMoney/18618382662/20917c851c4a54f2a054390dac9085b7/0.001/mknsThD8skfXe1VprNQ4u8A1HQjCTYycLN");
            System.out.println("jsonStr="+jsonStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
