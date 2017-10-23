package com.xin.framework.xinframwork.common;

import android.content.Context;

import com.xin.framework.xinframwork.BuildConfig;
import com.xin.framework.xinframwork.store.entity.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * 使用objectBox,具体使用文档参照：http://objectbox.io/documentation/entity-annotations/
 * Description : 数据库配置
 * Created by xin on 2017/9/14 0014.
 */

public class DBConfig {
    private static BoxStore boxStore;


    public static void init(Context app) {

        boxStore = MyObjectBox.builder().androidContext(app).build();
        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(boxStore).start(app);

            /*
             the object browser comes with a notification to quickly open it on your device.
             Running on your dev machine
             To open it on your development machine, check the Android logcat logs.
             It will print the port and the ADB command needed to do the port forwarding.
             Because it usually uses port 8090, the command to run usually is:
             adb forward tcp:8090 tcp:8090
             adb forward tcp:8090 tcp:8090
             Once the port forwarding is in place you can open the browser using “http://localhost:8090/index.html” on the big screen.

             */
        }

    }

    public static BoxStore getBoxStore() {

        if (boxStore == null)
            throw new NullPointerException("did not init DBConfig");

        return boxStore;
    }


}
