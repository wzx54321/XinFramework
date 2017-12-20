package com.xin.framework.xinframwork.hybrid.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;

import com.xin.framework.xinframwork.utils.android.SysUtils;
import com.xin.framework.xinframwork.utils.android.logger.Log;

/**
 * Description :coolies 处理类
 * Created by 王照鑫 on 2017/11/8 0008.
 */

public class CookiesHandler {

    private static boolean isInit = false;

    public static synchronized void initCookiesManager(Context context) {
        if (!isInit) {
            createCookiesSyncInstance(context);
            isInit = true;
        }
    }

    //获取Cookie
    public static String getCookiesByUrl(String url) {
        return CookieManager.getInstance() == null ? null : CookieManager.getInstance().getCookie(url);
    }


    /**
     * 清除过期的cookie
     */
    public static void removeExpiredCookies() {
        CookieManager mCookieManager = null;
        if ((mCookieManager = CookieManager.getInstance()) != null) { //同步清除
            mCookieManager.removeExpiredCookie();
            toSyncCookies();
        }
    }

    public static void removeAllCookies() {
        removeAllCookies(null);

    }


    // 解决兼容 Android 4.4 java.lang.NoSuchMethodError: android.webkit.CookieManager.removeSessionCookies
    public static void removeSessionCookies() {
        removeSessionCookies(null);
    }

    public static void removeSessionCookies(ValueCallback<Boolean> callback) {

        if (callback == null)
            callback = getDefaultIgnoreCallback();
        if (CookieManager.getInstance() == null) {
            callback.onReceiveValue(new Boolean(false));
            return;
        }
        if (!SysUtils.hasLollipop()) {
            CookieManager.getInstance().removeSessionCookie();
            toSyncCookies();
            callback.onReceiveValue(new Boolean(true));
            return;
        } else {

            CookieManager.getInstance().removeSessionCookies(callback);
        }
        toSyncCookies();

    }


    //Android  4.4  NoSuchMethodError: android.webkit.CookieManager.removeAllCookies
    public static void removeAllCookies(@Nullable ValueCallback<Boolean> callback) {

        if (callback == null)
            callback = getDefaultIgnoreCallback();
        if (!SysUtils.hasLollipop()) {
            CookieManager.getInstance().removeAllCookie();
            toSyncCookies();
            callback.onReceiveValue(!CookieManager.getInstance().hasCookies());
            return;
        } else {

            CookieManager.getInstance().removeAllCookies(callback);
        }
        toSyncCookies();
    }


    private static ValueCallback<Boolean> getDefaultIgnoreCallback() {

        return new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean ignore) {
                Log.i("removeExpiredCookies:" + ignore);
            }
        };
    }


    public static void syncCookie(String url, String cookies) {

        CookieManager mCookieManager = CookieManager.getInstance();
        if (mCookieManager != null) {
            mCookieManager.setCookie(url, cookies);
            toSyncCookies();
        }
    }


    private static void createCookiesSyncInstance(Context context) {


        if (!SysUtils.hasLollipop()) {
            CookieSyncManager.createInstance(context);
        }
    }


    private static void toSyncCookies() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
            return;
        }

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                if (SysUtils.hasLollipop())
                    CookieManager.getInstance().flush();

            }
        });
    }
}
