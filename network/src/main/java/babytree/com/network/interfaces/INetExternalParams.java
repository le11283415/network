package babytree.com.network.interfaces;

import android.support.annotation.NonNull;

/**
 * Created by huangwenqiang on 2017/5/10.
 */
public interface INetExternalParams {

    String getUserId();

    String getAppVersion();

    // App is Release version
    boolean isRelease();
    // app 主域名
    @NonNull String httpHost();

    //最多只支持切换一个域名 副域名
    @NonNull String httpSecondHost();

    //提供mock的域名
    @NonNull String mockHost();

    int connectTimeOut();
}
