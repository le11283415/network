package babytree.com.network;

import android.content.Context;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babytree.com.network.interfaces.INetExternalParams;
import okhttp3.Interceptor;

/**
 * Created by huangwenqiang on 2017/5/10.
 */

public class XgNetWork {

    private static XgNetWork sNetwork;
    private final Map<String, String> mExtraHeaders;
    private final INetExternalParams mExternalParams;
    private final List<Interceptor> networkInterceptors;
    private final List<Interceptor> interceptors;
    public final Context mApplicationContext;

    private XgNetWork(Context context, INetExternalParams params,
                      Map<String, String> extraHeaders,
                      List<Interceptor> networkInterceptors, List<Interceptor> interceptors) {
        this.mApplicationContext = context;
        this.mExternalParams = params;
        this.mExtraHeaders = extraHeaders;
        this.networkInterceptors = networkInterceptors;
        this.interceptors = interceptors;
    }


    public static void init(XgNetWork xgNetwork) {
        if (xgNetwork == null) {
            throw new RuntimeException("Please using XKNetwork.Builder(context).build() to init XKNetwork");
        }
        sNetwork = xgNetwork;
    }


    public static XgNetWork get() {
        if (sNetwork == null) {
            throw new RuntimeException("Please using XgNetWork.init() in Application first");
        }
        return sNetwork;

    }


    public INetExternalParams externalParams() {
        return mExternalParams;
    }

    public List<Interceptor> networkInterceptors() {
        return networkInterceptors;
    }

    public List<Interceptor> interceptors() {
        return interceptors;
    }


    public Map<String, String> headers() {
        Map<String, String> map = new HashMap<>();
//        map.put("os", "android");
//        map.put("version", mExternalParams.getAppVersion());
//        final String userId = mExternalParams.getUserId();
//        if (!TextUtils.isEmpty(userId)) {
//            map.put("userId", userId);
//        }
//        map.put("User-Agent", "(" + Build.MODEL + ";Android " + Build.VERSION.RELEASE + ";) deviceID/");
        if (mExtraHeaders != null) {
            map.putAll(mExtraHeaders);
        }
        return map;
    }

    private static void checkNotNull(Object object, String err) {
        if (object == null) {
            throw new IllegalArgumentException(err + " can not be null !");
        }
    }

    public static final class Builder {
        private Context context;
        private Map<String, String> extraHeaders;
        private INetExternalParams networkParams;
        private List<Interceptor> networkInterceptors;
        private List<Interceptor> interceptors;
        private int timeOut;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder extraHeaders(Map<String, String> extraHeaders) {
            this.extraHeaders = extraHeaders;
            return this;
        }

        public Builder externalParams(INetExternalParams params) {
            this.networkParams = params;
            return this;
        }

        public Builder networkInterceptors(List<Interceptor> networkInterceptors) {
            this.networkInterceptors = networkInterceptors;
            return this;
        }

        public Builder interceptors(List<Interceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }


        public XgNetWork build() {
            checkNotNull(context, "context");
            checkNotNull(networkParams, "INetExternalParams");

            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }

            if (networkInterceptors == null) {
                networkInterceptors = new ArrayList<>();
            }

            return new XgNetWork(context, networkParams, extraHeaders, networkInterceptors, interceptors);
        }

    }
}
