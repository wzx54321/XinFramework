package com.xin.framework.xinframwork.utils.android;

import android.app.Activity;
import android.os.Handler;

import java.util.Stack;

/**
 * 描述：Activity 堆栈管理类
 *
 * @author Xin
 * @since JDK1.8
 */
public class ActivityStackManager {

    private static Stack<Activity> activityStack;
    private static ActivityStackManager instance;

    private ActivityStackManager() {
    }

    public static ActivityStackManager getInstance() {
        if (instance == null) {
            synchronized (ActivityStackManager.class) {
                if (instance == null) {

                    instance = new ActivityStackManager();
                }
            }
        }
        return instance;
    }

    public synchronized void popCurrentActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public synchronized void popActivity(Activity activity) {
        if (activity != null) {
            if (activityStack.remove(activity)) {
                activity.finish();
            }
        }
    }

    public synchronized Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public synchronized void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public synchronized void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass()
                    .equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    public synchronized void popActivity(Class cls) {
        try {
            Activity actLast = null;
            for (Activity activity : activityStack) {
                if (activity.getClass()
                        .equals(cls)) {
                    actLast = activity;
                    activity.finish();

                }
            }
            activityStack.remove(actLast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void popAllActivityExceptLast(Class cls) {


        Activity actLast = null;
        while (activityStack.size() >= 1) {
            Activity activity = currentActivity();
            if (!activity.getClass()
                    .equals(cls)) {
                activity.finish();
            } else {
                actLast = activity;
            }
            activityStack.remove(activity);
        }
        if (actLast != null)
            activityStack.add(actLast);
    }

    public synchronized void popAllActivity() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                while (!activityStack.isEmpty()) {
                    Activity activity = activityStack.lastElement();
                    if (activity != null) {
                        activity.finish();
                        activityStack.removeElement(activity);
                    }
                }
            }
        });

    }

    public synchronized void removeCurrentActivity(Activity activity) {
        if (activityStack != null && activity != null) {
            activityStack.remove(activity);
        }
    }

    public synchronized boolean contains(Class cls) {

        for (Activity activity : activityStack) {
            if (activity.getClass()
                    .equals(cls)) {
                return true;
            }
        }
        return false;
    }
}
