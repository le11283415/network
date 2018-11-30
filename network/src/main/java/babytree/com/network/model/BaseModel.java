package babytree.com.network.model;

import com.google.gson.JsonElement;

import babytree.com.network.global.Constant;

/**
 * Created by Xiaogai on 2017/4/19.
 */

public class BaseModel {

    public String resultCode;
    public String resultDesc;
    public JsonElement resultData;

    public boolean isSuccess() {
        return resultCode.equals("0");
    }

    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return !(String.valueOf(Constant.WEB_RESP_CODE_SUCCESS).equals(resultCode));
    }
}
