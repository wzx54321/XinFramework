package com.xin.framework.xinframwork.common;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.xin.framework.xinframwork.BuildConfig;
import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.utils.android.SysUtils;
import com.xin.framework.xinframwork.utils.android.logger.CrashLogger;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

import static com.xin.framework.xinframwork.app.XinApplication.getAppContext;

/**
 * Description :Crash报告配置
 * Created by xin on 2017/8/17 0017.
 */

public class CrashReportConfig {


    public static  void init( Context app) {
        // 初始化Bugly
        if (!BuildConfig.DEBUG) {
            // 获取当前包名
            String packageName = app.getPackageName();
            // 获取当前进程名
            String processName = SysUtils.getProcessName(android.os.Process.myPid());
            // 设置是否为上报进程
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(app);
            strategy.setUploadProcess(processName == null || processName.equals(packageName));
            CrashReport.initCrashReport(app.getApplicationContext(),app.getString(R.string.bugly_app_id), false, strategy);
        } else {
            if (app.getResources().getBoolean(R.bool.crash_logger_enable)) {
                Thread.currentThread()
                        .setUncaughtExceptionHandler(new CrashLogger(getAppContext()));
            } else {
                try{
                    CustomActivityOnCrash.install(app);
                }catch (Exception e){
                    Log.e(e,"CrashReportConfig on CustomActivityOnCrash init");
                }

            }

        }

    }
}
