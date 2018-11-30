package babytree.com.network.exception;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import com.google.gson.JsonParseException;

import android.net.ParseException;

import babytree.com.network.utlis.ContextUtil;
import babytree.com.network.utlis.NetworkUtil;
import retrofit2.HttpException;

/**
 * Created by Tamic on 2016-08-12.
 */
public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络错误";
                    break;
            }
            return ex;
        }else if (e instanceof ApiException) { //业务异常
            ApiException apiException = (ApiException) e;
            try {
                ex = new ResponseThrowable(e, Integer.parseInt(apiException.mCode));
                ex.message = apiException.mMessage;
                return ex;
            }catch (Exception ee){
                ex = new ResponseThrowable(e,ERROR.SERVER_ERROR);
                ex.message = "服务器异常";
                return ex;
            }
        }
        else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "数据错误";
            return ex;
        } else if (e instanceof ConnectException) {
            if(!NetworkUtil.isNetworkAvailable(ContextUtil.getContext())) {
                ex = new ResponseThrowable(e, ERROR.NETWORK_ERROR);
                ex.message = "网络错误，请检查网络";
            }else {
                ex = new ResponseThrowable(e, ERROR.NETWORK_ERROR);
                ex.message = "网络错误，请检查网络";
            }
            return ex;
        } else if( e instanceof UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.NETWORK_ERROR);
            ex.message = "网络错误，请检查网络";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException){
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络超时，请稍后再试";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络超时，请稍后再试";
            return ex;
        }else if(e instanceof SocketException) {
            ex = new ResponseThrowable(e, ERROR.NET_INTERCEPT);
            ex.message = "网络中断";
            return ex;
        } else if(e instanceof IOException) {
            ex = new ResponseThrowable(e, ERROR.NET_ERROR);
            ex.message = "网络错误";
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }


    /**
     * 除解析异常外，其他异常都可认为是网络异常，当然包括了一些服务器异常
     * 约定异常
     */
    public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
        /**
         * 网络无连接
         */
        public static final int NET_NO_CONNECT = 1007;

        /**
         * 服务器异常
         */
        public static final int SERVER_ERROR = 1008;

        /**
         * 服务器异常
         */
        public static final int NET_INTERCEPT = 1009;

        /**
         * 网络错误
         */
        public static final int NET_ERROR = 1010;

    }

    public static class ResponseThrowable extends Exception {
        public int code;
        public String message;

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;

        }
    }

    public class ServerException extends RuntimeException {
        public int code;
        public String message;
    }
}

