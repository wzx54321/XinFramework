package com.xin.framework.xinframwork.content;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.xin.framework.utils.common.data.DataKeeper;
import com.xin.framework.xinframwork.app.App;
import com.xin.framework.xinframwork.common.SharedPreferencesKeys;

import java.util.UUID;

/**
 * Description :SharedPreferences
 * Created by 王照鑫 on 2017/5/16 0016.
 */

public class SPManager implements SharedPreferencesKeys {


    private DataKeeper mDk;
    private static SPManager spManager;

    private SPManager() {
        mDk = new DataKeeper(App.getAppContext(), spFileName);
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
     * @return
     */
    public String getDeviceId() {
        return mDk.get(KEY_PHONE_DEVICE_ID, "");
    }

    /**
     * 保存设备ID
     *
     * @param deviceid
     */
    @SuppressLint("HardwareIds")
    public void putDeviceId(String deviceid) {

        if (TextUtils.isEmpty(mDk.get(KEY_PHONE_DEVICE_ID, ""))) {
            // 某些设备在应用启动时会提示获取手机识别码安全限制，此时如果禁止权限则deviceid为空，无法进行登录,所以这种情况下取SERIAL作为设备唯一标识
            if (TextUtils.isEmpty(deviceid)) {
                deviceid = android.os.Build.SERIAL != null ? android.os.Build.SERIAL : UUID.randomUUID().toString();
            }
            mDk.put(KEY_PHONE_DEVICE_ID, deviceid);

        }

    }


}
