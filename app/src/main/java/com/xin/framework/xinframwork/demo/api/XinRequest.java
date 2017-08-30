package com.xin.framework.xinframwork.demo.api;

import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.common.RestApiPath;
import com.xin.framework.xinframwork.demo.callback.CustomConvert;
import com.xin.framework.xinframwork.http.OkGo;
import com.xin.framework.xinframwork.http.adapter.ObservableResponse;
import com.xin.framework.xinframwork.http.callback.CustomRequestCallback;
import com.xin.framework.xinframwork.http.model.CustomData;
import com.xin.framework.xinframwork.http.model.CustomParams;
import com.xin.framework.xinframwork.http.model.Response;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description : 根据需求封装
 * Created by 王照鑫 on 2017/8/30 0030.
 */

public class XinRequest<T> {

    Class clzz;

    public XinRequest(Class clzz) {
        this.clzz = clzz;
    }

    public void Post(String method, JSONObject input, final CustomRequestCallback<T> callback) {
        OkGo.<CustomData<T>>post(RestApiPath.REST_URI_HOST, method) // 使用常量配置
                .params("req", new CustomParams(XinApplication.getAppContext()).CreateParams(input))
                .converter(new CustomConvert<CustomData<T>>(clzz) {
                }).adapt(new ObservableResponse<CustomData<T>>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //做一些操作 showLoading();
                        if (callback != null) {
                            callback.onBeforeRequest(disposable);
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Response<CustomData<T>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<CustomData<T>> response) {
                        if (callback != null) {
                            callback.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) { //请求错误
                        Log.printStackTrace(e);

                        if (callback != null) {
                            callback.onError(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (callback != null) {
                            callback.onComplete();
                        }
                    }
                });
    }


    private static CompositeDisposable compositeDisposable;

    public static void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public static void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }
}
