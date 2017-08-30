package com.xin.framework.xinframwork.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 *  P
 */
public interface IPresenter {
    void onStart();
    void onDestroy();

    <P extends IPresenter> void setView(IView iview);



}
