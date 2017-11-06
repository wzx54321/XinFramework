package com.xin.framework.xinframwork.utils.android.view;

import android.graphics.Color;

/**
 * Description :
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class ColorUtils {


    public static int interpolateColor(int colorFrom, int colorTo, int posFrom, int posTo) {
        float delta = posTo - posFrom;
        int red = (int) ((Color.red(colorFrom) - Color.red(colorTo)) * delta / posTo + Color.red(colorTo));
        int green = (int) ((Color.green(colorFrom) - Color.green(colorTo)) * delta / posTo + Color.green(colorTo));
        int blue = (int) ((Color.blue(colorFrom) - Color.blue(colorTo)) * delta / posTo) + Color.blue(colorTo);
        return Color.argb(255, red, green, blue);
    }
}
