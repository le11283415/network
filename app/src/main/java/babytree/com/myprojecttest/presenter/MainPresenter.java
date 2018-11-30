package babytree.com.myprojecttest.presenter;

import babytree.com.baselib.mvp.BasePresenter;
import babytree.com.baselib.rxjava.XgSchedulerApplier;
import babytree.com.baselib.rxjava.XgSubscriber;
import babytree.com.myprojecttest.bean.Blog;
import babytree.com.myprojecttest.bean.NewsBean;
import babytree.com.myprojecttest.interfaces.IMainInterface;
import babytree.com.myprojecttest.net.Inetwork;
import babytree.com.network.XGRest;
import babytree.com.network.exception.ExceptionHandle;

/**
 * Created by kangle on 2018/10/30.
 */
public class MainPresenter extends BasePresenter<IMainInterface> {


    public void getBlogList() {
        XGRest.getInstance().create(Inetwork.class).getBlogs().
                compose(XgSchedulerApplier.<Blog>DefaultSchedulers()).
                subscribe(new XgSubscriber<Blog>(this) {
                    @Override
                    public boolean onFailed(ExceptionHandle.ResponseThrowable responeThrowable) {
                        getMvpView().showToast(responeThrowable.message);
                        return false;
                    }

                    @Override
                    public void onSuccess(Blog listBaseResponse) {
                        getMvpView().showBlogData(listBaseResponse);
                    }
                });
    }

    public void getDateNews() {
        XGRest.getInstance().create(Inetwork.class).getNews().
                compose(XgSchedulerApplier.<NewsBean>DefaultSchedulers()).
                subscribe(new XgSubscriber<NewsBean>(this) {
                    @Override
                    public boolean onFailed(ExceptionHandle.ResponseThrowable responeThrowable) {
                        getMvpView().showToast(responeThrowable.message);
                        return false;
                    }

                    @Override
                    public void onSuccess(NewsBean newsBeanBaseResponse) {
                        getMvpView().showNews(newsBeanBaseResponse);
                    }

                });
    }


}
