package com.xin.framework.xinframwork.http.callback;

import com.xin.framework.xinframwork.http.model.CustomData;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/30 0030.
 */

public interface CustomRequestCallback<T> {


    void onBeforeRequest(@NonNull Disposable disposable);

    void onSuccess(@NonNull  CustomData<T>  resault);

    void onError(@NonNull Throwable e);

    void onComplete();
}
