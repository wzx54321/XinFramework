package com.xin.framework.xinframwork.hybrid.webview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.xin.framework.xinframwork.http.utils.HttpUtils;
import com.xin.framework.xinframwork.hybrid.bean.WebPostParams;
import com.xin.framework.xinframwork.hybrid.model.WebModel;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.common.io.StringCodingUtils;

import okhttp3.internal.Util;

/**
 * Description :
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class XinWebView extends WebView {


    private ViewGroup mViewGroup;
    private boolean mIsUsed;
    private WebModel.XinWebViewClient mXinWebViewClient;

    public XinWebView(Context context) {
        super(context);
    }

    public XinWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XinWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBackClickListener != null) {
                return onBackClickListener.onKeyBack() || super.onKeyDown(keyCode,
                        event);
            }
        }

        return super.onKeyDown(keyCode,
                event);
    }

    private onBackClickListener onBackClickListener;


    public void setOnBackClickListener(XinWebView.onBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public void setParentViewGroup(ViewGroup view, ViewGroup.LayoutParams webParams) {

        if (mIsUsed) {
            if (mViewGroup != null) {
                mViewGroup.removeView(this);
            }
        }
        this.mViewGroup = view;

        view.addView(this, webParams);
    }


    public ViewGroup getParentViewGroup() {
        return mViewGroup;
    }

    public void setIsUsed(boolean isUsed) {
        mIsUsed = isUsed;

    }

    public interface onBackClickListener {
        boolean onKeyBack();
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }


    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }


    public void postUrl(String url, @NonNull WebPostParams<String, String> params) {

        String paramStr = HttpUtils.createParams(url, params);

        if (!TextUtils.isEmpty(paramStr)) {

            Log.i("webView地址:  " + url + "\n" + "参数: " + paramStr.toString());
            postUrl(url, StringCodingUtils.getBytes(paramStr, Util.UTF_8));//这种写法可以正确解码

        }
    }


    public void setXinWebViewClient(WebModel.XinWebViewClient client) {
        super.setWebViewClient(client);
        this.mXinWebViewClient = client;

    }

    public void recycle() {

        if (mXinWebViewClient != null) {
            mXinWebViewClient.getSonicSession().destroy();
            mXinWebViewClient.setSonicSession(null);
        }

        setWebViewClient(null);

        setWebChromeClient(null);
        setOnBackClickListener(null);
        super.destroy();

        if (mViewGroup != null) {
            if (mViewGroup.indexOfChild(this) != -1) {
                mViewGroup.removeView(this);
                mViewGroup = null;

                mIsUsed = false;
            }
        }
    }
}
