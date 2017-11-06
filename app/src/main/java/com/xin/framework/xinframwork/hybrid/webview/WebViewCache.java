package com.xin.framework.xinframwork.hybrid.webview;

import android.content.Context;
import android.content.MutableContextWrapper;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.common.utils.FileUtil;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.xin.framework.xinframwork.hybrid.webview.WebViewConfig.CookiesHandler.removeAllCookies;

/**
 * Description :WebView Cache
 * Created by 王照鑫 on 2017/11/1 0001.
 */

class WebViewCache {

    private ConcurrentLinkedQueue<XinWebView> webViews;


    private WebViewCache() {
        webViews = new ConcurrentLinkedQueue<>();
    }

    public static WebViewCache getInstance() {
        return Holder.mInstance;
    }

    private static class Holder {
        static WebViewCache mInstance = new WebViewCache();
    }


    protected XinWebView initWebView() {
        XinWebView view = new XinWebView(new MutableContextWrapper(getApplication()));
        put(view);

        return view;
    }


    protected void resetWebView() {
        XinWebView webView = getWebView();
        if (webView != null) {
            webView.recycle();

            webViews.poll();
        }
    }


    protected XinWebView useWebView(Context ctx) {
        XinWebView view = getWebView();
        if (view == null) {
            view = initWebView();
        }
        ((MutableContextWrapper) view.getContext()).setBaseContext(ctx);
        return view;
    }


    protected void clearWebCache(Boolean init) {

        if (!init || !getApplication().getResources().getBoolean(R.bool.web_clear_cache_enable))
            return;
        try {

            XinWebView webView = webViews.poll();
            if (webView != null) {
                getApplication().deleteDatabase("webviewCache.db");
                getApplication().deleteDatabase("webview.db");
                webView.clearCache(true);
                webView.clearHistory();
                webView.clearFormData();
                removeAllCookies(null);
                FileUtil.clearCacheFolder(new File(WebViewConfig.getInstance().getCacheDir(getApplication())), 0);
            }
            webViews = null;
        } catch (Exception e) {
            Log.printStackTrace(e);
        }

    }


    protected void put(XinWebView webView) {

        if (webView == null)
            return;

        webViews.clear();
        webViews.add(webView);

    }


    protected XinWebView getWebView() {
        return webViews.peek();

    }


    protected Context getApplication() {
        return XinApplication.getAppContext();
    }


}
