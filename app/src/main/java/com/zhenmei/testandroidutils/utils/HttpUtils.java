package com.zhenmei.testandroidutils.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Http请求工具类
 */
public class HttpUtils {

    /**
     * 连接超时的时间设置
     */
    private static final int TIMEOUT_IN_MILLIONS = 5000;

    /**
     * 回调接口
     */
    public interface CallBack {
        void onRequestComplete(String result);
    }

    /**
     * 异步的Get请求
     */
    public static void doGetAsyn(final String urlStr, final CallBack callBack) {
        new Thread() {
            public void run() {
                try {
                    String result = doGet(urlStr);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Get请求，获得返回数据
     */
    public static String doGet(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            }
            conn.disconnect();
        }
        return null;
    }

    /**
     * 异步的Post请求
     */
    public static void doPostAsyn(final String urlStr, final String params,
                                  final CallBack callBack) throws Exception {
        new Thread() {
            public void run() {
                try {
                    String result = doPost(urlStr, params);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * Post请求，获得返回数据
     */
    public static String doPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl
                    .openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

            if (param != null && !param.trim().equals("")) {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 用HttpURLConnection的get请求来访问
     */
    public static String httpUrlGet(String URL, Map<String, Object> map) {
        String result = "";
        // 拼接地址
        String newURL = URL + "?" + checkMap(map);
        try {
            // 创建url
            URL url = new URL(newURL);
            // 利用url打开一个连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置连接方式为get
            conn.setRequestMethod("GET");
            // 获取输入流，转换成缓冲流方便读取
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            // 判断连接是否成功
            if (conn.getResponseCode() == 200) {
                String line = "";
                while ((line = reader.readLine()) != null) {
                    // 拼接结果
                    result += line;
                }
            } else {// 访问失败
                result = "HttpUrlConnectionGet方式请求失败";
            }
            // 关闭流
            in.close();
            // 断开连接
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 用HttpURLConnection的post请求来访问
     */
    public static String httpUrlPost(String URL, Map<String, Object> map) {
        String result = "";
        try {
            // 创建url
            URL url = new URL(URL);
            // 利用url打开一个连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置连接方式为post
            conn.setRequestMethod("POST");
            // 设置可以向外输出信息，也可以接收信息
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置请求头信息
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");
            // 要发送的信息
            String data = checkMap(map);
            // 设置数据内容的长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            // 获取输出流，发送信息给服务器
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            // 获取输入流，转换成缓冲流方便读取
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            // 判断连接是否成功
            if (conn.getResponseCode() == 200) {
                String line = "";
                while ((line = reader.readLine()) != null) {
                    // 拼接结果
                    result += line;
                }
            } else {// 访问失败
                result = "HttpUrlConnectionPost方式请求失败";
            }
            // 关闭流
            in.close();
            // 断开连接
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 用HttpClient的get请求来访问
     */
    public static String httpClientGet(String URL, Map<String, Object> map) {
        String result = null;
        // 拼接地址
        String newURL = URL + "?" + checkMap(map);
        // 创建 HttpClient的实例
        HttpClient client = new DefaultHttpClient();
        try {
            // 创建某种连接方法的实例，在这里是HttpGet。
            HttpGet get = new HttpGet(newURL);
            // 调用HttpClient对象的execute(HttpUriRequest request)发送请求
            HttpResponse response = client.execute(get);
            // 调用HttpResponse的getEntity()方法可获取HttpEntity对象
            HttpEntity entity = response.getEntity();
            // 判断是否能够请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获取返回的数据
                result = EntityUtils.toString(entity, "UTF-8");
            } else {// 访问失败
                result = "HttpClientGet方式请求失败败";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * 用HttpClient的post请求来访问
     */
    public static String httpClientPost(String URL, Map<String, Object> map) {
        String result = null;
        // 创建 HttpClient的实例
        HttpClient client = new DefaultHttpClient();
        try {
            // 创建某种连接方法的实例，在这里是HttpPost。
            HttpPost post = new HttpPost(URL);
            // 封装传递参数的集合 键值对NameValuePair
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            // 往这个集合中添加你要传递的参数
            Set<String> set = map.keySet();
            // 创建传递参数封装
            for (String key : set) {
                list.add(new BasicNameValuePair(key, (String) map.get(key)));
            }
            // 实体对象，设置传递参数的编码
            // UrlEncodedFormEntity entitys = new UrlEncodedFormEntity(list,
            // "UTF-8");
            HttpEntity entitys = new UrlEncodedFormEntity(list);
            // 把实体对象存入到httpPost对象中
            post.setEntity(entitys);
            // 调用HttpClient对象的execute(HttpUriRequest request)发送请求
            HttpResponse response = client.execute(post);
            // 调用HttpResponse的getEntity()方法可获取HttpEntity对象
            HttpEntity entity = response.getEntity();
            // 判断是否能够请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获取返回的数据
                result = EntityUtils.toString(entity, "UTF-8");
            } else {// 访问失败
                result = "HttpClientPost方式请求失败";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * Map拼接参数成数据
     */
    private static String checkMap(Map<String, Object> map) {
        String data = "";
        Set<String> set = map.keySet();
        for (String key : set) {
            data = data + key + "=" + map.get(key) + "&";
        }
        data = data.substring(0, data.length() - 1);
        return data;
    }
}
