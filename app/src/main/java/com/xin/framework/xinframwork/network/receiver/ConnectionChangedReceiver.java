package com.xin.framework.xinframwork.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xin.framework.xinframwork.utils.android.logger.Log;


/**
 * 网络状态监听
 */
public class ConnectionChangedReceiver extends BroadcastReceiver {

    private static boolean isDisconnect;

    @Override
    public void onReceive(Context context,
                          Intent intent) {

        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = manager.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isAvailable()) {
                // 网络连接
                String name = netInfo.getTypeName();


                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // WiFi网络
                    Log.d("WiFi网络");
                } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    // 有线网络
                    Log.d(
                            "有线网络");
                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // ///////
                    Log.d(
                            "移动网络");
                }


                if (isDisconnect) {
                    HandleNetReConnection();
                    isDisconnect = false;
                }


            } else {
                // 网络断开
                Log.d(
                        "网络断开");
                if (!isDisconnect) {
                    // 发送断网广播

                }
                isDisconnect = true;

            }
        }

    }

    //TODO 重新
    private void HandleNetReConnection() {
        Log.d(
                "重新连接");

    }

}
