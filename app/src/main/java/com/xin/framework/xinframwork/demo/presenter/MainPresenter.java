package com.xin.framework.xinframwork.demo.presenter;

import com.xin.framework.xinframwork.demo.bean.AppVersion;
import com.xin.framework.xinframwork.demo.contract.MainContract;
import com.xin.framework.xinframwork.demo.model.MainModel;
import com.xin.framework.xinframwork.http.callback.CustomRequestCallback;
import com.xin.framework.xinframwork.http.model.CustomData;
import com.xin.framework.xinframwork.mvp.Iv;
import com.xin.framework.xinframwork.mvp.PresenterMessage;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Description :MainPresenter
 * Created by xin on 2017/8/25 0025.
 */

public class MainPresenter implements MainContract.Presenter {
    MainContract.View mView;
    MainContract.Model model;

    @Override
    public void onStart() {


        model = new MainModel();
    }

    @Override
    public void onDestroy() {
        model.onDestroy();
        mView = null;
    }

    @Override
    public void setView( Iv iview) {
        mView = (MainContract.View) iview;
    }

    @Override
    public void checkVersion(PresenterMessage msg) {


        model.checkVersion(new CustomRequestCallback<AppVersion>() {
            @Override
            public void onBeforeRequest(@NonNull Disposable disposable) {

            }

            @Override
            public void onSuccess(@NonNull CustomData<AppVersion> result) {
                @SuppressWarnings("UnusedAssignment") AppVersion appVersion = result.data;
            }


            @Override
            public void onError(@NonNull Throwable e) {
                Log.printStackTrace(e);
            }

            @Override
            public void onComplete() {
            }
        });


    }


}
