package com.xin.framework.xinframwork.hybrid.contract;

import android.support.annotation.NonNull;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xin.framework.xinframwork.hybrid.bean.WebOpenInfo;
import com.xin.framework.xinframwork.hybrid.bean.WebPostParams;
import com.xin.framework.xinframwork.mvp.IModel;
import com.xin.framework.xinframwork.mvp.IPresenter;
import com.xin.framework.xinframwork.mvp.IView1;

/**
 * Description : web view 使用
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public interface WebContract {

    interface Presenter extends IPresenter {


        void setWebViewClient();

        void setWebChromeClient();

        void openBrowser(WebOpenInfo mWebOpenInfo);
    }

    interface Model extends IModel {

    }


    interface View extends IView1 {
        void setWebViewClient(WebViewClient client);

        void setWebChromeClient(WebChromeClient client);

        void setTitleText(String titleText);

        void onProgressChanged(WebView view, int newProgress);

        void setTitleStyles();


        void postUrl(String url, @NonNull WebPostParams<String, String> params);

        void loadData(String content);

        void loadUrl(String url);

        void onShowCustomView(android.view.View view, WebChromeClient.CustomViewCallback callback);

        void onHideCustomView();


    }



}
