package com.xin.framework.xinframwork.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.ui.widget.titlebar.utils.TitleCompatibilityUtil;
import com.xin.framework.xinframwork.ui.widget.titlebar.view.TitleBar;
import com.xin.framework.xinframwork.utils.android.view.ViewFinder;

import me.framework.fragmentation.ActivitySupport;

/**
 * 基类
 * Created by 鑫 on 2017/5/11 0011.<p>
 */
public abstract class BaseActivity extends ActivitySupport {

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


    protected abstract void initView();

    protected abstract int getLayoutId();

    protected abstract void initTitleBar();


}
