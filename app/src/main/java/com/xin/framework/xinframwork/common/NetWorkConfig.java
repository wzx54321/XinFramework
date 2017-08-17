package com.xin.framework.xinframwork.common;

import android.Manifest;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xin.framework.xinframwork.network.support25.NetWorkNotify;
import com.xin.framework.xinframwork.utils.android.PermissionUtil;
import com.xin.framework.xinframwork.utils.android.SysUtils;

import static com.xin.framework.xinframwork.app.XinApplication.getAppContext;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/17 0017.
 * Job number：147109
 * Email： wangzhaoxin@syswin.com
 * Person in charge :
 * Leader：guohaichun
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetWorkConfig {


    final static ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            //就会连接该网络，回调 通过Network获取相关信息
        }

        // to Override ...
    };

    public static void initNetNotify(Activity activity) {

        //   网络监听
        if (SysUtils.hasNougat()) {


            PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {

                @Override
                public void onRequestPermissionSuccess() {

                    NetWorkNotify.getInstance(getAppContext()).setNetNotifyForNougat(callback);


                }

                @Override
                public void onRequestPermissionFailure() {

                }
            }, new RxPermissions(activity), null, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.WRITE_SETTINGS);

        }
    }
}
