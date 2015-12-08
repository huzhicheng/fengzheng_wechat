package org.fengzheng.wechat.common;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;


public class NetWorkHelper {
    public String GetMessage(String path) {

        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            //System.out.println("result=" + buffer.toString());
            conn.disconnect();
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public void PostMessage(String sender, String content, String path) {
        // String path = "http://192.168.1.106:8009/messageApi";

        try {
            Map<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("sender", "12");
            //requestParams.put("content", "中国");
            StringBuilder params = new StringBuilder();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.append(entry.getKey());
                params.append("=");
                params.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                params.append("&");
            }
            if (params.length() > 0)
                params.deleteCharAt(params.length() - 1);
            byte[] data = params.toString().getBytes();
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(data.length));
            // POST方式，其实就是浏览器把数据写给服务器
            conn.setDoOutput(true); // 设置可输出流
            // OutputStream os = conn.getOutputStream(); // 获取输出流
            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            outStream.write(data); // 将数据写给服务器
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                // return StreamTools.streamToString(is);
            } else {
                // return "网络访问失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return 网络访问失败;
        }
    }

    public String getURLResponse(String urlString) {
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL(urlString); //URL对象
            conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载

            //在android中必须将此项设置为false
            conn.setDoOutput(false); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }
            System.out.println(resultData);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("MalformedURLException:" + e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("IOException:" + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return resultData;
    }

    public String getHttpsResponse(String hsUrl,String requestMethod) {
        URL url;
        InputStream is = null;
        String resultData = "";
        try {
            url = new URL(hsUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            TrustManager[] tm = {xtm};

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });


            con.setDoInput(true); //允许输入流，即允许下载

            //在android中必须将此项设置为false
            con.setDoOutput(false); //允许输出流，即允许上传
            con.setUseCaches(false); //不使用缓冲
            if(null!=requestMethod && !requestMethod.equals("")) {
                con.setRequestMethod(requestMethod); //使用指定的方式
            }
            else{
                con.setRequestMethod("GET"); //使用get请求
            }
            is = con.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }
            System.out.println(resultData);


            Certificate[] certs = con.getServerCertificates();

            int certNum = 1;

            for (Certificate cert : certs) {
                X509Certificate xcert = (X509Certificate) cert;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }


    /**
     * 下载文件
     * @param hsUrl
     * @return
     */
    public String DownLoadHttpsFile(String hsUrl,String fileName,String path) {
        URL url;
        InputStream is = null;
        String filePath = path+fileName;
        try {
            url = new URL(hsUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            TrustManager[] tm = {xtm};

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });


            con.setDoInput(true); //允许输入流，即允许下载

            //在android中必须将此项设置为false
            con.setDoOutput(false); //允许输出流，即允许上传
            con.setUseCaches(false); //不使用缓冲
            con.setRequestMethod("GET"); //使用get请求
            is = con.getInputStream();   //获取输入流，此时才真正建立链接

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len=is.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }
            is.close();
            byte[] fileBytes = outStream.toByteArray();

            File file = new File(filePath);
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(fileBytes);
            fops.flush();
            fops.close();
           /* Certificate[] certs = con.getServerCertificates();
            for (Certificate cert : certs) {
                X509Certificate xcert = (X509Certificate) cert;
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * HTTPS协议的POST请求
     * @param hsUrl  请求地址
     * @param json   请求数据
     * @return
     */
    public String PostHttpsResponse(String hsUrl,String json) {
        URL url;
        InputStream is = null;
        String resultData = "";
        try {
            url = new URL(hsUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            TrustManager[] tm = {xtm};

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });


            con.setDoInput(true); //允许输入流，即允许下载

            //在android中必须将此项设置为false
            con.setDoOutput(true); //允许输出流，即允许上传
            con.setUseCaches(false); //不使用缓冲
            con.setRequestMethod("POST"); //使用get请求

            //表单数据
            if (null != json) {
                OutputStream outputStream = con.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }


            is = con.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }
            System.out.println(resultData);

           /* log(con.getResponseCode());
            log(con.getCipherSuite());
            log("");*/
            Certificate[] certs = con.getServerCertificates();

            int certNum = 1;

            for (Certificate cert : certs) {
                X509Certificate xcert = (X509Certificate) cert;
                /*log("Cert No. " + certNum++);
                log(xcert.getType());
                log(xcert.getPublicKey().getAlgorithm());
                log(xcert.getIssuerDN());
                log(xcert.getIssuerDN());
                log(xcert.getNotAfter());
                log(xcert.getNotBefore());
                log("");*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }

    X509TrustManager xtm = new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }
    };
}