package com.xin.framework.xinframwork.hybrid.video;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

import com.xin.framework.xinframwork.hybrid.webview.XinWebView;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Description :WebVideo加载视频页面全屏兼容
 * Created by 王照鑫 on 2017/11/3 0003.
 */

public class WebVideoDelegate implements IVideo {

    private Activity mActivity;
    private XinWebView mWebView;
    private Set<Pair<Integer, Integer>> flags = null;

    private View mMoiveView;
    private ViewGroup moiveParentView = null;
    private WebChromeClient.CustomViewCallback mCallback;

    public WebVideoDelegate(Activity activity, XinWebView webView) {
        this.mActivity = activity;
        this.mWebView = webView;
        flags = new HashSet<>();
    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

        Log.i("onShowCustomView:" + view);

        if (mActivity.isFinishing())
            return;

        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Window window = mActivity.getWindow();

        Pair<Integer, Integer> mPair = null;
        //保存当前屏幕的状态
        if ((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) == 0) {
            mPair = new Pair<>(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 0);
            window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            flags.add(mPair);
        }


        if ((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED) == 0) {
            mPair = new Pair<>(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 0);
            window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            flags.add(mPair);
        }

        if (mMoiveView != null) {
            callback.onCustomViewHidden();
            return;
        }

        if (mWebView != null)
            mWebView.setVisibility(View.GONE);

        if (moiveParentView == null) {
            FrameLayout mDecorView = (FrameLayout) mActivity.getWindow().getDecorView();
            moiveParentView = new FrameLayout(mActivity);
            moiveParentView.setBackgroundColor(Color.BLACK);
            mDecorView.addView(moiveParentView);
        }
        this.mCallback = callback;
        moiveParentView.addView(this.mMoiveView = view);


        moiveParentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideCustomView() {

        Log.i("onHideCustomView:" + mMoiveView);
        if (mMoiveView == null)
            return;
        if (mActivity != null && mActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (!flags.isEmpty()) {
            for (Pair<Integer, Integer> mPair : flags) {
                mActivity.getWindow().setFlags(mPair.second, mPair.first);
                Log.i("f:" + mPair.first + "  s:" + mPair.second);
            }
            flags.clear();
        }

        mMoiveView.setVisibility(View.GONE);

        if (moiveParentView != null && mMoiveView != null) {
            moiveParentView.removeView(mMoiveView);

        }
        if (moiveParentView != null)
            moiveParentView.setVisibility(View.GONE);


        if (this.mCallback != null)
            mCallback.onCustomViewHidden();
        this.mMoiveView = null;
        if (mWebView != null)
            mWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isVideoState() {
            return mMoiveView != null;
    }

     @Override
    public boolean event() {

        Log.i("event:" + isVideoState());
        if (isVideoState()) {
            onHideCustomView();
            return true;
        } else {
            return false;
        }

    }
}
