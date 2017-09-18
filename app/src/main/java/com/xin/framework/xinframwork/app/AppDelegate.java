package com.xin.framework.xinframwork.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.xin.framework.xinframwork.BuildConfig;
import com.xin.framework.xinframwork.common.AppConfig;
import com.xin.framework.xinframwork.common.CrashReportConfig;
import com.xin.framework.xinframwork.common.DBConfig;
import com.xin.framework.xinframwork.common.FileConfig;
import com.xin.framework.xinframwork.common.HttpConfig;
import com.xin.framework.xinframwork.common.NetWorkConfig;
import com.xin.framework.xinframwork.http.glide.base.GlideApp;
import com.xin.framework.xinframwork.utils.android.ActivityStackManager;
import com.xin.framework.xinframwork.utils.android.SysUtils;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.android.logger.LogLevel;
import com.xin.framework.xinframwork.utils.android.logger.MemoryLog;

import io.objectbox.BoxStore;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/17 0017.
 */

class AppDelegate implements Application.ActivityLifecycleCallbacks {

    private XinApplication app;
    private static int appCreateCount;
    private BoxStore boxStore;


    public AppDelegate(XinApplication app) {
        this.app = app;
    }

    public void onCreate() {

        if (!app.getApplicationInfo().packageName.equals(SysUtils.getCurProcessName(app)))
            return;
        appCreateCount = 0;

        //  创建或更新数据库
        DBConfig.init(app);

        // Log 配置
        Log.init().logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);

        // 配置： ANR异常捕获 内存泄露捕获
        if (!LeakCanary.isInAnalyzerProcess(app)) {
            BlockCanary.install(app, new AppBlockCanaryContext()).start();
            LeakCanary.install(app);
        }

        // init  CrashReport
        CrashReportConfig.init(app);
        //  Device ID
        AppConfig.setDeviceId(app);
        //  渠道号
        AppConfig.setChannel(app);



        // 生命周期
        app.registerActivityLifecycleCallbacks(this);


        // 配置网络请求
        HttpConfig.init(app);

    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (MemoryLog.DEBUG_MEMORY) {// 堆栈和内存使用log
            MemoryLog.printMemory(activity.getClass().getName() + "-->onCreate");
        }
        ActivityStackManager.getInstance().pushActivity(activity);

        if (appCreateCount == 0) {
            // 配置文件系统
            new FileConfig().init(activity);

            if (SysUtils.hasNougat()) {
                // 配置网络监听
                NetWorkConfig.initNetNotify(activity);
            }
        }
        appCreateCount++;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityStackManager.getInstance().popActivity(activity);
    }


    public void onLowMemory() {
        GlideApp.get(app).onLowMemory();
    }

    public void onTrimMemory(int level) {
        GlideApp.get(app).onTrimMemory(level);
    }

    public void exit() {
        try {
            ActivityStackManager.getInstance().popAllActivity();
            ActivityManager activityMgr =
                    (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(app.getPackageName());
            System.exit(0);
        } catch (Exception er) {
            Log.e(er, "exit app error");
        }

    }
}
