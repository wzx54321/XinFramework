package com.xin.framework.xinframwork.utils.common.assist;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xin.framework.xinframwork.R;


/**
 * Description :Toast的工具类
 * 居中显示
 */
public class ToastPrompt {

    private static Toast sToast;

    /**
     * Toast显示一个View
     *
     * @param context
     * @param view
     */
    public static void show(Context context, View view) {
        Toast toast = getToast(context, view);
        toast.show();
    }

    /**
     * 显示一个View为TextView的Toast
     *
     * @param textResId
     */
    public static void showTextViewPrompt(Context context, @StringRes int textResId) {
        showTextViewPrompt(context, context.getString(textResId));
    }


    public static void showTextViewPrompt(Context context, String name) {
        showVerboseToast(context, name);
    }

    public static void showTextViewPromptShort(Context context, String name) {
        showVerboseToast(context, name);
    }


    public static void showImageViewPromptLong(Context context, String showContent) {
        showOkToast(context, showContent);
    }

    public static void showTextViewPromptLong(Context context, String name) {
        showVerboseToast(context, name);
    }

    /**
     * 显示正确的Toast
     *
     * @param text
     */
    public static void showOkToast(Context context, String text) {

        View view = View.inflate(context, R.layout.toast_view_prompt, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast_prompt);
        tv.setText(text);

        Toast toast = getToast(context, view);
        toast.show();
    }

    /**
     * 显示错误的Toast
     *
     * @param text
     */
    public static void showErrorToast(Context context, String text) {
        View view = View.inflate(context, R.layout.toast_view_prompt, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast_prompt);
        tv.setText(text);
        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toast_show_error, 0, 0, 0);

        Toast toast = getToast(context, view);
        toast.show();
    }

    /**
     * 显示警告的Toast
     *
     * @param text
     */
    public static void showWarnToast(Context context, String text) {
        View view = View.inflate(context, R.layout.toast_view_prompt, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast_prompt);
        tv.setText(text);
        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toast_show_warn, 0, 0, 0);

        Toast toast = getToast(context, view);
        toast.show();
    }


    /**
     * 显示View为TextView 的Toast
     *
     * @param text
     */
    public static void showVerboseToast(Context context, String text) {
        View view = View.inflate(context, R.layout.toast_view_prompt, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast_prompt);
        tv.setText(text);
        tv.setCompoundDrawables(null, null, null, null);

        Toast toast = getToast(context, view);
        toast.show();
    }

    private static Toast getToast(Context context, View view) {

        if (sToast == null) {
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            sToast = toast;
        }
        sToast.setView(view);
        return sToast;
    }

}
