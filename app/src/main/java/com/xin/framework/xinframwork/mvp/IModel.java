package com.xin.framework.xinframwork.mvp;

import com.xin.framework.xinframwork.demo.bean.AppVersion;
import com.xin.framework.xinframwork.http.callback.CustomRequestCallback;

/**
 *  M
 */

public interface IModel {
    void onDestroy();

    void checkVersion(CustomRequestCallback<AppVersion> customRequestCallback);
}
