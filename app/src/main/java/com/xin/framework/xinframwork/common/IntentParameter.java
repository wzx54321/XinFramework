package com.xin.framework.xinframwork.common;

/**
 * Description : Intent传值使用的KEY,可以统一声明在这里
 * Created by xin on 2017/6/15 0015.
 * Job number：147109
 */

public interface IntentParameter {

    String TAG = IntentParameter.class.getSimpleName();

    interface extra {
        String WEB_LOADING_URL = TAG + "web_loading_url";
        String WEB_LOADING_METHOD = TAG + "web_loading_method";
    }

    interface extras {
        String WEB_OPEN_INFO = TAG + "WEB_OPEN_INFO";
    }


    interface rquestCode {

    }


    interface resultCode {

    }

}
