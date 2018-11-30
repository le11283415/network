package babytree.com.baselib.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;


import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 骑手工具类
 */
@SuppressWarnings("unused")
public class Util {

    /**
     * 判断某个界面是否在前台
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName componentName = list.get(0).topActivity;
            String topClassName = componentName.getShortClassName();
            int last = topClassName.lastIndexOf('.');
            if (last != -1) {
                topClassName = topClassName.substring(last+1, topClassName.length());
            }
            if (!TextUtils.isEmpty(topClassName) &&
                    className.equals(topClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据后台传过来的长宽比例参数来设置显示图片的长宽值，margin表示图片两侧的空白值
     *
     * @param pictureNum
     * @param margin
     * @param fractor
     * @param context
     * @return
     */
    public static int[] getPitcureSize(double pictureNum, int margin, double fractor, Context context) {
        if (fractor == 0) fractor = 1;
        int[] pitcureSize = new int[2];
        pitcureSize[0] = (int) ((ScreenUtil.getScreenWidth(context) - (pictureNum + 1) * margin) / pictureNum);
        pitcureSize[1] = (int) (pitcureSize[0] / fractor);
        return pitcureSize;
    }

    /**
     * 根据后台传过来的长宽比例参数来设置显示图片的长宽值，margin表示图片两侧的空白值
     *
     * @param pictureNum
     * @param margin
     * @param fractor
     * @param width
     * @return
     */
    public static int[] getPitcureSize(double pictureNum, int margin, double fractor, int width) {
        if (fractor == 0) fractor = 1;
        int[] pitcureSize = new int[2];
        pitcureSize[0] = (int) ((width - (pictureNum + 1) * margin) / pictureNum);
        pitcureSize[1] = (int) (pitcureSize[0] / fractor);
        return pitcureSize;
    }

    /**
     * 将毫秒时间转换
     * @param milSecond
     * @return　分？秒　只有相应值不为零才显示相应字段
     */
    public static String translateMilSecond(long milSecond) {
        int ss = 1000;
        int mi = ss * 60;
        StringBuilder sb = new StringBuilder();

        long minute = milSecond  / mi;
        long second = (milSecond - minute * mi) / ss;
        sb.append(minute>0 ? minute+"分" : "");
        sb.append(second > 0 ? second + "秒" : "");


        return sb.toString() ;
    }
    /**
     * 将秒时间转换
     * @param second
     * @return　分？秒　只有相应值不为零才显示相应字段
     */
    public static String translateSecond(long second) {
        int mi = 60;
        StringBuilder sb = new StringBuilder();

        long minute = second  / mi;
        long sec = (second - minute * mi);
        sb.append(minute>0 ? minute+"分" : "");
        sb.append(second > 0 ? sec + "秒" : "");


        return sb.toString() ;
    }

    /**
     * Url 解析
     * @param str
     * @return
     */
    public static HashMap<String, String> parseUrlScheme(String str) {
        HashMap<String, String> map = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(str, "&", false);

        while (st.hasMoreElements()) {
            String ss = st.nextToken();
            if (ss.contains("=")) {
                StringTokenizer st1 = new StringTokenizer(ss, "=", false);
                String k = st1.nextToken();
                String v = st1.nextToken();
                map.put(k, v);
            }
        }
        return map;
    }

    /** 电话号码是否为空 */
    public static boolean isTelEmpty(String tel) {
        if (TextUtils.isEmpty(tel)) {
            return true;
        }
        return tel.equals("-");
    }

    /** 检查密码的合法性，6-12个字符，需包含字母和数字 */
    public static boolean isPasswordValid(String psw) {
        boolean isDigit = false;
        boolean isLetter = false;
        for(int i = 0; i < psw.length(); i++) {
            if(Character.isDigit(psw.charAt(i))){
                isDigit = true;
            }
            if(Character.isLetter(psw.charAt(i))){
                isLetter = true;
            }
            if (isDigit && isLetter) {
                break;
            }
        }
//        String regex = "^[a-zA-Z0-9]+$";
        String regex = "^[a-zA-Z0-9]{6,12}$";
        boolean isRight = isDigit && isLetter && psw.matches(regex);
        return isRight;
    }

    /**
     * 判断Intent是否可用
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;

    }

}
