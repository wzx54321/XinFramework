package com.xin.framework.xinframwork.utils.android.view;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xin.framework.xinframwork.utils.android.SysUtils;

import java.util.UUID;

/**
 * Created by Get_sugar
 * 用于查找View使用替代findViewById
 */
public class ViewFinder {

    public static <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    public static <T extends View> T findViewById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    public static <T extends View> T getView(View view) throws Exception {
        return (T) view;
    }
    /**
     * 动态设置ListView的高度
     * @param listView ListView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static int generateViewId() {
        if (SysUtils.hasJellyBeanMr1()) {
            return View.generateViewId();
        } else {
            return UUID.randomUUID().hashCode();
        }
    }
}
