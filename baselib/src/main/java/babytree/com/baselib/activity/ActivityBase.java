package babytree.com.baselib.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import babytree.com.baselib.R;
import babytree.com.baselib.mvp.MvpView;
import babytree.com.baselib.utils.UiUtil;
import babytree.com.baselib.view.PageLoadingHelper;
import babytree.com.network.exception.ExceptionHandle;

/**
 * 全局Activity基类
 * Created by yefeng on 2017/04/07.
 * Modified by  yefeng
 */
public abstract class ActivityBase extends AppCompatActivity implements MvpView {

    private Toolbar mToolbar;
    private FrameLayout mContentContainer;
    private TextView mTitleBarTitle;
    private View mContentView;
    private View mTitleBarBack;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTitleBarRightText;
    private SparseArray<Dialog> dialogArrayMap;
    protected PageLoadingHelper mPageLoadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setComponent();
        super.onCreate(savedInstanceState);
        setContentView(getBaseContentLayoutResId());
        initBaseView();
        mPageLoadingHelper = new PageLoadingHelper(mContentContainer, this, mContentView);
        initBaseToolBar();
        initActivity(mContentView);
        initPresenter();
        showContent();
        initInstanceState(savedInstanceState);
    }

    protected int getBaseContentLayoutResId() {
        return useSwipeRefreshLayout() ?
                R.layout.activity_base_refresh_layout : R.layout.activity_base_layout;
    }

    protected void setRefreshComplete() {
        if (useSwipeRefreshLayout()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    /**
     * 初始化保存状态
     */
    protected void initInstanceState(Bundle savedInstanceState) {
    }

    private void initBaseView() {
        mContentContainer = (FrameLayout) super.findViewById(R.id.content_container);
        mContentView = getLayoutInflater().inflate(getContentLayoutResId(), mContentContainer, false);
        if (useSwipeRefreshLayout()) {
            mSwipeRefresh = (SwipeRefreshLayout) super.findViewById(R.id.refresh);
            mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    reLoadData();
                }
            });
        }
    }

    /**
     * 重写此方法  下拉刷新
     */
    public void reLoadData() {
    }

    /**
     * 如果之类想使用下来刷新功能，需要重写 返回true
     */
    public boolean useSwipeRefreshLayout() {
        return false;
    }

    @Override
    public View findViewById(@IdRes int id) {
        //  防止无法获取根布局内容
        if(android.R.id.content == id){
            return super.findViewById(id);
        }else{
            return mContentView.findViewById(id);
        }
    }

    private void initBaseToolBar() {
        mToolbar = (Toolbar) super.findViewById(R.id.toolbar);
        setNormalTitlebar();
    }

    protected abstract int getContentLayoutResId();

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * 初始化注入器
     */
    protected void setComponent() {
    }

    protected void initPresenter() {
    }

    protected abstract void initActivity(View pView);

    /**
     * 初始化标准的标题栏
     */
    public void setNormalTitlebar() {
        View titleBar = getLayoutInflater().inflate(R.layout.view_simple_title_bar, null);
        mTitleBarBack = titleBar.findViewById(R.id.titlebar_back);
        mTitleBarTitle = (TextView) titleBar.findViewById(R.id.titlebar_title);
        mTitleBarRightText = (TextView) titleBar.findViewById(R.id.titlebar_right_text);
        mTitleBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setCustomTitleBar(titleBar);
    }

    /**
     * 添加自定义标题栏
     */
    public void setCustomTitleBar(View titlebarLayout) {
        mToolbar.removeAllViews();  // 先清除掉之前可能加入的

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.MATCH_PARENT);//传入的布局，覆盖整个Toolbar
        mToolbar.addView(titlebarLayout, lp);
    }

    public void hideTitleBar() {
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 强制竖屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 设置标题
     */
    @Override
    public void setTitle(CharSequence title) {
        if (mTitleBarTitle != null) {
            mTitleBarTitle.setText(title);
        }
    }

    /**
     * 设置标题
     */
    @Override
    public void setTitle(int TitleId) {
        if (mTitleBarTitle != null) {
            mTitleBarTitle.setText(TitleId);
        }
    }

    /**
     * 设置右文本
     */
    public void setRightTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            mTitleBarRightText.setVisibility(View.VISIBLE);
            mTitleBarRightText.setText(title);
        }
    }

    /**
     * 设置右文本
     */
    public void showRightTitle(boolean isShow) {
        mTitleBarRightText.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    /**
     * 设置右文本点击事件
     */
    public void setRightTitleClick(View.OnClickListener onClickListener) {
        if (mTitleBarRightText != null) {
            mTitleBarRightText.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置右文本是否可以点击
     */
    public void setRightTitleEnable(boolean enable) {
        if (mTitleBarRightText != null) {
            mTitleBarRightText.setEnabled(enable);
        }
    }

    /**
     * Toast 提示
     */
    @Override
    public void showToast(int resid) {
        showToast(resid, Toast.LENGTH_SHORT);
    }

    /**
     * Toast 提示
     */
    @Override
    public void showToast(int resid, int duration) {
        UiUtil.showToast(this, resid);
    }

    /**
     * Toast 提示
     */
    @Override
    public void showToast(CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            UiUtil.showToast(this, message.toString());
        }

    }

    /**
     * Toast 提示
     */
    @Override
    public void showToast(CharSequence message, int duration) {
        if (!TextUtils.isEmpty(message)) {
            UiUtil.showToast(this, message.toString());
        }
    }

    @Override
    public void showMsg(int title, int des) {
        UiUtil.showToast(this, title, des);
    }

    /**
     * 显示等待框
     * 默认显示"加载中..."
     */
    @Override
    public void showWaiting() {
    }

    /**
     * 默认显示"加载中..."
     */
    @Override
    public void showWaiting(boolean instantShow) {
    }

    /**
     * 显示等待框
     * 默认显示"加载中..."
     */
    @Override
    public void showWaiting(int strId) {
        String message = getString(strId);
    }

    /**
     * 显示等待框
     */
    @Override
    public void showWaiting(int strId, boolean isCancelable) {
        String message = getString(strId);
    }

    /**
     * 显示等待框
     * 默认显示"加载中..."
     */
    @Override
    public void showWaiting(String message) {
    }

    /**
     * 显示等待框
     * 默认显示"加载中..."
     */
    @Override
    public void showWaiting(String message, boolean isCancelable) {
    }

    /**
     * 隐藏等待框
     */
    @Override
    public void stopWaiting() {
    }

    /**
     * 如果使用了下拉刷新，重新加载数据成功后需要调此方法
     */
    public void showContent() {
        mPageLoadingHelper.showContent();
        setRefreshComplete();
    }

    protected SwipeRefreshLayout getRefreshLayout() {
        return mSwipeRefresh;
    }

    /**
     * 设置是否显示Toolbar标题栏
     */
    public void setToolBarVisible(boolean is) {
        if (mToolbar == null) {
            return;
        }
        if (is) {
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.GONE);
        }
    }

    /**
     * 设置是否显示toolbar的返回按钮
     */
    public void setBackIconVisiable(boolean visiable) {
        mTitleBarBack.setVisibility(visiable ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 只有API大于16的才能够设置Toolbar的背景颜色
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setTitleBarBackgroundColor(int color) {
        if (mToolbar != null) {
            mToolbar.setBackgroundColor(color);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setTitleBarBackgroundColor(Drawable barBackgroundColor) {
        if (mToolbar != null) {
            mToolbar.setBackground(barBackgroundColor);
        }
    }

    public void toActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 显示或隐藏输入法
     */
    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != dialogArrayMap){
            dialogArrayMap.clear();
        }
    }

    @Override
    public void onExceptionHandle(ExceptionHandle.ResponseThrowable restError) {

    }

    @Override
    public void showErrorView() {
        setRefreshComplete();
        mPageLoadingHelper.showErrorView();
    }

    @Override
    public void showErrorView(ExceptionHandle.ResponseThrowable throwable) {
        setRefreshComplete();
        mPageLoadingHelper.showErrorView(throwable);
    }

    @Override
    public void showErrorView(@Nullable String pErrorMes, @DrawableRes int pErrorIconRes) {
        setRefreshComplete();
        mPageLoadingHelper.showErrorView(pErrorMes, pErrorIconRes);
    }

    @Override
    public void showErrorView(View pview) {
        setRefreshComplete();
        mPageLoadingHelper.showErrorView(pview);
    }

    @Override
    public void showPageInprossView() {
        mPageLoadingHelper.showInPageProgressView();
    }

    @Override
    public void showEmptyView(@DrawableRes int pEmptyIconRes, @Nullable String pEpmtyMes) {
        setRefreshComplete();
        mPageLoadingHelper.showEmptyView(pEmptyIconRes, pEpmtyMes);
    }

    @Override
    public void showEmptyView(@DrawableRes int pEmptyIconRes, @StringRes int pEpmtyMes) {
        setRefreshComplete();
        mPageLoadingHelper.showEmptyView(pEmptyIconRes, pEpmtyMes);
    }

    @Override
    public void showEmptyView() {
        setRefreshComplete();
        mPageLoadingHelper.showEmptyView();
    }
}
