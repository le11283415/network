package babytree.com.network.exception;

/**
 * Created by Administrator on 2016/12/1.
 */
public class ApiException extends RuntimeException {

    public final String mMessage;
    public final String mCode;
    //用于展示的异常信息


    public ApiException(String code,String message) {
        this.mCode = code;
        this.mMessage = message;
    }



}
