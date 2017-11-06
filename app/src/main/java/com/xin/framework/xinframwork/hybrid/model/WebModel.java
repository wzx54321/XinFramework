package com.xin.framework.xinframwork.hybrid.model;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.hybrid.contract.WebContract;
import com.xin.framework.xinframwork.hybrid.webview.WebViewConfig;
import com.xin.framework.xinframwork.hybrid.webview.XinWebView;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import java.util.HashMap;

/**
 * Description :Model
 * Created by 王照鑫 on 2017/11/1 0001.
 */

public class WebModel implements WebContract.Model {


    private XinWebView mWebView;
    private static HashMap<String, String> titles;

    public WebModel() {

        titles = new HashMap<>();
    }


    public static class XinWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

           Log.d("XinWebView访问地址:" + url);

            if (handle_Tel_Mail_SMS(url)) {
                return true;
            }
            return shouldOverrideUrlLoading(url);
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            String url = request.getUrl().toString();
            if (handle_Tel_Mail_SMS(url)) {
                return true;
            }
            return shouldOverrideUrlLoading(url);
        }


        /**
         * 子类继承
         *
         * @param url
         * @return True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
         */
        public boolean shouldOverrideUrlLoading(String url) {
            // 子类继承
            return false;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            // 部分手机在返回多级web页时，不回调onReceivedTitle方法，故在加载完成时，获取之前存储的title。
            String titleText = titles.get(view.getOriginalUrl());
            if (!TextUtils.isEmpty(titleText)) {
                onTitleSet(titleText);
            }

            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
            setTitleStyles();

        }

        public void setTitleStyles() {
        }

        public void onTitleSet(String titleText) {
        }


        // 重写其他的方法处理


    }


    public static class XinWebChromeClient extends WebChromeClient {


        @Override
        public void onReceivedTitle(WebView view,
                                    String title) {
            // 部分手机在返回多级web页时，不回调onReceivedTitle方法，故每次加载时存入title。
            if (!TextUtils.isEmpty(title)) {
                titles.put(view.getOriginalUrl(),
                        title);
                onTitleSet(title);
            }

        }


        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin,
                                                       final GeolocationPermissions.Callback callback) {
            callback.invoke(origin,
                    true,
                    true);

            super.onGeolocationPermissionsShowPrompt(origin,
                    callback);
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        public void onTitleSet(String titleText) {
        }

    }







    private static boolean handle_Tel_Mail_SMS(String targetUrl) {

        if (TextUtils.isEmpty(targetUrl)) {
            return false;
        }
        try {
            // 兼容打电话、发短信、发邮件
            if (WebViewConfig.TEL_ENABLE && targetUrl.startsWith("tel:")
                    || WebViewConfig.MAIL_ENABLE && targetUrl.startsWith("mailto:")
                    || WebViewConfig.SMS_ENABLE && targetUrl.startsWith("smsto:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                XinApplication.getAppContext().startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
        }

        return false;
    }

    @Override
    public void onDestroy() {
        if (titles != null) {
            titles.clear();
            titles = null;

        }
    }
}
