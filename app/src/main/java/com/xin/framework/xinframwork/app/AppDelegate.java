package com.xin.framework.xinframwork.app;

import android.app.Activity;
import android.app.Application;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.text.TextUtils;

import com.github.moduth.blockcanary.BlockCanary;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.xin.framework.xinframwork.BuildConfig;
import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.common.FileConfig;
import com.xin.framework.xinframwork.content.SPManager;
import com.xin.framework.xinframwork.network.support25.NetWorkNotify;
import com.xin.framework.xinframwork.utils.android.SysUtils;
import com.xin.framework.xinframwork.utils.android.logger.CrashLogger;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.android.logger.LogLevel;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

import static com.xin.framework.xinframwork.app.XinApplication.getAppContext;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/17 0017.
 * Job number：147109
 * Email： wangzhaoxin@syswin.com
 * Person in charge :
 * Leader：guohaichun
 */

class AppDelegate implements Application.ActivityLifecycleCallbacks {

    private XinApplication app;
    private static int appCreateCount;
    public AppDelegate(XinApplication app) {
        this.app = app;
    }

    public void onCreate() {

        if (!app.getApplicationInfo().packageName.equals(SysUtils.getCurProcessName(app)))
            return;
        appCreateCount = 0;
        // Log 配置
        Log.init().logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
        //配置： ANR异常捕获 内存泄露捕获
        if (!LeakCanary.isInAnalyzerProcess(app)) {
            BlockCanary.install(app, new AppBlockCanaryContext()).start();
            LeakCanary.install(app);
        }
        // init bugly
        initCrashReporter();

        //  Device ID
        if (TextUtils.isEmpty(SPManager.getInstance().getDeviceId())) {
            SPManager.getInstance().putDeviceId(SysUtils.getDeviceId(app));
        }
        // TODO 创建或更新数据库

        //   网络监听
        if (SysUtils.hasNougat()) {
            NetWorkNotify.getInstance(getAppContext()).setNetNotifyForNougat(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    //就会连接该网络，回调 通过Network获取相关信息
                }

                // to Override ...
            });
        }


        app.registerActivityLifecycleCallbacks(this);
    }



    private void initCrashReporter() {
        // 初始化Bugly
        if (!BuildConfig.DEBUG) {
            // 获取当前包名
            String packageName = app.getPackageName();
            // 获取当前进程名
            String processName = SysUtils.getProcessName(android.os.Process.myPid());
            // 设置是否为上报进程
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(app);
            strategy.setUploadProcess(processName == null || processName.equals(packageName));
            CrashReport.initCrashReport(app.getApplicationContext(), app.getString(R.string.bugly_app_id), false, strategy);
        } else {
            if (app.getResources().getBoolean(R.bool.crash_loger_enable)) {
                Thread.currentThread()
                        .setUncaughtExceptionHandler(new CrashLogger(getAppContext()));
            } else {
                CustomActivityOnCrash.install(app);
            }

        }

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        appCreateCount++;
        if (appCreateCount == 1) {
            //   配置文件系统
            new FileConfig().init(activity);
        }
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

    }


    private void readChannel() {

        final long startTime = System.currentTimeMillis();
        final ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(app);
       /* if (channelInfo != null) {
            tv.setText(channelInfo.getChannel() + "\n" + channelInfo.getExtraInfo());
        }
        Toast.makeText(this, "ChannelReader takes " + (System.currentTimeMillis() - startTime) + " milliseconds", Toast.LENGTH_SHORT).show();*/
    }

    public void onLowMemory() {

    }
}
