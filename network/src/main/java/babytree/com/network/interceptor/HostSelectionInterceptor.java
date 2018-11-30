package babytree.com.network.interceptor;


import android.text.TextUtils;
import java.io.IOException;

import babytree.com.network.XgNetWork;
import babytree.com.network.interfaces.INetExternalParams;
import babytree.com.network.utlis.HeaderKey;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huangwenqiang on 2017/5/11.
 * <p>
 * 切换域名。
 */
public class HostSelectionInterceptor implements Interceptor {
    private final INetExternalParams mParams;

    public HostSelectionInterceptor() {
        mParams = XgNetWork.get().externalParams();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (needChangeHost(request)) {
            HttpUrl changeUrl = HttpUrl.parse(mParams.httpSecondHost());
            HttpUrl.Builder builder = request.url().newBuilder()
                    .scheme(changeUrl.scheme())
                    .host(changeUrl.host())
                    .port(changeUrl.port());
            //只支持切换一次path，而且只有一级path
            if (changeUrl.pathSegments() != null && changeUrl.pathSegments().size() > 0) {
                String newPath = changeUrl.pathSegments().get(0);
                if (!TextUtils.isEmpty(newPath))
                    builder.setPathSegment(0, newPath);
            }

            HttpUrl newUrl = builder.build();
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }

        return chain.proceed(request);
    }

    /**
     * need change host
     *
     * @return true or false
     */
    private boolean needChangeHost(Request request) {
        return !TextUtils.isEmpty(request.header(HeaderKey.CHANGE_HOST));
    }


}
