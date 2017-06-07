package com.xin.framework.xinframwork.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xin.framework.utils.android.view.ViewFinder;
import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.ui.widget.titlebar.utils.TitleCompatibilityUtil;
import com.xin.framework.xinframwork.ui.widget.titlebar.view.TitleBar;

import kr.co.namee.permissiongen.PermissionGen;
import me.framework.fragmentation.ActivitySupport;

/**
 * 基类
 * Created by 鑫 on 2017/5/11 0011.<p>
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
 * @see BaseActivity#checkPermission(int, String...)
 * @see PermissionGen#doExecuteSuccess(Object, int)
 * @see PermissionGen#doExecuteFail(Object, int)
 */
public abstract class BaseActivity extends ActivitySupport {

    public final static int PERMISSION_REQUEST_CODE = 1000;
    public boolean mIsImmersive;
    public TitleBar mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsImmersive = TitleCompatibilityUtil.full(this);
        setContentView(getLayoutId());
        mTitle = ViewFinder.findViewById(this, R.id.title_bar);
        initTitleBar();

        initView();


    }

    /**
     * 6.0以上系统权限请求
     */
    public void checkPermission(int requestCode, String... permissions) {
        PermissionGen.with(this)
                .addRequestCode(requestCode)
                .permissions(permissions)
                .request();
    }


    protected abstract void initView();

    protected abstract int getLayoutId();

    protected abstract void initTitleBar();


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

}
