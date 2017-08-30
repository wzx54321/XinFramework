package com.xin.framework.xinframwork.common;

import android.content.Context;
import android.text.TextUtils;

import com.meituan.android.walle.ChannelInfo;
import com.xin.framework.xinframwork.content.SPManager;
import com.xin.framework.xinframwork.utils.android.SysUtils;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/22 0022.
 */

public class AppConfig {
    public static void setDeviceId(Context app) {
        if (TextUtils.isEmpty(SPManager.getInstance().getDeviceId())) {
            SPManager.getInstance().putDeviceId(SysUtils.getDeviceId(app));
        }
    }

    public static void setChannel(Context app) {
        if (TextUtils.isEmpty(SPManager.getInstance().getChannel())) {
            ChannelInfo channelInfo = SysUtils.readChannel(app);
            SPManager.getInstance().putChannel(channelInfo != null ? channelInfo.getChannel() : "200001");
        }
    }
}
