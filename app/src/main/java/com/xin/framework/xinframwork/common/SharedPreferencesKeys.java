package com.xin.framework.xinframwork.common;


public interface SharedPreferencesKeys {

    String TAG = SharedPreferencesKeys.class.getSimpleName();

    // 升级后channel id不变
    String KEY_RECORDED_CHANNEL_ID = TAG + ".key.recorded.channel.id";

    String KEY_APP_VERSION = TAG + ".key.app.version.name";

    String KEY_APP_VERSION_CODE = TAG + ".key.app.version.code";

    String KEY_PHONE_DEVICE_ID = TAG + "key.phone.device.id";

    /**
     * 根据使用情况配置文件名称
     */
    String spFileName = "common_sharedPreferences";
}