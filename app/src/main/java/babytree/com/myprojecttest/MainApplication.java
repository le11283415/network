package babytree.com.myprojecttest;

import android.app.Application;
import android.support.annotation.NonNull;

import babytree.com.baselib.utils.CoreUtils;
import babytree.com.network.XgNetWork;
import babytree.com.network.interfaces.INetExternalParams;

/**
 * Created by kangle on 2018/10/30.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CoreUtils.init(this);
        XgNetWork.Builder builder = new XgNetWork.Builder(this);
        XgNetWork build = builder.externalParams(new INetExternalParams() {
            @Override
            public String getUserId() {
                return "";
            }

            @Override
            public String getAppVersion() {
                return "";
            }

            @Override
            public boolean isRelease() {
                return false;
            }

            @NonNull
            @Override
            public String httpHost() {
                return "http://www.baidu.com";
            }

            @NonNull
            @Override
            public String httpSecondHost() {
                return null;
            }

            @NonNull
            @Override
            public String mockHost() {
                return null;
            }

            @Override
            public int connectTimeOut() {
                return 0;
            }
        }).build();
        XgNetWork.init(build);

    }
}
