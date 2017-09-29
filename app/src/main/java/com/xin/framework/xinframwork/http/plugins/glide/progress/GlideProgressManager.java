package com.xin.framework.xinframwork.http.plugins.glide.progress;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.xin.framework.xinframwork.http.plugins.glide.progress.body.GlideProgressRequestBody;
import com.xin.framework.xinframwork.http.plugins.glide.progress.body.GlideProgressResponseBody;
import com.xin.framework.xinframwork.http.plugins.glide.progress.body.ProgressInfo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GlideProgressManager {


    //WeakHashMap会在java虚拟机回收内存时,找到没被使用的key,将此条目移除,所以不需要手动remove()
    private final Map<String, List<ImgProgressListener>> mRequestListeners = new WeakHashMap<>();
    private final Map<String, List<ImgProgressListener>> mResponseListeners = new WeakHashMap<>();
    private static volatile GlideProgressManager mProgressManager;
    public static final int DEFAULT_REFRESH_TIME = 150;
    private final Handler mHandler;
    private final Interceptor mInterceptor;
    private int mRefreshTime = DEFAULT_REFRESH_TIME; //进度刷新时间(单位ms),避免高频率调用

    public static final GlideProgressManager getInstance() {
        if (mProgressManager == null) {

            synchronized (GlideProgressManager.class) {
                if (mProgressManager == null) {
                    mProgressManager = new GlideProgressManager();
                }
            }
        }
        return mProgressManager;
    }

    private GlideProgressManager() {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mInterceptor = new Interceptor() {


            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = wrapRequestBody(chain.request());

                Response response = chain.proceed(request);

                return wrapResponseBody(response);
            }
        };
    }



    /**
     * 将 {@link okhttp3.OkHttpClient.Builder} 传入,配置一些本管理器需要的参数
     *
     * @param builder
     * @return
     */
    public OkHttpClient.Builder with(OkHttpClient.Builder builder) {
        return builder
                .addNetworkInterceptor(mInterceptor);
    }



    public Response wrapResponseBody(Response response) {
        if (response == null || response.body() == null)
            return response;

        if (haveRedirect(response)) {
            resolveRedirect(mRequestListeners, response);
            resolveRedirect(mResponseListeners, response);
            return response;
        }
        String key = response.request().url().toString();

        if (mResponseListeners.containsKey(key)) {
            List<ImgProgressListener> listeners = mResponseListeners.get(key);
            return response.newBuilder()
                    .body(new GlideProgressResponseBody(mHandler, response.body(), listeners, mRefreshTime))
                    .build();
        }
        return response;
    }


    public Request wrapRequestBody(Request request) {
        if (request == null || request.body() == null)
            return request;
        String key = request.url().toString();
        if (mRequestListeners.containsKey(key)) {
            List<ImgProgressListener> listeners = mRequestListeners.get(key);
            return request.newBuilder()
                    .method(request.method(), new GlideProgressRequestBody(mHandler, request.body(), listeners, mRefreshTime))
                    .build();
        }
        return request;

    }


    /**
     * 是否需要重定向
     *
     * @param response
     * @return
     */
    private boolean haveRedirect(Response response) {
        String status = response.header("Status");
        if (TextUtils.isEmpty(status))
            return false;
        if (status.contains("301") || status.contains("302") || status.contains("303") || status.contains("307")) {
            return true;
        }
        return false;
    }



    /**
     * 使重定向后,也可以监听进度
     *
     * @param map
     * @param response
     */
    private void resolveRedirect(Map<String, List<ImgProgressListener>> map, Response response) {
        String url = response.request().url().toString();
        List<ImgProgressListener> progressListeners = map.get(url); //查看此重定向 url ,是否已经注册过监听器
        if (progressListeners != null) {
            String location = response.header("Location");// 重定向地址
            if (!TextUtils.isEmpty(location) && !map.containsKey(location)) {
                map.put(location, progressListeners); //将需要重定向地址的监听器,提供给重定向地址,保证重定向后也可以监听进度
            }
        }
    }


    /**
     * 设置 {@link ImgProgressListener#onProgress(ProgressInfo)} 每次被调用的间隔时间,单位毫秒
     *
     * @param refreshTime
     */
    public void setRefreshTime(int refreshTime) {
        this.mRefreshTime = refreshTime;
    }


    /**
     * 将需要被监听上传进度的 Url 注册到管理器,此操作请在页面初始化时进行,切勿多次注册同一个(内容相同)监听器
     *
     * @param url
     * @param listener 当此 Url 地址存在上传的动作时,此监听器将被调用
     */
    public void addRequestListener(String url, ImgProgressListener listener) {
        List<ImgProgressListener> progressListeners;
        synchronized (GlideProgressManager.class) {
            progressListeners = mRequestListeners.get(url);
            if (progressListeners == null) {
                progressListeners = new LinkedList<>();
                mRequestListeners.put(url, progressListeners);
            }
        }
        progressListeners.add(listener);
    }

    /**
     * 将需要被监听下载进度的 Url 注册到管理器,此操作请在页面初始化时进行,切勿多次注册同一个(内容相同)监听器
     *
     * @param url
     * @param listener 当此 Url 地址存在下载的动作时,此监听器将被调用
     */
    public void addResponseListener(String url, ImgProgressListener listener) {
        List<ImgProgressListener> progressListeners;
        synchronized (GlideProgressManager.class) {
            progressListeners = mResponseListeners.get(url);
            if (progressListeners == null) {
                progressListeners = new LinkedList<>();
                mResponseListeners.put(url, progressListeners);
            }
        }
        progressListeners.add(listener);
    }

    /**
     * 当在 {@link GlideProgressRequestBody} 和 {@link GlideProgressResponseBody} 内部处理二进制流时发生错误
     * 会主动调用 {@link ImgProgressListener#onError(long, Exception)},但是有些错误并不是在它们内部发生的
     * 但同样会引起网络请求的失败,所以向外面提供{@link GlideProgressManager#notifyOnErorr},当外部发生错误时
     * 手动调用此方法,以通知所有的监听器
     *
     * @param url
     * @param e
     */
    public void notifyOnErorr(String url, Exception e) {
        forEachListenersOnError(mRequestListeners, url, e);
        forEachListenersOnError(mResponseListeners, url, e);
    }



    private void forEachListenersOnError(Map<String, List<ImgProgressListener>> map, String url, Exception e) {
        if (map.containsKey(url)) {
            List<ImgProgressListener> progressListeners = map.get(url);
            ImgProgressListener[] array = progressListeners.toArray(new ImgProgressListener[progressListeners.size()]);
            for (int i = 0; i < array.length; i++) {
                array[i].onError(-1, e);
            }
        }
    }

}
