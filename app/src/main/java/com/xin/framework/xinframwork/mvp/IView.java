package com.xin.framework.xinframwork.mvp;

/**

 */
public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);


    /**
     * 处理Presenter发送的消息,这里面和handler的原理一样,switch(what),做不同的操作
     * @param message PresenterMessage
     */
    void handlePresenterMsg(PresenterMessage message);

}
