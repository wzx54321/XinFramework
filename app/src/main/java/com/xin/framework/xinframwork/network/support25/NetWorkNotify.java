package com.xin.framework.xinframwork.network.support25;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Description :
 * Created by 王照鑫 on 2017/6/15 0015.
 */

public class NetWorkNotify {

    private Context mContext;
    private static NetWorkNotify mInstance;

    private NetWorkNotify(Context context) {
        this.mContext = context;
    }


    public static NetWorkNotify getInstance(Context context) {
        if (mInstance == null) {
            synchronized (NetWorkNotify.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkNotify(context);
                }
            }
        }

        return mInstance;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setNetNotifyForNougat(ConnectivityManager.NetworkCallback callback) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), callback);
    }
}
