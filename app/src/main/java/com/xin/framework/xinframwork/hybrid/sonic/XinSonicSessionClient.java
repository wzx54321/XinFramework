package com.xin.framework.xinframwork.hybrid.sonic;

import android.os.Bundle;

import com.tencent.sonic.sdk.SonicSessionClient;
import com.xin.framework.xinframwork.hybrid.webview.WebViewConfig;
import com.xin.framework.xinframwork.hybrid.webview.XinWebView;

import java.util.HashMap;

/**
 * Description :SonicSessionClient实现 参照：https://github.com/Tencent/VasSonic/blob/master/sonic-android/README.md
 * Created by 王照鑫 on 2017/11/6 0006.
 */

public class XinSonicSessionClient extends SonicSessionClient {

    private XinWebView webView;

    public void bindWebView(XinWebView webView) {
        this.webView = webView;
    }

    public XinWebView getWebView() {
        return webView;
    }

    @Override
    public void loadUrl(String url, Bundle extraData) {
        WebViewConfig.getInstance().checkCacheMode();
        webView.loadUrl(url);
    }

    @Override
    public void loadDataWithBaseUrl(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, "UTF-8", historyUrl);
    }


    @Override
    public void loadDataWithBaseUrlAndHeader(String baseUrl, String data, String mimeType, String encoding, String historyUrl, HashMap<String, String> headers) {
        loadDataWithBaseUrl(baseUrl, data, mimeType, encoding, historyUrl);
    }


}
