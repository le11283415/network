package babytree.com.baselib.rxjava;

import android.support.annotation.NonNull;
import babytree.com.baselib.mvp.BasePresenter;
import babytree.com.baselib.mvp.MvpView;
import babytree.com.network.exception.ExceptionHandle;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by huangwenqiang on 2017/5/16.
 *
 *  * 目地：1、统一处理异常
 *         2、自动回调显示和隐藏loading dialog
 */

public abstract class XgSubscriber<T> implements Observer<T> {

    private BasePresenter mBasePresenter;
    private boolean mShowLoadingDialog = true;

    public XgSubscriber(BasePresenter pBasePresenter) {
        this.mBasePresenter = pBasePresenter;
    }

    public XgSubscriber(BasePresenter pBasePresenter, boolean pShowLoadingDialog) {
        this.mBasePresenter = pBasePresenter;
        this.mShowLoadingDialog = pShowLoadingDialog;
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (!isViewAttached() || !mShowLoadingDialog) {
            return;
        }
        if(getDialogMessage()!= 0){
            this.mBasePresenter.getMvpView().showWaiting(getDialogMessage());
        }else {
            this.mBasePresenter.getMvpView().showWaiting();
        }
    }

    /**
     * 默认为空或者默认展示为正在加载中
     * @return 需要现实菊花的信息
     */
    public int getDialogMessage() {
        return 0;
    }

    @Override
    public void onError(Throwable t) {
        if (!isViewAttached()) {
            return;
        }
        if(this.mBasePresenter.getMvpView() != null) {
            MvpView mvpView = this.mBasePresenter.getMvpView();
            mvpView.stopWaiting();
            ExceptionHandle.ResponseThrowable restError = ExceptionHandle.handleException(t);
            if(onFailed(restError)){
                mvpView.onExceptionHandle(restError);
            }
        }
    }

    @Override
    public void onNext(T t) {
        if (!isViewAttached()) {
            return;
        }
        if(this.mBasePresenter.getMvpView() != null) {
            onSuccess(t);
        }
    }

    @Override
    public void onComplete() {
        if (!isViewAttached() || !mShowLoadingDialog) {
            return;
        }
        this.mBasePresenter.getMvpView().stopWaiting();
    }

    private boolean isViewAttached() {
        return mBasePresenter !=null && mBasePresenter.isViewAttached();
    }

    /**
     * @param responeThrowable
     * @return false:表未异常不需要上层再处理(即不需要再回调onExceptionDispose方法)
     * true :此异常需要基类中的异常处理方案处理。（表示此异常没有被消费掉）
     */

    public abstract boolean onFailed(ExceptionHandle.ResponseThrowable responeThrowable);

    public abstract void onSuccess(T t);
}
