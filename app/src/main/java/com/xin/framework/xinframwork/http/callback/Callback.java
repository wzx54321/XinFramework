package com.xin.framework.xinframwork.http.callback;


import com.xin.framework.xinframwork.http.convert.Converter;
import com.xin.framework.xinframwork.http.model.Response;
import com.xin.framework.xinframwork.http.request.base.Request;
import com.xin.framework.xinframwork.store.entity.EntityDownload;
import com.xin.framework.xinframwork.store.entity.EntityUpload;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版   本：1.0
 * 创建日期：2016/1/14
 * 描   述：抽象的回调接口
 * 修订历史：
 * ================================================
 *
 */
public interface Callback<T> extends Converter<T> {
    /** 请求网络开始前，UI线程 */
    void onStart(Request<T, ? extends Request> request);

    /** 对返回数据进行操作的回调， UI线程 */
    void onSuccess(Response<T> response);

    /** 缓存成功的回调,UI线程 */
    void onCacheSuccess(Response<T> response);

    /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
    void onError(Response<T> response);

    /** 请求网络结束后，UI线程 */
    void onFinish();

    /** 上传过程中的进度回调，get请求不回调，UI线程 */
    void uploadProgress(EntityUpload progress);

    /** 下载过程中的进度回调，UI线程 */
    void downloadProgress(EntityDownload progress);
}
