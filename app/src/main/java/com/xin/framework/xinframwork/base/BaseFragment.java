package com.xin.framework.xinframwork.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xin.framework.xinframwork.mvp.IPresenter;
import com.xin.framework.xinframwork.mvp.Iv;
import com.xin.framework.xinframwork.mvp.TypeUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.framework.fragmentation.FragmentSupport;

/**
 * 基类
 * create by xin
 */
public abstract class BaseFragment<P extends IPresenter> extends FragmentSupport {
    protected P mPresenter;
    private Unbinder mUnBinder;
    @Nullable
    protected View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = TypeUtil.getT(this, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mRootView = setLayout(inflater);
        if (mRootView != null) {
            mUnBinder = ButterKnife.bind(this, mRootView);
        }
        if (this instanceof Iv && mPresenter != null) mPresenter.setView((Iv) this);


        initView();
        return mRootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null)
            mPresenter.onStart();

        onPresenterStarted();
    }

    @Nullable
    protected abstract View setLayout(LayoutInflater inflater);
    protected abstract void initView();
    protected abstract void onPresenterStarted();




    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null && mUnBinder != Unbinder.EMPTY) mUnBinder.unbind();
        mPresenter.onDestroy();
        this.mPresenter = null;
        this.mUnBinder = null;
    }


}
