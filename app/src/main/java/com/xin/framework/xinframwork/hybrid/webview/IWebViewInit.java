package com.xin.framework.xinframwork.hybrid.webview;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

/**
 * Description :
 * Created by 王照鑫 on 2017/11/1 0001.
 * Job number：147109
 * Email： wangzhaoxin@syswin.com
 * Person in charge :
 * Leader：guohaichun
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
