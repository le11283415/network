package babytree.com.network.model;


import babytree.com.network.global.Constant;

/**
 * Created by mark on 17-4-15.
 */

public class BaseResponse<T> {
    public String resultCode;
    public String resultDesc;
    public T resultData;
    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return !(String.valueOf(Constant.WEB_RESP_CODE_SUCCESS).equals(resultCode));
    }
}
