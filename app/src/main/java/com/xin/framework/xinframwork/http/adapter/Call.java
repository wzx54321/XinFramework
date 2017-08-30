package com.xin.framework.xinframwork.http.adapter;

import com.xin.framework.xinframwork.http.callback.Callback;
import com.xin.framework.xinframwork.http.model.Response;
import com.xin.framework.xinframwork.http.request.base.Request;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/9/11
 * 描    述：请求的包装类
 * 修订历史：
 * ================================================
 */
public interface Call<T> {

    /** 同步执行 */
    Response<T> execute() throws Exception;

    /** 异步回调执行 */
    void execute(Callback<T> callback);

    /** 是否已经执行 */
    boolean isExecuted();


    /** 取消 */
    void cancel();

    /** 是否取消 */
    boolean isCanceled();

    Call<T> clone();

    Request getRequest();
}
