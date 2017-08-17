package com.xin.framework.xinframwork.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.github.moduth.blockcanary.BlockCanary;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.squareup.leakcanary.LeakCanary;
import com.xin.framework.xinframwork.BuildConfig;
import com.xin.framework.xinframwork.common.CrashReportConfig;
import com.xin.framework.xinframwork.common.FileConfig;
import com.xin.framework.xinframwork.common.NetWorkConfig;
import com.xin.framework.xinframwork.content.SPManager;
import com.xin.framework.xinframwork.utils.android.SysUtils;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.android.logger.LogLevel;
import com.xin.framework.xinframwork.utils.glide.GlideApp;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/17 0017.
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
        CrashReportConfig.init(app);

        //  Device ID
        setDeviceId();

        // TODO 创建或更新数据库
        // TODO 添加Activity栈管理
        app.registerActivityLifecycleCallbacks(this);
    }

    private void setDeviceId() {
        if (TextUtils.isEmpty(SPManager.getInstance().getDeviceId())) {
            SPManager.getInstance().putDeviceId(SysUtils.getDeviceId(app));
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        if (appCreateCount == 0) {
            //   配置文件系统
            new FileConfig().init(activity);

            if (SysUtils.hasNougat()) {
                //   配置网络监听
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

    }


    /**
     * 获取渠道信息
     */
    private void readChannel() {

        final long startTime = System.currentTimeMillis();
        final ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(app);

        // TODO
       /* if (channelInfo != null) {
            tv.setText(channelInfo.getChannel() + "\n" + channelInfo.getExtraInfo());
        }
        Toast.makeText(this, "ChannelReader takes " + (System.currentTimeMillis() - startTime) + " milliseconds", Toast.LENGTH_SHORT).show();*/
    }

    public void onLowMemory() {
        GlideApp.get(app).onLowMemory();
    }

    public void onTrimMemory(int level) {
        GlideApp.get(app).onTrimMemory(level);
    }
}
