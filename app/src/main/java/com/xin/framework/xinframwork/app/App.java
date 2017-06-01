package com.xin.framework.xinframwork.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.xin.framework.utils.android.SysUtils;
import com.xin.framework.xinframwork.content.SPManager;

/**
 * Description :Application
 * Created by 王照鑫 on 2017/5/16 0016.
 */

public class App extends Application {
    private static Context sAppContext = null;

    public static Context getAppContext() {
        return sAppContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
        initMainProcess();
    }

    /**
     * 主进程配置初始化，防止主进程中多次配置
     */
    private void initMainProcess() {
        if (!getApplicationInfo().packageName.equals(SysUtils.getCurProcessName(sAppContext)))
            return;
        //配置： ANR异常捕获 内存泄露捕获
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            BlockCanary.install(this, new AppBlockCanaryContext()).start();
            LeakCanary.install(this);
        }
        // init bugly
        //  CrashReport.initCrashReport(getApplicationContext(), "TODO 注册时申请的AppID", false);
        // TODO 配置文件系统

        //  Device ID
        SPManager.getInstance().putDeviceId(SysUtils.getDeviceId(sAppContext));
        // TODO 创建或更新数据库
    }
}
