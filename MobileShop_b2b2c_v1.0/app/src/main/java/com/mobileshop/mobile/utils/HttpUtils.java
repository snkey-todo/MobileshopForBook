package com.mobileshop.mobile.utils;


import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.mobileshop.mobile.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Dawei on 4/27/15.
 */
public class HttpUtils {


    /**
     * 发送post请求
     * @param uri
     * @param params
     * @return
     */
    public static String post(String uri, Map<String,String> params){
        String url = Constants.baseUrl + uri;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        Iterator<String> keyIterator = params.keySet().iterator();
        while(keyIterator.hasNext()){
            String key = keyIterator.next();
            nameValuePair.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }

        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

        HttpContext httpContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        //将webview中的cookies加到请求头
        CookieManager cookieManager = CookieManager.getInstance();
        if(cookieManager.hasCookies()) {
            String cookie = cookieManager.getCookie(url);
            if(!StringUtils.isEmpty(cookie)) {
                httpPost.setHeader("Cookie", cookie);
            }
        }

        HttpResponse response;
        try{
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                //将新加的cookies同步到CookieManager
                List<Cookie> cookieList = cookieStore.getCookies();
                if(cookieList != null && cookieList.size() > 0){
                    for(Cookie cookie : cookieList){
                        cookieManager.setCookie(Constants.baseUrl, cookie.getName() + "=" + cookie.getValue());
                    }
                    CookieSyncManager.getInstance().sync();
                }

                return EntityUtils.toString(response.getEntity());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 主机更换
     * @param context
     * @param url
     * @return
     */
    public static String replaceBaseUrl(Context context, String url){
        return url.replace("http://localhost:8080",Constants.baseUrl);

    }
    public static String getJson(String uri){
        return get(Constants.baseUrl + uri, "application/json; charset=utf-8");
    }

    public static void getJsonByOkHttp(String url){

    }

    public static String get(String url, String contentType){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        if(!StringUtils.isEmpty(contentType)){
            request.addHeader("Content-Type", contentType);
        }
        HttpContext httpContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        //将webview中的cookies加到请求头
        CookieManager cookieManager = CookieManager.getInstance();
        if(cookieManager.hasCookies()) {
            String cookie = cookieManager.getCookie(url);
            if(!StringUtils.isEmpty(cookie)) {
                request.setHeader("Cookie", cookie);
            }
        }

        HttpResponse response;
        try{
            response = httpClient.execute(request, httpContext);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                //将新加的cookies同步到CookieManager
                List<Cookie> cookieList = cookieStore.getCookies();
                if(cookieList != null && cookieList.size() > 0){
                    for(Cookie cookie : cookieList){
                        cookieManager.setCookie(Constants.baseUrl, cookie.getName() + "=" + cookie.getValue());
                    }
                    CookieSyncManager.getInstance().sync();
                }

                return EntityUtils.toString(response.getEntity());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * HttpURLConnection Login
     * @param username
     * @param password
     */
    public static String loginByGet(String username, String password) {
        try {
            // 设置请求的地址 通过URLEncoder.encode(String s, String enc)
            // 使用指定的编码机制将字符串转换为 application/x-www-form-urlencoded 格式
            username = URLEncoder.encode(username,"UTF-8");
            password = URLEncoder.encode(password,"UTF-8");
            String url =Constants.baseUrl+ "/api/mobile/member!loginApp.do?username=" + username + "&password=" + password;

           // 根据地址创建URL对象(网络访问的url)
            URL newUrl = new URL(url);
            // url.openConnection()打开网络链接
            HttpURLConnection urlConnection = (HttpURLConnection) newUrl.openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(5000);// 设置超时的时间
            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
            // 设置请求的头
            urlConnection.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

            // 获取响应的状态码 404 200 505 302
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    os.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                os.close();

                // 返回字符串
                String result = new String(os.toByteArray());
                System.out.println("-->"+ result);
                return result;
            } else {
                System.out.println("------------------链接失败-----------------");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("------------------链接异常-----------------");
            return "";
        }
    }
}
