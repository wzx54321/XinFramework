package com.xin.framework.xinframwork.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.mvp.IPresenter;
import com.xin.framework.xinframwork.mvp.IView;
import com.xin.framework.xinframwork.mvp.TypeUtil;
import com.xin.framework.xinframwork.ui.widget.titlebar.utils.TitleCompatibilityUtil;
import com.xin.framework.xinframwork.ui.widget.titlebar.view.TitleBar;
import com.xin.framework.xinframwork.utils.android.view.ViewFinder;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.framework.fragmentation.ActivitySupport;

/**
 * 基类
 * Created by 鑫 on 2017/5/11 0011.<p>
 */
public abstract class BaseActivity<P extends IPresenter> extends ActivitySupport {
    protected P mPresenter;
    public boolean mIsImmersive;
    @Nullable
    public TitleBar mTitle;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = TypeUtil.getT(this, 0);
        super.onCreate(savedInstanceState);
        mIsImmersive = TitleCompatibilityUtil.full(this);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        mTitle = ViewFinder.findViewById(this, R.id.title_bar);
        initTitleBar();
        initView();
        if (this instanceof IView)
            mPresenter.setView((IView) this);

        afterCreated();
    }

    /**
     * 创建activity以后
     */
    protected abstract void afterCreated();


    protected abstract void initView();

    protected abstract int getLayoutId();

    @Nullable
    protected abstract void initTitleBar();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        mPresenter.onDestroy();
        this.mPresenter = null;
        this.mUnbinder = null;
    }
}
