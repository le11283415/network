package babytree.com.network.utlis;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Yefeng on 2017/4/13.
 * Modified by xxx
 */

public class UtilSetting {


    private static UtilSetting SharedPreferences = null;
    private static Object mLock = new Object();
    private android.content.SharedPreferences mSharedPreferences = null;
    private Context mContext;


    public static UtilSetting getInstance(Context context) {
        synchronized (mLock) {
            if (SharedPreferences == null) {
                SharedPreferences = new UtilSetting(context);
            }
            return SharedPreferences;
        }
    }

    private UtilSetting(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * 设置或者获取是否刷新了token
     */
    public void saveBoolean(boolean boo) {
        mSharedPreferences.edit().putBoolean("TOKENUPDATE", boo).apply();
    }

    public boolean saveBoolean() {
        return mSharedPreferences.getBoolean("TOKENUPDATE",false);
    }

    /** 存放token值 */
    public void putToken(String token) {
        mSharedPreferences.edit().putString("TOKEN", token).apply();
    }

    public String getToken() {
        return mSharedPreferences.getString("TOKEN", "");
    }

}
