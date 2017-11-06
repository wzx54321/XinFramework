package com.xin.framework.xinframwork.utils.android.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.KeyCharacterMap;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.xin.framework.xinframwork.utils.android.SysUtils;

import java.lang.reflect.Field;
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
     * @return is running Flyme
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
     * @return  is MIUI
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


    /**
     * 修改状态栏为全透明
     *
     * @param window
     */
    public static void full(Window window) {
        if (SysUtils.hasKitKat()) {
            if (MIUISetStatusBarLightMode(window, true)) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else if (FlymeSetStatusBarLightMode(window, true)) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else if (SysUtils.hasLollipop()) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }


    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }


    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }




    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        int id = Resources.getSystem().getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = Resources.getSystem().getBoolean(id);
        }

        try {
            Class e = Class.forName("android.os.SystemProperties");
            Method m = e.getMethod("get", new Class[]{String.class});
            String navBarOverride = (String) m.invoke(e, new Object[]{"qemu.hw.mainkeys"});
            hasNavigationBar = "0".equals(navBarOverride);
        } catch (Exception var5) {
            var5.getStackTrace();
        }

        return hasNavigationBar;
    }


    public static int getNavigationBarHeight(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(4);
        if (!hasMenuKey && !hasBackKey) {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }


    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void StatusBarDarkMode(Window window, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(window, false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(window, false);
        } else if (type == 3) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param window
     * @param type   1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Window window, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(window, true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(window, true);
        } else if (type == 3) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param window
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Window window) {
        int result = 0;
        if (SysUtils.hasKitKat()) {
            if (MIUISetStatusBarLightMode(window, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(window, true)) {
                result = 2;
            } else if (SysUtils.hasM()) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    public static int StatusBarDarkMode(Window window) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(window, false)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(window, false)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                result = 3;
            }
        }
        return result;
    }


    public static boolean supportStatusBarLightMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = ((Activity) context).getWindow();
            if ( MIUISetStatusBarLightMode(window, true)
                    || FlymeSetStatusBarLightMode(window, true)
                    || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return true;
            }
        }
        return false;
    }

    public static boolean supportStatusBarLightMode(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ( MIUISetStatusBarLightMode(window, true)
                    || FlymeSetStatusBarLightMode(window, true)
                    || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return true;
            }
        }
        return false;
    }

}
