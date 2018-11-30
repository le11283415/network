package babytree.com.baselib.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import babytree.com.baselib.R;
import babytree.com.baselib.activity.ActivityBase;
import babytree.com.baselib.mvp.MvpView;
import babytree.com.baselib.utils.ScreenUtil;
import babytree.com.baselib.utils.UiUtil;
import babytree.com.baselib.view.PageLoadingHelper;
import babytree.com.network.exception.ExceptionHandle;

/**
 * fragment基类
 * Created by yefeng on 2017/04/07.
 * Modified by yefeng
 * Modified by BoQin 添加Fragment是否可见方法
 */

public abstract class FragmentBase extends Fragment implements MvpView {

    public static final int RESULT_OK = 200;

    protected static final String Tag = "BaseFragment";
    protected boolean loaded = false;
    private SparseArray<Dialog> dialogArrayMap;
    private Context mContext;
    private ImageView mImageBack;
    private TextView mTextTitle;
    private TextView mRightText;
    private ImageView mRightIcon;
    private ViewGroup mTitleBar;
    private ImageView mMiddleRightIcon;
    private View mTitleBarLine;
    private ProgressBar mPBTitle;
    protected FrameLayout mContentContainer;
    protected LayoutInflater mInflater;

    /**
     * 在ViewPager中有效，标记当前 Fragment 是否处于可见状态，建议使用{@link #isFragmentVisible()}
     *
     * @see #isFragmentVisible()
     */
    public boolean mIsVisible;
    private boolean mIsVisibleToUser;
    /** 是否在前台，该状态在onStart和onStop更新 */
    private boolean mIsForeground;

    private PageLoadingHelper mPageLoadingHelper;
    private View mContentView;
    private SwipeRefreshLayout mSwipeRefresh;
    public FragmentActivity _mActivity;
    private View mRootView;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setComponent();
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        _mActivity = getActivity();
    }

    protected void setComponent() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initBaseView(inflater, container);
        initBaseToolBar();
        initPresenter();
        initFragment(mContentView);
        initSavadInstance(savedInstanceState);
        mContext = _mActivity;
        showContent();
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsForeground = true;
    }

    protected void setRefreshComplete() {
        if (useSwipeRefreshLayout()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    protected SwipeRefreshLayout getRefreshLayout() {
        return mSwipeRefresh;
    }

    protected void setRefreshStart() {
        if (useSwipeRefreshLayout()) {
            mSwipeRefresh.setRefreshing(true);
        }
    }

    private View initBaseView(LayoutInflater pInflater, ViewGroup pContainer) {
        mInflater = pInflater;
        mRootView = pInflater
                .inflate(useSwipeRefreshLayout() ? R.layout.fragment_base_refresh_layout : R.layout.fragment_base_layout, pContainer, false);
        mContentContainer = mRootView.findViewById(R.id.content_container);

        mContentView = mInflater.inflate(getLayoutId(), mContentContainer, false);
        mPageLoadingHelper = new PageLoadingHelper(mContentContainer, this, mContentView);

        if (useSwipeRefreshLayout()) {
            mSwipeRefresh = mRootView.findViewById(R.id.refresh);
            mSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(_mActivity, R.color.core_color_ffd800));
            mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    reLoadData();
                }
            });
        }

        return mRootView;
    }

    public void setReloadTitle(String msg) {
        mTextTitle.setText(msg);
        mPBTitle.setVisibility(View.VISIBLE);
    }

    /**
     * 重写此方法  下拉刷新
     */
    public void reLoadData() {
        setRefreshStart();
    }

    /**
     * 如果之类想使用下来刷新功能，需要重写 返回true
     */
    public boolean useSwipeRefreshLayout() {
        return false;
    }

    protected void initSavadInstance(Bundle savedInstanceState) {
    }

    protected void initPresenter() {
    }

    protected abstract int getLayoutId();

    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }

    protected abstract void initFragment(View view);

    /**
     * 返回要显示在标题栏中的标题
     *
     * @return 返回 null 则隐藏标题栏
     */
    public
    @StringRes
    int getTitleText() {
        return -1;
    }

    private void initBaseToolBar() {
        mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        setNormalTitleBar();
    }

    /**
     * 初始化标准的标题栏
     */
    public void setNormalTitleBar() {

        if (getActivity() instanceof ActivityBase) {
            Toolbar toolbar = ((ActivityBase) getActivity()).getToolbar();
            toolbar.setVisibility(View.GONE);
        }
        //        View titleBar = getLayoutInflater().inflate(R.layout.view_simple_title_bar, null);
        View toolBar = mInflater.inflate(R.layout.view_simple_title_bar, null);

        mTitleBar = toolBar.findViewById(R.id.title_bar);
        mImageBack = toolBar.findViewById(R.id.titlebar_back);
        mTextTitle = toolBar.findViewById(R.id.titlebar_title);
        mRightIcon = toolBar.findViewById(R.id.titlebar_right_icon);
        mRightText = toolBar.findViewById(R.id.titlebar_right_text);
        mMiddleRightIcon = toolBar.findViewById(R.id.titlebar_middle_right_icon);
        mTitleBarLine = toolBar.findViewById(R.id.titlebar_line);
        mPBTitle = toolBar.findViewById(R.id.pb_reloading);

        setTitle(getTitleText());

        mImageBack.setImageResource(R.drawable.core_icon_left_arrow);
        mImageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
        setCustomTitleBar(toolBar);
    }

    /**
     * 添加自定义标题栏
     */
    public void setCustomTitleBar(View titleBarLayout) {
        // 先清除掉之前可能加入的
        mToolbar.removeAllViews();
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.MATCH_PARENT);
        //传入的布局，覆盖整个Toolbar
        mToolbar.addView(titleBarLayout, lp);
    }

    /**
     * 该方法是给使用到地图的 Fragment 用的，因为地图中需要用到 savedInstanceState
     */
    protected void onViewInflated(View view, Bundle savedInstanceState) {
    }

    public void setTitleBarBackground(@ColorRes int color) {
        mTitleBar.setBackgroundColor(ContextCompat.getColor(_mActivity, color));
    }

    public void setTitleBarVisible(boolean visible) {
        mToolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void hideBackButton() {
        setStatusLeftImage(View.GONE);
    }

    /**
     * 设置标题
     */
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            mTextTitle.setText(title);
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.GONE);
        }
        mPBTitle.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置左图标的状态
     */
    public void setStatusLeftImage(int visible) {
        if (mImageBack != null) {
            mImageBack.setVisibility(visible);
        }
    }

    /**
     * 设置左边图标
     */
    public void setLeftImage(@DrawableRes int res) {
        if (mImageBack != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mImageBack.getLayoutParams();
            layoutParams.setMargins(ScreenUtil.dip2px(getContext(), 15), 0, 0, 0);
            mImageBack.setLayoutParams(layoutParams);
            mImageBack.setImageResource(res);
        }
    }

    /**
     * 设置左图标点击事件
     */
    public void setLeftImageClick(OnClickListener onClickListener) {
        if (mImageBack != null) {
            mImageBack.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置标题栏的分割线是否可见
     */
    public void setTitlebarLine(int visible) {
        if (null != mTitleBarLine) {
            mTitleBarLine.setVisibility(visible);
        }
    }

    /**
     * 设置右文本
     */
    public void setRightTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            mRightText.setText(title);
            mToolbar.setVisibility(View.VISIBLE);
            mRightIcon.setVisibility(View.INVISIBLE);
            mRightText.setVisibility(View.VISIBLE);
        } else {
            mRightText.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置右文本
     */
    public void setRightIcon(@DrawableRes int drawableRes) {
        mToolbar.setVisibility(View.VISIBLE);
        mRightIcon.setImageResource(drawableRes);
        mRightIcon.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.INVISIBLE);
    }

    public void setMiddleRightIcon(@DrawableRes int drawableRes) {
        if (null != mMiddleRightIcon) {
            if (-1 == drawableRes) {
                mMiddleRightIcon.setVisibility(View.GONE);
            } else {
                mMiddleRightIcon.setImageResource(drawableRes);
                mMiddleRightIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setMiddleRightOnClick(OnClickListener listener) {
        if (null != mMiddleRightIcon) {
            mMiddleRightIcon.setOnClickListener(listener);
        }
    }

    public void showRightIcon(boolean isShow) {
        mRightIcon.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置右文本点击事件
     */
    public void setRightTitleClick(OnClickListener onClickListener) {
        if (mRightText != null) {
            mRightText.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置右文本点击事件
     */
    public void setRightIconClick(OnClickListener onClickListener) {
        if (mRightIcon != null) {
            mRightIcon.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(int titleId) {
        if (titleId == -1) {
            mToolbar.setVisibility(View.GONE);
        } else {
            mToolbar.setVisibility(View.VISIBLE);
            mTextTitle.setText(titleId);
        }
    }

    public boolean checkNetworkState() {
        ConnectivityManager cm = (ConnectivityManager) _mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Toast 提示
     */
    @Override
    public void showToast(int resid) {
        UiUtil.showToast(mContext, resid);
    }

    /**
     * Toast 提示
     */
    @Override
    public void showToast(int resid, int duration) {
        UiUtil.showToast(mContext, resid);
    }

    /**
     * Toast 提示
     */
    @Override
    public void showToast(CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            UiUtil.showToast(mContext, message.toString());
        }
    }

    /**
     * Toast 提示
     */
    @Override
    public void showToast(CharSequence message, int duration) {
        if (!TextUtils.isEmpty(message)) {
            UiUtil.showToast(mContext, message.toString());
        }
    }

    @Override
    public void showMsg(int title, int des) {
        UiUtil.showToast(mContext, title, des);
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
     * 如果使用了下拉刷新，重新加载数据成功后需要调此方法
     */
    public void showContent() {
        mPageLoadingHelper.showContent();
        setRefreshComplete();
    }

    /**
     * 隐藏等待框
     * 顺便隐藏refresh
     */
    @Override
    public void stopWaiting() {
    }

    /**
     * 显示或隐藏输入法
     */
    protected void showKeyboard(boolean isShow) {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (isShow) {
                if (getActivity().getCurrentFocus() == null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                } else {
                    imm.showSoftInput(getActivity().getCurrentFocus(), 0);
                }
            } else {
                if (getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
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
    public void showEmptyView(
            @DrawableRes
                    int pEmptyIconRes,
            @StringRes
                    int pEpmtyMes) {
        setRefreshComplete();
        mPageLoadingHelper.showEmptyView(pEmptyIconRes, pEpmtyMes);
    }

    @Override
    public void showEmptyView() {
        setRefreshComplete();
        mPageLoadingHelper.showEmptyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisibleToUser = true;
        } else {
            mIsVisibleToUser = false;
        }
        mIsVisible = isFragmentVisible();
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsForeground = true;
        mIsVisible = isFragmentVisible();
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsForeground = false;
        mIsVisible = isFragmentVisible();
    }

    /**
     * 在ViewPager中可用该方法可以判断是否在可见
     */
    public boolean isFragmentVisible() {
        return mIsVisibleToUser && mIsForeground;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != dialogArrayMap) {
            dialogArrayMap.clear();
        }
    }
}

