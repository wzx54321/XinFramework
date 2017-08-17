package com.xin.framework.xinframwork.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.github.moduth.blockcanary.BlockCanary;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.xin.framework.xinframwork.BuildConfig;
import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.content.SPManager;
import com.xin.framework.xinframwork.utils.android.SysUtils;
import com.xin.framework.xinframwork.utils.android.logger.CrashLogger;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.android.logger.LogLevel;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

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
        // Log 配置
        Log.init().logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
        //配置： ANR异常捕获 内存泄露捕获
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            BlockCanary.install(this, new AppBlockCanaryContext()).start();
            LeakCanary.install(this);
        }
        // init bugly
        initCrashRepoter();
        // TODO 配置文件系统

        //  Device ID
        if (TextUtils.isEmpty(SPManager.getInstance().getDeviceId())) {
            SPManager.getInstance().putDeviceId(SysUtils.getDeviceId(sAppContext));
        }
        // TODO 创建或更新数据库
        // TODO 网络监听

    }

    private void initCrashRepoter() {
        // 初始化Bugly
        if (!BuildConfig.DEBUG) {
            // 获取当前包名
            String packageName = getPackageName();
            // 获取当前进程名
            String processName = SysUtils.getProcessName(android.os.Process.myPid());
            // 设置是否为上报进程
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
            strategy.setUploadProcess(processName == null || processName.equals(packageName));
            CrashReport.initCrashReport(getApplicationContext(), getString(R.string.bugly_app_id), false, strategy);
        } else {
            if (getResources().getBoolean(R.bool.crash_loger_enable)) {
                Thread.currentThread()
                        .setUncaughtExceptionHandler(new CrashLogger(getAppContext()));
            } else {
                CustomActivityOnCrash.install(this);
            }

        }

    }


    private void readChannel() {

        final long startTime = System.currentTimeMillis();
        final ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(this.getApplicationContext());
       /* if (channelInfo != null) {
            tv.setText(channelInfo.getChannel() + "\n" + channelInfo.getExtraInfo());
        }
        Toast.makeText(this, "ChannelReader takes " + (System.currentTimeMillis() - startTime) + " milliseconds", Toast.LENGTH_SHORT).show();*/
    }
}
