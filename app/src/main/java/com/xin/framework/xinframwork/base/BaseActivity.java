package com.xin.framework.xinframwork.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xin.framework.xinframwork.mvp.IPresenter;
import com.xin.framework.xinframwork.mvp.Iv;
import com.xin.framework.xinframwork.mvp.PresenterMessage;
import com.xin.framework.xinframwork.mvp.TypeUtil;
import com.xin.framework.xinframwork.ui.widget.titlebar.view.TitleBar;
import com.xin.framework.xinframwork.utils.android.view.KeyBoardConflictCompat;
import com.xin.framework.xinframwork.utils.android.view.TitleCompatibilityUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.framework.fragmentation.ActivitySupport;

/**
 * 基类
 * Created by 鑫 on 2017/5/11 0011.<p>
 */
public abstract class BaseActivity<P extends IPresenter> extends ActivitySupport {
    protected P mPresenter;
    @Nullable
    public TitleBar mTitle;
    private Unbinder mUnBinder;
    public PresenterMessage msg;

    public abstract void createMessage();


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        TitleCompatibilityUtil.full( getWindow());
        TitleCompatibilityUtil.StatusBarLightMode(getWindow());
        KeyBoardConflictCompat.assistActivity(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = TypeUtil.getT(this, 0);
        super.onCreate(savedInstanceState);
        if (getLayoutId() == 0) {
            throw new ExceptionInInitializerError("Activity 没有实现 getLayoutId()方法");
        }
        setContentView(getLayoutId());
        mUnBinder = ButterKnife.bind(this);
        initView();
        if (mPresenter != null && (this instanceof Iv)) {
            mPresenter.setView((Iv) this);
            createMessage();
            mPresenter.onStart();
        }
        afterCreated();
    }

    /**
     * 创建activity以后
     */
    protected abstract void afterCreated();


    protected abstract void initView();

    protected abstract int getLayoutId();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null && mUnBinder != Unbinder.EMPTY) mUnBinder.unbind();
        if (mPresenter != null) mPresenter.onDestroy();
        this.mPresenter = null;
        this.mUnBinder = null;
    }
}
