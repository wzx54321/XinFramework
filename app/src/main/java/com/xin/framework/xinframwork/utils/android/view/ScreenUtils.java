package com.xin.framework.xinframwork.utils.android.view;

import android.content.Context;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Description : 屏幕工具
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class ScreenUtils {

    public static float density;
    private static int widthPixels;
    private static int heightPixels;


    public static void init(Context context) {
        DisplayMetrics sDisplayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = sDisplayMetrics.widthPixels;
        heightPixels = sDisplayMetrics.heightPixels;
        density = sDisplayMetrics.density;
    }


    public static int dp2px(float dpValue) {
        return (int) (dpValue * density + 0.5F);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / density + 0.5F);
    }


    public static int dp2PxInt(float dp) {
        return (int) (dp2px(dp) + 0.5f);
    }

    public static float px2DpCeilInt(float px) {
        return (int) (px2dp(px) + 0.5f);
    }


    public static int[] getScreenInfo() {
        int[] info = new int[]{widthPixels, heightPixels};
        return info;
    }

    public static void hideSoftInputKeyBoard(Context context, View focusView) {
        if (focusView != null) {
            IBinder binder = focusView.getWindowToken();
            if (binder != null) {
                InputMethodManager imd = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imd.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static void showSoftInputKeyBoard(Context context, View focusView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
    }


}
