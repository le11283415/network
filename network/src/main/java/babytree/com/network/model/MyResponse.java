package babytree.com.network.model;

import babytree.com.network.global.Constant;

/**
 * Created by kangle on 2018/11/10.
 */
public class MyResponse<T> {

    public String status;
    public String errMsg;
    public T data;
    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return !(String.valueOf(Constant.WEB_RESP_CODE_SUCCESS).equals(status));
    }

}
