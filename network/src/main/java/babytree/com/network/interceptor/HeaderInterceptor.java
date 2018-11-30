package babytree.com.network.interceptor;


import java.io.IOException;
import java.util.Map;

import babytree.com.network.XgNetWork;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Add Header for request
 * Created by huangwenqiang on 2017/10/20.
 */
public class HeaderInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Map<String, String> headers = XgNetWork.get().headers();
        Request.Builder builder = request.newBuilder();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }

        return chain.proceed(builder.build());
    }
}
