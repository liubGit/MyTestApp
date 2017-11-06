package liub.com.mylibrary.net;


import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liub on 2017/10/26 .
 * 网络操作工具类
 */
public class HttpClientUtils {
    private final MediaType jsonMT = MediaType.parse("application/json; charset=utf-8");
    private final static int WRITE_TIMEOUT = 10, READ_TIMEOUT = 10, CONN_TIMEOUT = 10;
    private static HttpClientUtils clientUtils;
    private OkHttpClient httpClient;

    private HttpClientUtils() {
        httpClient = new OkHttpClient();
    }

    public static HttpClientUtils getInstance() {
        if (clientUtils == null) {
            synchronized (HttpClientUtils.class) {
                clientUtils = new HttpClientUtils();
            }
        }
        return clientUtils;
    }

    /**
     * 返回一个request对象
     */
    private Request getRequest(String strURl, long tag) throws Exception {
        return new Request.Builder().url(strURl).tag(tag).build();
    }

    /***
     * 同步get请求
     */
    public Response getSyn(String strURl) throws Exception {
        long tag = System.currentTimeMillis();
        Request request = getRequest(strURl, tag);
        return httpClient.newCall(request).execute();
    }

    /**
     * 异步get请求
     */
    private void getAsny(String strURl, OkClientCallback callback) throws Exception {

        long tag = System.currentTimeMillis();
        Request request = getRequest(strURl, tag);
        callback.onStart(request);

        Call call = httpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 异步post请求
     */
    private void postRequest(String strUrl, String strJson, int connTime, int writeTimeout, int readTimeout, OkClientCallback callback) throws Exception {
        RequestBody body = RequestBody.create(jsonMT, strJson);

        long tag = System.currentTimeMillis();
        Request request = new Request.Builder().url(strUrl).post(body).tag(tag).build();
        callback.onStart(request);

        if (writeTimeout == 0)
            writeTimeout = WRITE_TIMEOUT;
        if (readTimeout == 0)
            readTimeout = READ_TIMEOUT;
        if (connTime == 0)
            connTime = WRITE_TIMEOUT + READ_TIMEOUT;

        httpClient = new OkHttpClient.Builder()
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .connectTimeout(connTime, TimeUnit.SECONDS)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 同步post请求
     */
    public Response postRequestSyn(String strUrl, String strJson, int connTime, int writeTimeout, int readTimeout) throws Exception {
        RequestBody body = RequestBody.create(jsonMT, strJson);

        long tag = System.currentTimeMillis();
        Request request = new Request.Builder().url(strUrl).post(body).tag(tag).build();

        if (writeTimeout == 0)
            writeTimeout = WRITE_TIMEOUT;
        if (readTimeout == 0)
            readTimeout = READ_TIMEOUT;
        if (connTime == 0)
            connTime = WRITE_TIMEOUT + READ_TIMEOUT;

        return httpClient.newCall(request).execute();
    }

    /**
     * 获取键值对Request
     */
    private Request getPostRequest(String postUrl, Map<String, String> params, Map<String, String> headers) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> m : params.entrySet()) {
                String strKey = m.getKey();
                String value = m.getValue();
                builder.add(strKey, value);
            }
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                requestBuilder.addHeader(header.getKey(), header.getValue());
            }
        }
        return requestBuilder.url(postUrl).post(builder.build()).build();
    }

//    /**
//     * 获取图片对Request
//     */
//    private Request getPostFileRequest(String strUrl, Map<String, File> fileMap, Map<String, String> map) throws Exception {
//        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
//        if (map != null) {
//            for (Map.Entry<String, String> m : map.entrySet()) {
//                String strKey = m.getKey();
//                String value = m.getValue();
//                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + strKey + "\""),
//                        RequestBody.create(null, value));
//            }
//        }
//        if (fileMap != null) {
//            RequestBody fileBody;
//            for (Map.Entry<String, File> m : fileMap.entrySet()) {
//                String strKey = m.getKey();
//                File file = m.getValue();
//                String fileName = file.getName();
//                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
//                builder.addPart(Headers.of("Content-Disposition",
//                        "form-data; name=\"" + strKey + "\"; filename=\"" + fileName + "\""), fileBody);
//            }
//        }
//        return new Request.Builder().url(strUrl).post(builder.build()).build();
//    }

    private String guessMimeType(String path) throws Exception {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

//    /**
//     * 异步post上传文件
//     */
//    private void postFile(String strUrl, Map<String, File> fileMap, Map<String, String> map, OkClientCallback callback) throws Exception {
//        Request request = getPostFileRequest(strUrl, fileMap, map);
//        callback.onStart(request);
//        Call call = httpClient.newCall(request);
//        call.enqueue(callback);
//    }

    /**
     * 异步post上传键值对
     */
    private void postNameAndValue(String strUrl, Map<String, String> param, OkClientCallback callback,
                                  Map<String, String> header, int writeTimeout, int readTimeout, int connTime) throws Exception {
        Request request = getPostRequest(strUrl, param, header);
        callback.onStart(request);
        if (writeTimeout == 0)
            writeTimeout = WRITE_TIMEOUT;
        if (readTimeout == 0)
            readTimeout = READ_TIMEOUT;
        if (connTime == 0)
            connTime = WRITE_TIMEOUT + READ_TIMEOUT;

        if (httpClient.writeTimeoutMillis() != writeTimeout * 1000) {
            httpClient = new OkHttpClient.Builder()
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .connectTimeout(connTime, TimeUnit.SECONDS)
                    .build();
        }

        Call call = httpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * get请求
     */
    public void sendGet(String strUrl, OkClientCallback callback) throws Exception {
        getAsny(strUrl, callback);
    }

    /**
     * post json请求
     */
    public void sendPost(String strUrl, String strJson, OkClientCallback callback) throws Exception {
        postRequest(strUrl, strJson, CONN_TIMEOUT, WRITE_TIMEOUT, READ_TIMEOUT, callback);
    }

    /**
     * post json请求
     *
     * @param writeTimeout 写入超时时间(秒)
     * @param readTimeout  读取超时时间(秒)
     */
    public void sendPost(String strUrl, String strJson, int connTimeout, int writeTimeout, int readTimeout, OkClientCallback callback) throws Exception {
        postRequest(strUrl, strJson, connTimeout, writeTimeout, readTimeout, callback);
    }

//    /**
//     * post文件请求
//     */
//    public void sendPostFile(String strUrl, Map<String, File> fileMap, Map<String, String> map, OkClientCallback callback) throws Exception {
//        postFile(strUrl, fileMap, map, callback);
//    }

    /**
     * post键值对
     */
    public void sendPostNameValuePairs(String strUrl, Map<String, String> map, OkClientCallback callback, Map<String, String> headers) throws Exception {
        postNameAndValue(strUrl, map, callback, headers, 0, 0, 0);
    }

    /**
     * post键值对
     */
    public void sendPostNameValuePairs(String strUrl, Map<String, String> map, OkClientCallback callback, Map<String, String> headers, int write, int read, int conn) throws Exception {
        postNameAndValue(strUrl, map, callback, headers, write, read, conn);
    }


}
