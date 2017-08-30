package com.xin.framework.xinframwork.common;


public interface SharedPreferencesKeys {

    String TAG = SharedPreferencesKeys.class.getSimpleName();

    // 升级后channel id不变
    String KEY_RECORDED_CHANNEL_ID = TAG + ".key.recorded.channel.id";

    String KEY_PHONE_DEVICE_ID = TAG + "key.phone.device.id";

    String KEY_USER_ID = TAG + "key.user.id";

    String KEY_USER_TOKEN = TAG + "key.user.token";


    /**
     * 根据使用情况配置文件名称
     */
    String spFileName = "common_sharedPreferences";
}