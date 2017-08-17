package com.xin.framework.xinframwork.common;

import android.os.Environment;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.app.App;

import java.io.File;

/**
 * Description : 文件路径配置
 * Created by 王照鑫 on 2017/7/10 0010.
 */

public class FileConfig {

    /**
     * 应用程序在SDCARD中的根目录，/mnt/sdcard/UnifyApp
     */
    public static String DIR_PUBLLIC_ROOT = App.getAppContext().getString(R.string.app_name);

    /**
     * 崩溃日志存储目录
     */
    public static String        DIR_CRASH                         = "crash";



    /**
     * 日志文件名后缀
     */
    public static String        FILE_NAME_EXTENSION_LOG           = ".log";
    /**
     * jpg文件名后缀
     */
    public static String        FILE_NAME_EXTENSION_JPG           = ".jpg";
    /**
     * WEBP文件名后缀
     */
    public static String        FILE_NAME_EXTENSION_WEBP          = ".WEBP";

    /**
     * png文件名后缀
     */
    public static String        FILE_NAME_EXTENSION_PNG           = ".png";
    /**
     * apk文件名后缀
     */
    public static String        FILE_NAME_EXTENSION_APK           = ".apk";

    /**
     * json文件名后缀
     */
    public static String        FILE_NAME_EXTENSION_JSON          = ".json";

    /**
     * 获取 SDCARD上的存储根目录（如果登录过，会自动追加加userId的子目录）<br>
     * ，/mnt/sdcard/mericentury/whithin
     *
     * @param type
     * @return
     */
    public static File getPublicDir(String type) {
        if (DIR_PUBLLIC_ROOT.equals(type)) {
            return Environment.getExternalStoragePublicDirectory(FileConfig.DIR_PUBLLIC_ROOT);
        } else {
            return new File(Environment.getExternalStoragePublicDirectory(FileConfig.DIR_PUBLLIC_ROOT),
                    type);
        }
    }
}
