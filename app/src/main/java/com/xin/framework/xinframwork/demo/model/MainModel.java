package com.xin.framework.xinframwork.demo.model;

import com.xin.framework.xinframwork.demo.api.ApiMethod;
import com.xin.framework.xinframwork.demo.api.XinRequest;
import com.xin.framework.xinframwork.demo.bean.AppVersion;
import com.xin.framework.xinframwork.demo.contract.MainContract;
import com.xin.framework.xinframwork.http.callback.CustomRequestCallback;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/25 0025.
 */

public class MainModel implements MainContract.Model {
    @Override
    public void onDestroy() {

    }

    @Override
    public void checkVersion(CustomRequestCallback<AppVersion> customRequestCallback) {
      new XinRequest<AppVersion>(AppVersion.class).Post(ApiMethod.API_CHECK_VER, null, customRequestCallback);

    }
}
