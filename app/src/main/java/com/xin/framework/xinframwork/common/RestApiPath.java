package com.xin.framework.xinframwork.common;

import com.xin.framework.xinframwork.BuildConfig;

/**
 * Description : 接口请求地址全部配置在这里
 * Created by 王照鑫 on 2017/5/16 0016.
 */

public class RestApiPath {

    // host
    private static final String HOST_OFFICIAL = "https://api.mericentury.com";
    private static final String HOST_DEV = "";
    private static final String HOST_TEST = "";
    private final static String[] HOST={HOST_OFFICIAL,HOST_DEV,HOST_TEST};

    public static String REST_URI_HOST = HOST[BuildConfig.API_PATH];
}
