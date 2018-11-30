package babytree.com.baselib.mvp;


/**
 * Created by huangwenqiang on 2017/5/11.
 */
public interface Presenter<V extends MvpView> {

    void attachView(V mMvpView);

    void detachView();
}
