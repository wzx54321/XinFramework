package com.xin.framework.xinframwork.http.callback;

import com.xin.framework.xinframwork.http.model.CustomData;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Description :  根据需求自定义的请求回调
 * Created by xin on 2017/8/30 0030.
 */

public interface CustomRequestCallback<T> {


    void onBeforeRequest(@NonNull Disposable disposable);

    void onSuccess(@NonNull  CustomData<T>  result);

    void onError(@NonNull Throwable e);

    void onComplete();
}
