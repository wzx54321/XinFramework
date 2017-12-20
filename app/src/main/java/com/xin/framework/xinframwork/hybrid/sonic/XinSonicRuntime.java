package com.xin.framework.xinframwork.hybrid.sonic;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicRuntime;
import com.tencent.sonic.sdk.SonicSessionClient;
import com.xin.framework.xinframwork.BuildConfig;
import com.xin.framework.xinframwork.common.FileConfig;
import com.xin.framework.xinframwork.hybrid.model.CookiesHandler;
import com.xin.framework.xinframwork.hybrid.webview.WebViewConfig;
import com.xin.framework.xinframwork.utils.android.SysUtils;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.common.utils.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Description :SonicRuntime实现类
 * Created by 王照鑫 on 2017/11/6 0006.
 */

public class XinSonicRuntime extends SonicRuntime {

    public XinSonicRuntime(Context context) {
        super(context);
    }

    @Override
    public void log(String tag, int level, String message) {
        switch (level) {
            case Log.ERROR:
                Log.e(tag + message);
                break;
        }
          /*    case Log.INFO:
                Log.i(tag + message);
                break;
            default:
                Log.d(tag + message);
        }*/
    }

    @Override
    public String getCookie(String url) {
        return  CookiesHandler.getCookiesByUrl(url);
    }

    @Override
    public boolean setCookie(String url, List<String> cookies) {
        if (!TextUtils.isEmpty(url) && cookies != null && !cookies.isEmpty()) {
            for (String cookie : cookies) {
                 CookiesHandler.syncCookie(url, cookie);
            }
            return true;

        }
        return false;
    }

    /**
     * 获取用户UA信息
     *
     * @return
     */
    @Override
    public String getUserAgent() {
        return "";
    }

    /**
     * 获取用户ID信息
     *
     * @return
     */
    @Override
    public String getCurrentUserAccount() {
        return "xin";
    }

    @Override
    public boolean isSonicUrl(String url) {
        return true;
    }

    @Override
    public Object createWebResourceResponse(String mimeType, String encoding, InputStream data, Map<String, String> headers) {
        WebResourceResponse resourceResponse = new WebResourceResponse(mimeType, encoding, data);
        if (SysUtils.hasLollipop()) {
            resourceResponse.setResponseHeaders(headers);
        }
        return resourceResponse;
    }

    @Override
    public boolean isNetworkValid() {
        return true;
    }

    @Override
    public void showToast(CharSequence text, int duration) {

    }

    @Override
    public void postTaskToThread(Runnable task, long delayMillis) {
        Thread thread = new Thread(task, "XinSonicThread");
        thread.start();
    }

    @Override
    public void notifyError(SonicSessionClient client, String url, int errorCode) {

    }


    @Override
    public File getSonicCacheDir() {

        // FIXME 权限
        File file;
        if (BuildConfig.DEBUG) {
            file = FileConfig.getPublicDir(FileConfig.DIR_WEB_SONIC_CACHE);
            FileUtil.createDir(file);

            return file;
        }

        file = WebViewConfig.getInstance().getSonicCacheDir(getContext());
        if (!file.exists() && !file.mkdir()) {
            log("", Log.ERROR, "getSonicCacheDir error:make dir(" + file.getAbsolutePath() + ") fail!");
            notifyError(null, file.getPath(), SonicConstants.ERROR_CODE_MAKE_DIR_ERROR);
        }
        return file;
    }
}
