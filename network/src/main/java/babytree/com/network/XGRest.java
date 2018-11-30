package babytree.com.network;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import babytree.com.network.converter.CommonConverterFactory;
import babytree.com.network.interceptor.HeaderInterceptor;
import babytree.com.network.interceptor.HostSelectionInterceptor;
import babytree.com.network.interceptor.MockHostInterceptor;
import babytree.com.network.interfaces.INetExternalParams;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by huangwenqiang on 2017/5/10.
 */
public class XGRest {


    private final static XGRest INSTANCE = new XGRest();

    public static XGRest getInstance(){
        return INSTANCE;
    }

    private final Retrofit mRetrofit;

    public XGRest() {

        final INetExternalParams networkParams = XgNetWork.get().externalParams();

        final OkHttpClient.Builder builder = generateDefaultOkHttpBuilder(networkParams.connectTimeOut());
//        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        final HostSelectionInterceptor hostSelectionInterceptor = new HostSelectionInterceptor();
        final MockHostInterceptor mockHostInterceptor = new MockHostInterceptor();
        final HeaderInterceptor headerInterceptor = new HeaderInterceptor();
//        if (networkParams.isRelease()) {
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        } else {
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        }
        builder.addInterceptor(hostSelectionInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(mockHostInterceptor);
//                .addNetworkInterceptor(loggingInterceptor);

        List<Interceptor> interceptorList = XgNetWork.get().interceptors();
        for (Interceptor interceptor : interceptorList) {
            builder.addInterceptor(interceptor);
        }

        List<Interceptor> networkInterceptorsList = XgNetWork.get().networkInterceptors();
        for (Interceptor interceptor : networkInterceptorsList) {
            builder.addNetworkInterceptor(interceptor);
        }

        final OkHttpClient client = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkParams.httpHost())
                .addConverterFactory(CommonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .validateEagerly(true)
                .build();
    }

    @SuppressWarnings("all")
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    private static final int CONNECT_TIMEOUT = 15;
    private static final int READ_TIMEOUT = 15;

    private static Cache getCache() {
        //缓存文件夹
        File cacheFile = new File(XgNetWork.get().mApplicationContext.getExternalCacheDir().toString(), "xghl/reponse");
        //缓存大小为200M
        int cacheSize = 100 * 1024 * 1024;
        //创建缓存对象
        return new Cache(cacheFile, cacheSize);
    }

    private static OkHttpClient.Builder generateDefaultOkHttpBuilder(int timeOut) {

        return new OkHttpClient.Builder()
//                .cache(getCache())
                .writeTimeout(timeOut == 0 ? CONNECT_TIMEOUT :timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut == 0 ? CONNECT_TIMEOUT :timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut == 0 ? CONNECT_TIMEOUT :timeOut, TimeUnit.SECONDS);
    }
}
