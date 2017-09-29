package com.xin.framework.xinframwork.common;

import android.app.Activity;
import android.os.Environment;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.utils.android.PermissionUtil;
import com.xin.framework.xinframwork.utils.android.SysUtils;
import com.xin.framework.xinframwork.utils.common.utils.FileUtil;
import com.xin.framework.xinframwork.utils.common.utils.SdCardUtil;

import java.io.File;

/**
 * Description : 文件路径配置
 * Created by 王照鑫 on 2017/7/10 0010.
 */

public class FileConfig {

    /**
     * 应用程序在SDCARD中的根目录，/mnt/sdcard/
     */
    public static String DIR_PUBLLIC_ROOT = XinApplication.getAppContext().getString(R.string.app_name);

    /**
     * 崩溃日志存储目录
     */
    public static String DIR_CRASH = "crash";

    /**
     * 下载目录
     */
    public static String DIR_DOWNLOAD = "download";

    /**
     * 日志文件名后缀
     */
    public static String FILE_NAME_EXTENSION_LOG = ".log";
    /**
     * jpg文件名后缀
     */
    public static String FILE_NAME_EXTENSION_JPG = ".jpg";
    /**
     * WEBP文件名后缀
     */
    public static String FILE_NAME_EXTENSION_WEBP = ".WEBP";

    /**
     * png文件名后缀
     */
    public static String FILE_NAME_EXTENSION_PNG = ".png";
    /**
     * apk文件名后缀
     */
    public static String FILE_NAME_EXTENSION_APK = ".apk";

    /**
     * json文件名后缀
     */
    public static String FILE_NAME_EXTENSION_JSON = ".json";

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

    public void init(final Activity activity) {
        if (SysUtils.hasLollipop()) {


            PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
                @Override
                public void onRequestPermissionSuccess() {
                    createParentDir();
                }

                @Override
                public void onRequestPermissionFailure() {
                    // 没有创建权限
                }
            }, new RxPermissions(activity), null);
        } else {
            createParentDir();
        }


    }

    private static void createParentDir() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                if (SdCardUtil.isSdCardAvailable()) {// 创建外置根目录,.nomedia文件
                    FileUtil.createNewFileAndParentDir(new File(getPublicDir(DIR_PUBLLIC_ROOT),
                            ".nomedia"));
                }
            }
        }).start();


    }
}
