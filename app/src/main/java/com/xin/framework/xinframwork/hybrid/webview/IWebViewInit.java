package com.xin.framework.xinframwork.hybrid.webview;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

/**
 * Created by 王照鑫 on 2017/11/1 0001.
 */

public interface IWebViewInit {

    WebSettings initWebSettings(WebView view);

    void addJavascriptInterface(Object object, String name);

    boolean init();


    String getCacheDir(Context context);

    File getSonicCacheDir(Context context);

    XinWebView useWebView(Context context);

    void resetWebView();

    void clearWebCache(boolean mIsWebViewInit);

    void checkCacheMode();
}
