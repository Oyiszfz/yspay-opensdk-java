package com.eptok.yspay.opensdkjava.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 功能描述
 *
 * @author: zyu
 * @description:
 * @date: 2019/3/22 11:10
 */
public class HttpClientUtil {

    private static final Logger logger = Logger.getLogger("HttpClientUtil");

    /**
     * @Description 发送post请求，json数据
     * @Date 15:19 2021/9/26
     * @Param 
     * @return 
    */
    public static String sendPostJson(String url, String param) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //注意在传送json数据时， Content-Type的值
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.info("send http request error :"+e);
            throw e;
        } finally{
            //使用finally块来关闭输出流、输入流
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }catch(IOException ex){
                throw ex;
            }
        }
        return result;
    }


    /**
     * 提交file模拟form表单
     * @param url
     * @param paramMap
     * @param fileKey
     * @return
     */
    public static String sendPostFile(String url,Map<String,String> paramMap,byte[] fileByte,String fileName,String fileKey) throws IOException {
        String result = "";
        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器的域名
            URL realurl = new URL(url);
            // 发送POST请求必须设置如下两行
            HttpURLConnection connection = (HttpURLConnection) realurl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setReadTimeout(20000);
            connection.setConnectTimeout(20000);

            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection","Keep-Alive");
            connection.setRequestProperty("Charset","UTF-8");
            connection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
            // 传输内容
            StringBuilder contentBody =null;
            //输出
            OutputStream out = connection.getOutputStream();
            // 1. 处理普通表单域(即形如key = value对)的POST请求（这里也可以循环处理多个字段，或直接给json）
            //这里看过其他的资料，都没有尝试成功是因为下面多给了个Content-Type
            //form-data  这个是form上传 可以模拟任何类型
            if (paramMap != null) {
                contentBody = new StringBuilder();
                for (String s : paramMap.keySet()) {
                    contentBody.append(boundaryPrefix + BOUNDARY);
                    contentBody.append(newLine);
                    contentBody.append("Content-Disposition: form-data; name=\"");
                    contentBody.append(s);
                    contentBody.append("\"").append(newLine).append(newLine);
                    contentBody.append(paramMap.get(s));
                    contentBody.append(newLine);
                }
                out.write(contentBody.toString().getBytes());
            }

            // 2. 处理file文件的POST请求（多个file可以循环处理）
            contentBody = new StringBuilder();
            contentBody.append(boundaryPrefix + BOUNDARY)
                    .append(newLine)
                    .append("Content-Disposition:form-data; name=\"")
                    .append(fileKey +"\"; ")   // form中field的名称
                    .append("filename=\"")
                    .append(fileName +"\"")   //上传文件的文件名，包括目录
                    .append(newLine)
                    .append("Content-Type:multipart/form-data")
                    .append(newLine).append(newLine);
            String boundaryMessage2 = contentBody.toString();
            out.write(boundaryMessage2.getBytes("utf-8"));

            // 开始真正向服务器写文件
            /*DataInputStream dis= new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut =new byte[(int) file.length()];
            bytes =dis.read(bufferOut);
            dis.close();
            out.write(fileByte,0, bytes);*/
            out.write(fileByte,0, fileByte.length);
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 4. 从服务器获得回答的内容
            String strLine="";
            String strResponse ="";
            InputStream in =connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((strLine =reader.readLine()) != null)
            {
                strResponse +=strLine +"\n";
            }
            return strResponse;
        } catch (Exception e) {
           logger.info("send http request error :" + e);
            throw e;
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostParam(String url, String param) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            param = param.replaceAll("\\+", "%2B");
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.info("send http request error :"+e);
            throw e;
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
     * @Description 发送post请求，json数据
     * @Date 15:19 2021/9/26
     * @Param
     * @return
     */
    public static byte[] sendPostDownFile(String url, String param) throws IOException {
        PrintWriter out = null;
        InputStream in = null;
        ByteArrayOutputStream output = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //注意在传送json数据时， Content-Type的值
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);;

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = conn.getInputStream();
            output= new ByteArrayOutputStream();
            byte[] buffer = new byte[1024*4];
            int n = 0;
            while (-1 != (n = in.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (Exception e) {
            logger.info("send http request error :"+e);
            throw e;
        } finally{
            //使用finally块来关闭输出流、输入流
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
                if(output!=null){
                    output.close();
                }
            }catch(IOException ex){
                throw ex;
            }
        }
    }
}
