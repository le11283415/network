package babytree.com.baselib.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import babytree.com.baselib.R;

public class UiUtil {

    private static Toast mToast;
    private static TextView mTv_msg;
    private static TextView mTv_desc;
    /**
     * toast新增的标题图标
     */
    private static ImageView mImg_title;

    /**
     * 该方法用来关闭相应VIEW的硬件加速渲染，因为小米手机在硬件加速开启的情况下，WEBVIEW和动画一起渲染有问题
     *
     * @param view view
     */
    public static void closeHardwareAccelerated(View view) {
        if (Build.MANUFACTURER.equals("Xiaomi")) {
            if (null != view) {
                view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return formatter.format(date);
    }

    public static boolean isChinese(String s) {
        String regex = "[\\\\u4e00-\\\\u9fa5]{1,8}";
        return s.matches(regex);
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2017-06-14 16-09"）返回时间戳
     */
    public static Long getLongData(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date date;
        String times = null;
        String stf = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            stf = String.valueOf(l);
            // times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.parseLong(stf);
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, null);
    }

    public static void showToast(String text) {
        showToast(CoreUtils.getApp(), text);
    }

    /**
     * 显示 Toast 的便捷方法
     *
     * @param text 提示的信息
     * @param desc 详细的描述，如果不想设置该项可以传 null 或者调用 {@link #showToast(Context, String)}
     */
    public static void showToast(Context context, String text, String desc, @DrawableRes int imgResId) {
        if (mToast != null) {
            if (!TextUtils.isEmpty(desc)) {
                mTv_desc.setVisibility(View.VISIBLE);
                mTv_desc.setText(desc);
            } else {
                mTv_desc.setVisibility(View.GONE);
            }
            // 设置标题图标
            if(-1 != imgResId){
                mImg_title.setImageResource(imgResId);
                mImg_title.setVisibility(View.VISIBLE);
            }else{
                mImg_title.setVisibility(View.GONE);
            }
            mTv_msg.setText(text);
            mToast.show();
        } else {
            mToast = new Toast(context.getApplicationContext());
            View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.toast_layout, null);
            mTv_msg = (TextView) view.findViewById(R.id.text_message);
            mTv_desc = (TextView) view.findViewById(R.id.text_description);
            mImg_title = view.findViewById(R.id.img_title);
            if (!TextUtils.isEmpty(desc)) {
                mTv_desc.setVisibility(View.VISIBLE);
                mTv_desc.setText(desc);
            }
            if(-1 != imgResId){
                mImg_title.setVisibility(View.VISIBLE);
                mImg_title.setImageResource(imgResId);
            }
            mTv_msg.setText(text);
            mToast.setView(view);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }

    /**
     * 显示 Toast 的便捷方法
     *
     * @param text 提示的信息
     * @param desc 详细的描述，如果不想设置该项可以传 null 或者调用 {@link #showToast(Context, String)}
     */
    public static void showToast(Context context, String text, String desc) {
        showToast(context, text, desc, -1);
    }

    public static void showToast(String text, @DrawableRes int imgResId){
        showToast(CoreUtils.getAppContext(), text, null, imgResId);
    }

    public static void showToast(String text, String desc, @DrawableRes int imgResId){
        showToast(CoreUtils.getAppContext(), text, desc, imgResId);
    }

    public static void showToast(String text, String desc) {
        showToast(CoreUtils.getApp(), text, desc);
    }

    public static void showToast(@StringRes int resId){
        showToast(CoreUtils.getApp(), resId);
    }

    public static void showToast(Context context,
            @StringRes
                    int resId) {
        showToast(context, context.getString(resId), null);
    }

    /**
     * 显示 Toast 的便捷方法
     *
     * @param msgResId  提示的信息的资源 id
     * @param descResId 详细的描述资源 id ，如果不想设置该项可以传 null 或者调用 {@link #showToast(Context, int)}
     */
    public static void showToast(Context context,
            @StringRes
                    int msgResId,
            @StringRes
                    int descResId) {
        showToast(context, context.getString(msgResId), context.getString(descResId));
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
            mTv_desc = null;
            mTv_msg = null;
        }
    }

    public static String getText(EditText v) {
        if (null != v) {
            return v.getText().toString();
        }
        return null;
    }

    /**
     * 检查密码的合法性，8-20个字符，数字、大小写字母
     */
    public static boolean isPasswordValids(String psw) {
        String regex = "^[a-zA-Z0-9]{8,20}$";
        return psw.matches(regex);
    }

    /**
     * 是否为电话号码
     */
    public static boolean isPhoneNum(String phone) {
        String regex = "^1[3|4|5|7|8|][0-9]{9}$";
        boolean matches = phone.matches(regex);
        return matches;
    }

    /** 检查密码的合法性，6-12个字符，需包含字母和数字 */
    public static boolean isPasswordValid(String psw) {
        boolean isDigit = false;
        boolean isLetter = false;
        for (int i = 0; i < psw.length(); i++) {
            if (Character.isDigit(psw.charAt(i))) {
                isDigit = true;
            }
            if (Character.isLetter(psw.charAt(i))) {
                isLetter = true;
            }
            if (isDigit && isLetter) {
                break;
            }
        }
        String regex = "^[a-zA-Z0-9]{8,20}$";
        boolean isRight = isDigit && isLetter && psw.matches(regex);
        return isRight;
    }

    public static boolean isComparePwdSuccess(String pwd, String confirmPwd) {
        if (pwd.equals(confirmPwd)) {
            return true;
        }
        return false;
    }

    public static boolean comparePwd(String pwd1, String pwd2) {
        if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            return false;
        }
        if (pwd1.equals(pwd2)) {
            return true;
        }
        return false;
    }

    public static boolean isIdCard(String idCardNum) {
        if (TextUtils.isEmpty(idCardNum)) {
            return false;
        }
        String regex
                = "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))((0[1-9]|[1|2]\\d)|3[0-1])("
                + "(\\d{4})|\\d{3}[Xx]))$";
        return idCardNum.matches(regex);
    }

    /**
     * 判断是否有新版本
     *
     * @param v1 当前版本
     * @param v2 本地版本
     * @return 是否有新版本
     */
    public static boolean compareVersion(String v1, String v2) {
        String[] v1s = v1.split("\\.");
        String[] v2s = v2.split("\\.");
        int n = Math.min(v1s.length, v2s.length);
        for (int i = 0; i < n; ++i) {
            int v1Value = 0, v2Value = 0;
            try {
                v1Value = Integer.valueOf(v1s[i]);
                v2Value = Integer.valueOf(v2s[i]);
            } catch (NumberFormatException e) {
            }

            if (v2Value > v1Value) {
                return true;
            }else if(v1Value > v2Value) {
                return false;
            }

        }
        return false;
    }

    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.trim();
        } catch (PackageManager.NameNotFoundException var2) {
            var2.printStackTrace();
            return "";
        }
    }

    public interface OnButtonActionListener {
        void actionPerformed();
    }

    public static String checkString(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }
}
