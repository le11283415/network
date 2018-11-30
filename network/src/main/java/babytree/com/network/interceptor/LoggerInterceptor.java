package babytree.com.network.interceptor;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Response.Builder;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by Administrator on 2017/4/19.
 */

public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public LoggerInterceptor(String tag) {
        this(tag, true);
    }

    public LoggerInterceptor(String tag, boolean showResponse) {
        if(tag == null || tag.isEmpty()) {
            tag = "OkHttpUtils";
        }

        this.showResponse = showResponse;
        this.tag = tag;
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        this.logForRequest(request);
        Response response = chain.proceed(request);
        return this.logForResponse(response);
    }

    private void logForRequest(Request request) {
        try {
            String e = request.url().toString();
            Headers headers = request.headers();
            System.out.println("url : " + e);
//            Log.e(this.tag, "---------------------request log start---------------------");
//            Log.e(this.tag, "method : " + request.method());
//            Log.e(this.tag, "url : " + e);
            if(headers != null && headers.size() > 0) {
//                Log.e(this.tag, "headers : \n");
//                Log.e(this.tag, "headers : \n");
            }

            RequestBody requestBody = request.body();
            if(requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if(mediaType != null) {
//                    Log.e(this.tag, "contentType : " + mediaType.toString());
                    System.out.println("contentType : " + mediaType.toString());
                    if(this.isText(mediaType)) {
//                        Log.e(this.tag, "content : " + this.bodyToString(request));
                        System.out.println("content : " + this.bodyToString(request));
                    } else {
//                        Log.e(this.tag, "content :  maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        } finally {
//            Log.e(this.tag, "---------------------request log end-----------------------");
        }

    }

    private Response logForResponse(Response response) {
        try {
//            Log.e(this.tag, "---------------------response log start---------------------");
            Builder e = response.newBuilder();
            Response clone = e.build();
//            Log.e(this.tag, "url : " + clone.request().url());
//            Log.e(this.tag, "code : " + clone.code());
//            Log.e(this.tag, "protocol : " + clone.protocol());
            if(clone.message() != null && !clone.message().isEmpty()) {
//                Log.e(this.tag, "message : " + clone.message());
                System.out.println("message : " + clone.message());
            }

            if(this.showResponse) {
                ResponseBody body = clone.body();
                if(body != null) {
                    MediaType mediaType = body.contentType();
                    if(mediaType != null) {
//                        Log.e(this.tag, "contentType : " + mediaType.toString());
                        if(this.isText(mediaType)) {
                            String resp = body.string();
                            System.out.println("content : " + resp);
//                            Log.e(this.tag, "content : " + resp);
                            body = ResponseBody.create(mediaType, resp);
                            Response var7 = response.newBuilder().body(body).build();
                            return var7;
                        }

//                        Log.e(this.tag, "content :  maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
//            Log.e(this.tag, "---------------------response log end-----------------------");
        }

        return response;
    }

    private boolean isText(MediaType mediaType) {
        return mediaType.type() != null && mediaType.type().equals("text")?true:mediaType.subtype() != null && (mediaType.subtype().equals("json") || mediaType.subtype().equals("xml") || mediaType.subtype().equals("html") || mediaType.subtype().equals("webviewhtml"));
    }

    private String bodyToString(Request request) {
        try {
            Request e = request.newBuilder().build();
            Buffer buffer = new Buffer();
            e.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException var4) {
            return "something error when show requestBody.";
        }
    }
}