package me.framework.fragmentation.helper;

import android.os.Bundle;

import me.framework.fragmentation.FragmentSupport;

/**
 * Fregment生命周期回调
 */
public class FragmentLifecycleCallbacks {

    /**
     * Called when the Fragment is called onSaveInstanceState().
     */
    public void onFragmentSaveInstanceState(FragmentSupport fragment, Bundle outState) {

    }

    /**
     * Called when the Fragment is called onEnterAnimationEnd().
     */
    public void onFragmentEnterAnimationEnd(FragmentSupport fragment, Bundle savedInstanceState) {

    }

    /**
     * Called when the Fragment is called onLazyInitView().
     */
    public void onFragmentLazyInitView(FragmentSupport fragment, Bundle savedInstanceState) {

    }

    /**
     * Called when the Fragment is called onSupportVisible().
     */
    public void onFragmentSupportVisible(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onSupportInvisible().
     */
    public void onFragmentSupportInvisible(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onAttach().
     */
    public void onFragmentAttached(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onCreate().
     */
    public void onFragmentCreated(FragmentSupport fragment, Bundle savedInstanceState) {

    }

    // 因为我们一般会移除super.onCreateView()来复写 onCreateView()  所以这里一般是捕捉不到onFragmentCreateView
//    /**
//     * Called when the Fragment is called onCreateView().
//     */
//    public void onFragmentCreateView(SupportFragment fragment, Bundle savedInstanceState) {
//
//    }

    /**
     * Called when the Fragment is called onCreate().
     */
    public void onFragmentViewCreated(FragmentSupport fragment, Bundle savedInstanceState) {

    }

    /**
     * Called when the Fragment is called onActivityCreated().
     */
    public void onFragmentActivityCreated(FragmentSupport fragment, Bundle savedInstanceState) {

    }

    /**
     * Called when the Fragment is called onStart().
     */
    public void onFragmentStarted(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onResume().
     */
    public void onFragmentResumed(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onPause().
     */
    public void onFragmentPaused(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onStop().
     */
    public void onFragmentStopped(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onDestroyView().
     */
    public void onFragmentDestroyView(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onDestroy().
     */
    public void onFragmentDestroyed(FragmentSupport fragment) {

    }

    /**
     * Called when the Fragment is called onDetach().
     */
    public void onFragmentDetached(FragmentSupport fragment) {

    }
}
