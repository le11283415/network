package babytree.com.myprojecttest.interfaces;

import babytree.com.baselib.mvp.MvpView;
import babytree.com.myprojecttest.bean.Blog;
import babytree.com.myprojecttest.bean.DateListBean;
import babytree.com.myprojecttest.bean.NewsBean;

/**
 * Created by kangle on 2018/10/31.
 */
public interface IMainInterface extends MvpView {


    void showNews(NewsBean resultData);

    void showDataList(DateListBean resultData);

    void showBlogData(Blog data);
}
