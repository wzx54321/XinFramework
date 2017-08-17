package com.xin.framework.xinframwork.ui.widget.titlebar.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xin.framework.xinframwork.utils.android.SysUtils;

import java.lang.reflect.Method;

/**
 * Compatibility for special ROM
 * <p/>
 * Created by tigerliang on 2015/10/12.
 */
public class TitleCompatibilityUtil {

    private static final String TAG = "CompatibilityUtil";

    /**
     * Check whether is running Flyme
     *
     * @return
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Check whether is MIUI
     *
     * @return
     */
    public static boolean isMIUI() {
        String miuiVer = TitlePropertyUtils.getQuickly("ro.miui.ui.version.name", null);
        return miuiVer != null && miuiVer.contains("V");
    }

    /**
     * Get MIUI version
     *
     * @return the MIUI version or -1 if failed to get this value.
     */
    public static int getMIUIVersion() {
        int result = -1;
        String miuiVer = TitlePropertyUtils.getQuickly("ro.miui.ui.version.name", null);
        if (miuiVer != null && miuiVer.contains("V")) {
            int versionStart = miuiVer.indexOf("V") + 1;
            if (versionStart < miuiVer.length()) {
                String version = miuiVer.substring(versionStart);
                try {
                    result = Integer.parseInt(version);
                } catch (NumberFormatException e) {
                    //omit
                }
            }
        }
        return result;
    }

    public static boolean full(Activity activity) {
        if (SysUtils.hasKitKat() && !SysUtils.hasLollipop()) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            return true;
        } else if (SysUtils.hasLollipop()) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            return true;
        }

        return false;
    }
}
