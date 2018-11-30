package babytree.com.network.interceptor;


import android.text.TextUtils;

import java.io.IOException;
import java.util.List;

import babytree.com.network.XgNetWork;
import babytree.com.network.interfaces.INetExternalParams;
import babytree.com.network.utlis.HeaderKey;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huangwenqiang on 2017/7/7.
 * <p>
 * 切换为mock域名。
 */
public class MockHostInterceptor implements Interceptor {
    private final INetExternalParams mParams;

    public MockHostInterceptor() {
        mParams = XgNetWork.get().externalParams();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            if (needChange2Mock(request) && !mParams.isRelease()) {
                HttpUrl changeUrl = HttpUrl.parse(mParams.mockHost());

                List<String> originPaths = request.url().pathSegments(); //请求里的多级path
                List<String> mockPaths = changeUrl.pathSegments();

                HttpUrl.Builder builder = request.url().newBuilder()
                        .scheme(changeUrl.scheme())
                        .host(changeUrl.host())
                        .port(changeUrl.port());

                //由于mock测试还需要加上path “/mockjsdata/14554/”
                if (originPaths != null && originPaths.size() > 0) {
                    int count = originPaths.size();
                    for (int i = 0; i < count; i++) {
                        builder.removePathSegment(0);
                    }
                }


                for (String path : mockPaths) {
                    builder.addPathSegments(path);
                }

                for (String path : originPaths) {
                    builder.addPathSegments(path);
                }


                HttpUrl newUrl = builder.build();
                request = request.newBuilder()
                        .url(newUrl)
                        .build();
            }
        } catch (Exception e) {

        }

        return chain.proceed(request);
    }

    /**
     * 切换为mock的域名
     *
     * @return true or false
     */
    private boolean needChange2Mock(Request request) {
        return !TextUtils.isEmpty(request.header(HeaderKey.MOCK_HOST));
    }


}
