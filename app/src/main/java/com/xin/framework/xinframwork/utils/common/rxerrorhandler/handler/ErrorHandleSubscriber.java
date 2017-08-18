package com.xin.framework.xinframwork.utils.common.rxerrorhandler.handler;

import com.xin.framework.xinframwork.utils.common.rxerrorhandler.core.RxErrorHandler;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by jess on 9/2/16 14:41
 * Contact with jess.yan.effort@gmail.com
 */

public abstract class ErrorHandleSubscriber<T> implements Observer<T> {
    private  ErrorHandlerFactory mHandlerFactvory;

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler){
        this.mHandlerFactvory = rxErrorHandler.getHandlerFactory();
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }


    @Override
    public void onComplete() {

    }


    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        mHandlerFactvory.handleError(e);
    }
}

