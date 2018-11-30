package babytree.com.baselib.rxjava;


import android.support.annotation.NonNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by huangwenqiang on 2017/5/16.
 *  非MVP模式使用
 *  * 目地：1、统一处理异常
 *         2、自动回调显示和隐藏loading dialog
 */

public abstract class XgSubscriberNoMvp<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }
}
