package com.xin.framework.xinframwork.mvp;

/**
 *  P
 */
public interface IPresenter {
    void onStart();
    void onDestroy();

    <P extends IPresenter> void setView(IView iview);



}
