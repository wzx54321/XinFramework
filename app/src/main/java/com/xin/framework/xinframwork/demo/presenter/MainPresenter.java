package com.xin.framework.xinframwork.demo.presenter;

import com.xin.framework.xinframwork.demo.api.ApiMethod;
import com.xin.framework.xinframwork.demo.api.XinRequest;
import com.xin.framework.xinframwork.demo.bean.AppVersion;
import com.xin.framework.xinframwork.demo.contract.MainContract;
import com.xin.framework.xinframwork.http.callback.CustomRequestCallback;
import com.xin.framework.xinframwork.http.model.CustomData;
import com.xin.framework.xinframwork.mvp.IView;
import com.xin.framework.xinframwork.mvp.PresenterMessage;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/25 0025.
 */

public class MainPresenter implements MainContract.Presenter {
    MainContract.View mView;

    @Override
    public void onStart() {
        // TODO something
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void setView(IView iview) {
        mView = (MainContract.View) iview;
    }

    @Override
    public void checkVersion(PresenterMessage msg) {
        new XinRequest<AppVersion>(AppVersion.class).Post(ApiMethod.API_CHECK_VER, null, new CustomRequestCallback<AppVersion>() {
            @Override
            public void onBeforeRequest(@NonNull Disposable disposable) {
                Log.i("onBeforeRequest");
            }

            @Override
            public void onSuccess(@NonNull CustomData<AppVersion> resault) {


                AppVersion appVersion = resault.data;
                Log.i("onSuccess");
            }


            @Override
            public void onError(@NonNull Throwable e) {
                Log.printStackTrace(e);
            }

            @Override
            public void onComplete() {
                Log.i("onComplete");
            }
        });
    }


}
