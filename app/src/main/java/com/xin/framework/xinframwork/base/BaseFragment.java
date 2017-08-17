package com.xin.framework.xinframwork.base;

import android.os.Bundle;

import me.framework.fragmentation.FragmentSupport;

/**
 * 基类
 * <p>
 * 权限使用子类根据 requestCode去实现<p>
 * <code><pre><p>
 * {@literal @}PermissionSuccess(requestCode = requestCode)
 * public void doSomething() {
 * <p>
 * }
 *  {@literal @}PermissionSuccess(requestCode = PERMISSION_REQUEST_CODE)
 * PermissionFail(requestCode = requestCode)
 * public void doFailSomething() {
 * <p>
 * }
 *  </code></pre></p>
 *
 */
public abstract class BaseFragment extends FragmentSupport {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }




}
