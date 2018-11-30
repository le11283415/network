package babytree.com.network;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Yefeng on 2017/4/18.
 * Modified by xxx
 */

public class RxUtils {

    private final static ObservableTransformer schedulersTransformer = new  ObservableTransformer() {
        @Override
        public ObservableSource apply(
                @NonNull
                        Observable upstream) {
            return ((Observable)  upstream).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

    };

    public static  <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }

}
