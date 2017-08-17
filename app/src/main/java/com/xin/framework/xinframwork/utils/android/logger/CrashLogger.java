package com.xin.framework.xinframwork.utils.android.logger;

import android.annotation.SuppressLint;
import android.content.Context;

import com.xin.framework.xinframwork.common.FileConfig;
import com.xin.framework.xinframwork.utils.common.utils.DateUtil;
import com.xin.framework.xinframwork.utils.common.utils.FileUtil;
import com.xin.framework.xinframwork.utils.common.utils.SdCardUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 描述： 未捕获异常
 * 
 * @Author Wangzhaoxin
 * @since JDK1.8
 */
@SuppressLint("SimpleDateFormat")
public class CrashLogger implements
                        UncaughtExceptionHandler {

    private Context mContext;

    public CrashLogger(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void uncaughtException(Thread thread,
                                  Throwable ex) {
        File file;
        try {
            if ( SdCardUtil.isSdCardAvailable()) {
                file = new File(FileUtil.getPublicDir(FileConfig.DIR_CRASH),
                                "crash-app-" + new SimpleDateFormat(DateUtil.Format.CN_DEFAULT_FORMAT).format(new Date()) + FileConfig.FILE_NAME_EXTENSION_LOG);
            } else {// Android/data/
                file = new File(FileUtil.getDiskCacheDir(mContext,
                                                         "/crash"),
                                "crash-app-" + new SimpleDateFormat(DateUtil.Format.CN_DEFAULT_FORMAT).format(new Date()) + FileConfig.FILE_NAME_EXTENSION_LOG);
            }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String error = sw.toString();
            Log.e("------------------------------------uncaughtException ------------------------------------:/n",
                      error + "\n ----------------------------------------------------------------------------------");


            FileUtil.writeStringToFile(file,
                                       error,
                                       false);
            Thread.getDefaultUncaughtExceptionHandler()
                  .uncaughtException(thread,
                                     ex);
        } catch (Exception e) {
            Log .e(e,"","");
        }
    }
}
