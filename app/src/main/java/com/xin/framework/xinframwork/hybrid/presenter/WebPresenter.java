package com.xin.framework.xinframwork.hybrid.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.xin.framework.xinframwork.hybrid.bean.WebOpenInfo;
import com.xin.framework.xinframwork.hybrid.bean.WebPostParams;
import com.xin.framework.xinframwork.hybrid.contract.WebContract;
import com.xin.framework.xinframwork.hybrid.model.WebModel;
import com.xin.framework.xinframwork.hybrid.sonic.XinSonicSessionClient;
import com.xin.framework.xinframwork.hybrid.webview.WebViewConfig;
import com.xin.framework.xinframwork.mvp.Iv;
import com.xin.framework.xinframwork.utils.android.logger.Log;

/**
 * Description : webview使用的presenter
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class WebPresenter implements WebContract.Presenter {

    private WebContract.View mView;
    private WebContract.Model model;
    private WebOpenInfo mWebOpenInfo;
    private XinSonicSessionClient mSonicSessionClient;


    @Override
    public void onStart() {
        model = new WebModel();

    }

    @Override
    public void onDestroy() {
        model.onDestroy();
        mView = null;
    }

    @Override
    public void setView(Iv iview) {
        mView = (WebContract.View) iview;
    }


    @Override
    public void setWebOpenInfo(WebOpenInfo info) {
        this.mWebOpenInfo = info;


    }

    @Override
    public void setWebViewClient() {


        SonicSession mSonicSession = buildSonicSession();


        mView.setWebViewClient(new WebModel.XinWebViewClient(mSonicSession) {
            @Override
            public void onTitleSet(String titleText) {
                mView.setTitleText(titleText);
            }


            @Override
            public void setTitleStyles() {

                mView.setTitleStyles();

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


        });

    }


    @Override
    public void setWebChromeClient() {
        mView.setWebChromeClient(new WebModel.XinWebChromeClient() {
            @Override
            public void onTitleSet(String titleText) {
                mView.setTitleText(titleText);
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mView.onProgressChanged(view, newProgress);

            }


            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                mView.onShowCustomView(view, callback);
            }


            @Override
            public void onHideCustomView() {
                super.onHideCustomView();

                mView.onHideCustomView();
            }
        });

    }

    @Override
    public void openBrowser(WebOpenInfo webOpenInfo) {
        String url = webOpenInfo.getUrl();
        String htmlContent = webOpenInfo.getHtmlContent();
        WebPostParams<String, String> params = webOpenInfo.getParams();
        if (TextUtils.isEmpty(htmlContent) && !TextUtils.isEmpty(url)) {
            if (params == null || params.isEmpty()) {

                // webview is ready now, just tell session client to bind
                if (mSonicSessionClient != null) {
                    mSonicSessionClient.bindWebView(mView.getWebView());
                    mSonicSessionClient.clientReady();
                } else { // default mode
                    mView.loadUrl(url);
                }
                Log.d("XinWebView访问地址:" + url);
            } else {
                mView.postUrl(url, params); // 没有使用 sonic
                Log.d("XinWebView访问地址:" + url + "\n" + "参数：" + params.toString());
            }
        } else {
            mView.loadData(htmlContent);
            Log.d("XinWebView访问内容:" + htmlContent);
        }
    }

    @Override
    public SonicSession buildSonicSession() {
        // create sonic session and run sonic flow
        SonicSession sonicSession = SonicEngine.getInstance().createSession(mWebOpenInfo.getUrl(), WebViewConfig.getInstance().getSessionConfig());
        if (null != sonicSession) {
            sonicSession.bindClient(mSonicSessionClient = new XinSonicSessionClient());
        }


        /*
        *   预先加载数据
        *  boolean preloadSuccess = SonicEngine.getInstance().preCreateSession(url, sessionConfig);
        * */
        return sonicSession;
    }


}
