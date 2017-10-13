package me.framework.fragmentation.helper.internal;

import android.os.Bundle;

import java.util.ArrayList;

import me.framework.fragmentation.FragmentSupport;
import me.framework.fragmentation.helper.FragmentLifecycleCallbacks;


/**
 * 用于处理Fragment生命周期的帮助类
 */
public class LifecycleHelper {
    public static final int LIFECYCLE_ON_SAVE_INSTANCE_STATE = 0;
    public static final int LIFECYCLE_ON_ENTER_ANIMATION_END = 1;
    public static final int LIFECYCLE_ON_LAZY_INIT_VIEW = 2;
    public static final int LIFECYCLE_ON_SUPPORT_VISIBLE = 3;
    public static final int LIFECYCLE_ON_SUPPORT_INVISIBLE = 4;
    public static final int LIFECYCLE_ON_ATTACH = 5;
    public static final int LIFECYCLE_ON_CREATE = 6;
    //public static final int LIFECYCLE_ON_CREATE_VIEW = 7;
    public static final int LIFECYCLE_ON_VIEW_CREATED = 8;
    public static final int LIFECYCLE_ON_ACTIVITY_CREATED = 9;
    public static final int LIFECYCLE_ON_START = 10;
    public static final int LIFECYCLE_ON_RESUME = 11;
    public static final int LIFECYCLE_ON_PAUSE = 12;
    public static final int LIFECYCLE_ON_STOP = 13;
    public static final int LIFECYCLE_ON_DESTROY_VIEW = 14;
    public static final int LIFECYCLE_ON_DESTROY = 15;
    public static final int LIFECYCLE_ON_DETACH = 16;

    private ArrayList<FragmentLifecycleCallbacks> mFragmentLifecycleCallbacks;

    public LifecycleHelper(ArrayList<FragmentLifecycleCallbacks> fragmentLifecycleCallbacks) {
        this.mFragmentLifecycleCallbacks = fragmentLifecycleCallbacks;
    }

    public void dispatchLifecycle(int lifecycle, FragmentSupport fragment, Bundle bundle, boolean visible) {
        switch (lifecycle) {
            case LIFECYCLE_ON_SAVE_INSTANCE_STATE:
                dispatchFragmentSaveInstanceState(fragment, bundle);
                break;
            case LIFECYCLE_ON_ENTER_ANIMATION_END:
                dispatchFragmentEnterAnimationDispatchEnd(fragment, bundle);
                break;
            case LIFECYCLE_ON_LAZY_INIT_VIEW:
                dispatchFragmentLazyInitView(fragment, bundle);
                break;
            case LIFECYCLE_ON_SUPPORT_VISIBLE:
                dispatchFragmentSupportVisible(fragment);
                break;
            case LIFECYCLE_ON_SUPPORT_INVISIBLE:
                dispatchFragmentSupportInvisible(fragment);
                break;
            case LIFECYCLE_ON_ATTACH:
                dispatchFragmentAttached(fragment);
                break;
            case LIFECYCLE_ON_CREATE:
                dispatchFragmentCreated(fragment, bundle);
                break;
//            case LIFECYCLE_ON_CREATE_VIEW:
//                dispatchFragmentCreateView(fragment, bundle);
//                break;
            case LIFECYCLE_ON_VIEW_CREATED:
                dispatchFragmentViewCreated(fragment, bundle);
                break;
            case LIFECYCLE_ON_ACTIVITY_CREATED:
                dispatchFragmentActivityCreated(fragment, bundle);
                break;
            case LIFECYCLE_ON_START:
                dispatchFragmentStarted(fragment);
                break;
            case LIFECYCLE_ON_RESUME:
                dispatchFragmentResumed(fragment);
                break;
            case LIFECYCLE_ON_PAUSE:
                dispatchFragmentPaused(fragment);
                break;
            case LIFECYCLE_ON_STOP:
                dispatchFragmentStopped(fragment);
                break;
            case LIFECYCLE_ON_DESTROY_VIEW:
                dispatchFragmentDestroyView(fragment);
                break;
            case LIFECYCLE_ON_DESTROY:
                dispatchFragmentDestroyed(fragment);
                break;
            case LIFECYCLE_ON_DETACH:
                dispatchFragmentDetached(fragment);
                break;
        }
    }

    private void dispatchFragmentSaveInstanceState(FragmentSupport fragment, Bundle outState) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentSaveInstanceState(fragment, outState);
        }
    }

    private void dispatchFragmentEnterAnimationDispatchEnd(FragmentSupport fragment, Bundle savedInstanceState) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentEnterAnimationEnd(fragment, savedInstanceState);
        }
    }

    private void dispatchFragmentLazyInitView(FragmentSupport fragment, Bundle savedInstanceState) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentLazyInitView(fragment, savedInstanceState);
        }
    }

    private void dispatchFragmentSupportVisible(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentSupportVisible(fragment);
        }
    }

    private void dispatchFragmentSupportInvisible(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentSupportInvisible(fragment);
        }
    }

    private void dispatchFragmentAttached(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentAttached(fragment);
        }
    }

    private void dispatchFragmentCreated(FragmentSupport fragment, Bundle savedInstanceState) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentCreated(fragment, savedInstanceState);
        }
    }

//    private void dispatchFragmentCreateView(SupportFragment fragment, Bundle savedInstanceState) {
//        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
//            mFragmentLifecycleCallbacks.get(i).onFragmentCreateView(fragment, savedInstanceState);
//        }
//    }

    private void dispatchFragmentViewCreated(FragmentSupport fragment, Bundle savedInstanceState) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentViewCreated(fragment, savedInstanceState);
        }
    }

    private void dispatchFragmentActivityCreated(FragmentSupport fragment, Bundle savedInstanceState) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentActivityCreated(fragment, savedInstanceState);
        }
    }

    private void dispatchFragmentStarted(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentStarted(fragment);
        }
    }

    private void dispatchFragmentResumed(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentResumed(fragment);
        }
    }

    private void dispatchFragmentPaused(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentPaused(fragment);
        }
    }

    private void dispatchFragmentStopped(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentStopped(fragment);
        }
    }

    private void dispatchFragmentDestroyView(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentDestroyView(fragment);
        }
    }

    private void dispatchFragmentDestroyed(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentDestroyed(fragment);
        }
    }

    private void dispatchFragmentDetached(FragmentSupport fragment) {
        for (int i = 0; i < mFragmentLifecycleCallbacks.size(); i++) {
            mFragmentLifecycleCallbacks.get(i).onFragmentDetached(fragment);
        }
    }
}
