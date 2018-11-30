package babytree.com.myprojecttest.activity;

import android.util.Log;
import android.view.View;

import babytree.com.baselib.activity.BaseBindPresenterActivity;
import babytree.com.myprojecttest.R;
import babytree.com.myprojecttest.bean.Blog;
import babytree.com.myprojecttest.bean.DateListBean;
import babytree.com.myprojecttest.bean.NewsBean;
import babytree.com.myprojecttest.interfaces.IMainInterface;
import babytree.com.myprojecttest.presenter.MainPresenter;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseBindPresenterActivity<MainPresenter> implements IMainInterface {

    public static final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter mainPresenter;

    @Override
    protected int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initActivity(View pView) {
        ButterKnife.bind(this, pView);
        mainPresenter = new MainPresenter();
    }

    @Override
    public MainPresenter getPresenter() {
        return mainPresenter;
    }


    @Override
    public void showNews(NewsBean resultData) {
        if(resultData != null) {
            Log.e(TAG,resultData.toString());
        }

    }

    @Override
    public void showDataList(DateListBean resultData) {
        if(resultData != null) {
            Log.e(TAG,resultData.toString());
        }
    }

    @Override
    public void showBlogData(Blog data) {
        showToast("获取数据成功");
    }


    @OnClick({R.id.btn_get_data, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_data:
                mainPresenter.getBlogList();
                break;
            case R.id.button:
                mainPresenter.getDateNews();
                break;
        }
    }

}
