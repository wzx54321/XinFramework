package com.xin.framework.xinframwork.content;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.common.SharedPreferencesKeys;
import com.xin.framework.xinframwork.utils.common.data.DataKeeper;

import java.util.UUID;

/**
 * Description :SharedPreferences
 * Created by xin on 2017/5/16 0016.
 */

public class SPManager implements SharedPreferencesKeys {


    private DataKeeper mDk;
    private static SPManager spManager;

    private SPManager() {
        mDk = new DataKeeper(XinApplication.getAppContext(), spFileName);
    }

    public static SPManager getInstance() {
        if (spManager == null) {
            synchronized (SPManager.class) {
                if (spManager == null) {
                    spManager = new SPManager();
                }
            }

        }

        return spManager;
    }


    /**
     * 获取设备ID
     *
     * @return 设备ID
     */
    public String getDeviceId() {
        return mDk.get(KEY_PHONE_DEVICE_ID, "");
    }

    /**
     * 保存设备ID
     *
     * @param deviceId 设备ID
     */
    @SuppressLint("HardwareIds")
    public void putDeviceId(String deviceId) {

        if (TextUtils.isEmpty(mDk.get(KEY_PHONE_DEVICE_ID, ""))) {
            // 某些设备在应用启动时会提示获取手机识别码安全限制，此时如果禁止权限则deviceId为空，无法进行登录,所以这种情况下取SERIAL作为设备唯一标识
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = android.os.Build.SERIAL != null ? android.os.Build.SERIAL : UUID.randomUUID().toString();
            }
            mDk.put(KEY_PHONE_DEVICE_ID, deviceId);

        }

    }


    /**
     * 登录的UID
     *
     * @return
     */
    public String getUserID() {
        return mDk.get(KEY_USER_ID, "");
    }


    /**
     * 存UserID
     *
     * @param uid 用户ID
     */
    public void putUserId(String uid) {
        mDk.put(KEY_USER_ID, uid);
    }

    /**
     * @return 获取userTOKEN
     */
    public String getUserToken() {
        return mDk.get(KEY_USER_TOKEN, "");
    }


    /***
     *
     * @param token 登录使用的token
     */
    public void putUserToken(String token) {
        mDk.put(KEY_USER_TOKEN, token);
    }

    /**
     * 存渠道号
     */
    public void putChannel(String info) {
        mDk.put(KEY_RECORDED_CHANNEL_ID, info);
    }

    /**
     * 获取渠道号
     */
    public String getChannel() {
       return mDk.get(KEY_RECORDED_CHANNEL_ID, "");
    }


}
