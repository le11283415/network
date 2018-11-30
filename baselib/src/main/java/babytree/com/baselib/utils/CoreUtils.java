package babytree.com.baselib.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by kangle on 2018/10/31.
 */
public class CoreUtils {

    private static Application mApp;

    public static void init(Application application){
        mApp = application;
    }

    public static Application getApp(){
        return mApp;
    }

    public static Context getAppContext(){
        return mApp.getApplicationContext();
    }

}
