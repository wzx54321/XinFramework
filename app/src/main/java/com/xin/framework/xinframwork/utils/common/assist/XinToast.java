package com.xin.framework.xinframwork.utils.common.assist;

import android.content.Context;

/**
 * Toast
 */
public class XinToast {

    private android.widget.Toast mToast;
    private Context context;

    public XinToast(Context context) {
        this.context = context.getApplicationContext();
    }

    public android.widget.Toast getSingletonToast(int resId) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(context, resId, android.widget.Toast.LENGTH_SHORT);
        }else{
            mToast.setText(resId);
        }
        return mToast;
    }

    public android.widget.Toast getSingletonToast(String text) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT);
        }else{
            mToast.setText(text);
        }
        return mToast;
    }

    public android.widget.Toast getSingleLongToast(int resId) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(context, resId, android.widget.Toast.LENGTH_LONG);
        }else{
            mToast.setText(resId);
        }
        return mToast;
    }

    public android.widget.Toast getSingleLongToast(String text) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_LONG);
        }else{
            mToast.setText(text);
        }
        return mToast;
    }

    public android.widget.Toast getToast(int resId) {
        return android.widget.Toast.makeText(context, resId, android.widget.Toast.LENGTH_SHORT);
    }

    public android.widget.Toast getToast(String text) {
        return android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT);
    }

    public android.widget.Toast getLongToast(int resId) {
        return android.widget.Toast.makeText(context, resId, android.widget.Toast.LENGTH_LONG);
    }

    public android.widget.Toast getLongToast(String text) {
        return android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_LONG);
    }

    public void showSingletonToast(int resId) {
        getSingletonToast(resId).show();
    }


    public void showSingletonToast(String text) {
        getSingletonToast(text).show();
    }

    public void showSingleLongToast(int resId) {
        getSingleLongToast(resId).show();
    }


    public void showSingleLongToast(String text) {
        getSingleLongToast(text).show();
    }

    public void showToast(int resId) {
        getToast(resId).show();
    }

    public void showToast(String text) {
        getToast(text).show();
    }

    public void showLongToast(int resId) {
        getLongToast(resId).show();
    }

    public void showLongToast(String text) {
        getLongToast(text).show();
    }

}
