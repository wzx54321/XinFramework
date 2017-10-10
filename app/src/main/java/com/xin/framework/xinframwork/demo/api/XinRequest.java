package com.xin.framework.xinframwork.demo.api;

import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.common.RestApiPath;
import com.xin.framework.xinframwork.demo.callback.CustomConvert;
import com.xin.framework.xinframwork.http.OkGo;
import com.xin.framework.xinframwork.http.adapter.ObservableResponse;
import com.xin.framework.xinframwork.http.cache.CacheMode;
import com.xin.framework.xinframwork.http.callback.CustomRequestCallback;
import com.xin.framework.xinframwork.http.model.CustomData;
import com.xin.framework.xinframwork.http.model.CustomParams;
import com.xin.framework.xinframwork.http.model.Response;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import org.json.JSONObject;

import java.util.HashMap;

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

    private Class clzz;

    /**
     * @param clzz 想要请求返回的Bean
     */
    public XinRequest(Class clzz) {
        this.clzz = clzz;
    }


    /**
     * <p> 定制的POST请求传参，根据具体API设计</p>
     *
     * @param method   请求的api的path
     * @param input    请求参数
     * @param callback 成功回调
     * @see CustomParams  CustomParams类头说明
     */
    public void Post(String method, JSONObject input, final CustomRequestCallback<T> callback) {
        OkGo.<CustomData<T>>post(RestApiPath.REST_URI_HOST, method) // 使用常量配置
                .params("req", new CustomParams(XinApplication.getAppContext()).CreateParams(input))
                .cacheKey(RestApiPath.REST_URI_HOST + method + (input != null ? input.toString() : ""))              //这里完全同okgo的配置一样
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .converter(new CustomConvert<CustomData<T>>(clzz))
                .adapt(new ObservableResponse<CustomData<T>>())//
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

    /**
     * POST请求JSON参数
     *
     * @param method 请求的api的path
     * @param input 请求参数
     * @param callback 成功回调
     */
    public void postJson(String method, JSONObject input, final CustomRequestCallback<T> callback) {
        OkGo.<CustomData<T>>post(/*RestApiPath.REST_URI_HOST*/"http://shindong.xin/testjson.php", method)/* 测试使用 */
               /* .headers("key", "value")*//* TODO header 传值*/
                .cacheKey(RestApiPath.REST_URI_HOST + method + (input != null ? input.toString() : "")) /* TODO 缓存Key 可以删除*/
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)/* TODO  缓存策略 可以删除 */
                .upJson(input)/* TODO  json 参数*/
                .converter(new CustomConvert<CustomData<T>>(clzz))/* TODO 返回数据转换，根据各自的需求自定义*/
                .adapt(new ObservableResponse<CustomData<T>>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //做一些操作 showLoading();
                        if (callback != null) {
                            callback.onBeforeRequest(disposable);
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
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
                    public void onError(@NonNull Throwable e) {
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


    /**
     * get 请求网络
     *
     * @param method   请求的api的path
     * @param input    请求参数
     * @param callback 成功回调
     */
    public void get(String method, HashMap<String, String> input, final CustomRequestCallback<T> callback) {

        OkGo.<CustomData<T>>get(RestApiPath.REST_URI_HOST, method)
                .params(input, false)
                .cacheKey(String.format("%s%s%s", RestApiPath.REST_URI_HOST, method, input != null ? input.toString() : ""))              //这里完全同okgo的配置一样
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .converter(new CustomConvert<CustomData<T>>(clzz) {
                })
                .adapt(new ObservableResponse<CustomData<T>>()).subscribeOn(Schedulers.io())//
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
