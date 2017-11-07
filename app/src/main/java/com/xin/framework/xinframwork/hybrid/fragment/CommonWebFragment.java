package com.xin.framework.xinframwork.hybrid.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.base.BaseFragment;
import com.xin.framework.xinframwork.common.IntentParameter;
import com.xin.framework.xinframwork.hybrid.bean.WebOpenInfo;
import com.xin.framework.xinframwork.hybrid.bean.WebPostParams;
import com.xin.framework.xinframwork.hybrid.contract.WebContract;
import com.xin.framework.xinframwork.hybrid.model.WebModel;
import com.xin.framework.xinframwork.hybrid.presenter.WebPresenter;
import com.xin.framework.xinframwork.hybrid.video.WebVideoDelegate;
import com.xin.framework.xinframwork.hybrid.webview.WebViewConfig;
import com.xin.framework.xinframwork.hybrid.webview.XinWebView;
import com.xin.framework.xinframwork.ui.widget.titlebar1.CommonTitleBar;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Description : 通用的webview
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class CommonWebFragment extends BaseFragment<WebPresenter> implements WebContract.View, XinWebView.onBackClickListener {


    @BindView(R.id.title_web_view)
    CommonTitleBar mTitleWebView;
    @BindView(R.id.progressbar_webview)
    ProgressBar mProgressbar;
    @BindView(R.id.root_webview_f)
    LinearLayout mRootWebviewF;

    @BindView(R.id.app_back)
    ImageView mBtnGoBack;
    @BindView(R.id.app_close)
    ImageView mBtnClose;


    private XinWebView mWebView;
    private WebVideoDelegate mWebVideoDelegate;


    public static CommonWebFragment getInstance(@NonNull Bundle bundle) {

        CommonWebFragment webFragment = new CommonWebFragment();
        webFragment.setArguments(bundle);

        return webFragment;

    }


    @Nullable
    @Override
    protected View setLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.common_fragment_web, null, false);
    }

    @Override
    protected void initView() {

        ButterKnife.bind(mTitleWebView.getLeftCustomView());
        mBtnClose.setVisibility(View.INVISIBLE);
        mWebView=  WebViewConfig.getInstance().useWebView(getContext());
        LinearLayout.LayoutParams webParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setParentViewGroup(mRootWebviewF, webParams);


        mWebVideoDelegate = new WebVideoDelegate(_mActivity, mWebView);
        mWebView.setOnBackClickListener(this);

    }


    @Override
    protected void onPresenterStarted() {


        WebOpenInfo mWebOpenInfo = (WebOpenInfo) getArguments().getSerializable(IntentParameter.extras.WEB_OPEN_INFO);
        mPresenter.setWebOpenInfo(mWebOpenInfo);
        mPresenter.setWebViewClient();
        mPresenter.setWebChromeClient();



        mPresenter.openBrowser(mWebOpenInfo);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null)
            mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }


    @Override
    public void onDestroy() {
        if (mWebView != null) {

            WebViewConfig.getInstance().resetWebView();
        }
        super.onDestroy();
    }


    @Override
    public void setWebViewClient(WebModel.XinWebViewClient client) {
        mWebView.setXinWebViewClient(client);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        mWebView.setWebChromeClient(client);
    }

    @Override
    public void setTitleText(String titleText) {
        mTitleWebView.getCenterTextView().setText(titleText);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress < 100 && !mProgressbar.isShown()) {
            mProgressbar.setVisibility(View.VISIBLE);
        }

        mProgressbar.setProgress(newProgress);
        mProgressbar.postInvalidate();

        if (newProgress == 100) {
            mProgressbar.setVisibility(View.GONE);

        }
    }


    @Override
    public void setTitleStyles() {
        // 处理title左边的view按钮显示效果
        if (mWebView.canGoBack()) {
            mBtnClose.setVisibility(View.VISIBLE);
        } else {
            mBtnClose.setVisibility(View.INVISIBLE);
        }
    }


    @OnClick({R.id.app_back, R.id.app_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_back:
                if (!onKeyBack()) {
                    _mActivity.finish();
                }
                break;
            case R.id.app_close:
                _mActivity.finish();
                break;
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.v("onConfigurationChanged_ORIENTATION_LANDSCAPE");
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.v("onConfigurationChanged_ORIENTATION_PORTRAIT");
            }
        } catch (Exception ex) {
            Log.printStackTrace(ex);
        }
    }


    /**
     * post请求网页
     *
     * @param url    地址
     * @param params 参数
     */
    @Override
    public void postUrl(String url, @NonNull WebPostParams<String, String> params) {


        mWebView.postUrl(url, params);


    }

    /**
     * 加载本地网页
     */
    @Override
    public void loadData(String content) {
        mWebView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        mWebVideoDelegate.onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        mWebVideoDelegate.onHideCustomView();
    }

    @Override
    public XinWebView getWebView() {
        return mWebView;
    }


    @Override
    public boolean onBackPressedSupport() {
        return onKeyBack();
    }

    @Override
    public boolean onKeyBack() {
        if (this.mWebVideoDelegate != null && this.mWebVideoDelegate.event()) {
            return true;
        }

        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }
}
