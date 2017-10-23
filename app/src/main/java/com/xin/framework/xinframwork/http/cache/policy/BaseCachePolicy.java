/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xin.framework.xinframwork.http.cache.policy;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.xin.framework.xinframwork.http.OkGo;
import com.xin.framework.xinframwork.http.cache.CacheMode;
import com.xin.framework.xinframwork.http.callback.Callback;
import com.xin.framework.xinframwork.http.exception.HttpException;
import com.xin.framework.xinframwork.http.model.Response;
import com.xin.framework.xinframwork.http.request.base.Request;
import com.xin.framework.xinframwork.http.utils.HeaderParser;
import com.xin.framework.xinframwork.http.utils.HttpUtils;
import com.xin.framework.xinframwork.store.box.CacheBox;
import com.xin.framework.xinframwork.store.entity.EntityCache;
import com.xin.framework.xinframwork.store.entity.EntityCache_;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/5/25
 * 描    述：
 * 修订历史：替换数据库使用objectBox
 * ================================================
 */
public abstract class BaseCachePolicy<T> implements CachePolicy<T> {

    protected Request<T, ? extends Request> request;
    protected volatile boolean canceled;
    protected volatile int currentRetryCount = 0;
    protected boolean executed;
    protected Call rawCall;
    protected Callback<T> mCallback;
    protected EntityCache<T> cacheEntity;
    protected CacheBox mCacheBox;

    public BaseCachePolicy(Request<T, ? extends Request> request) {
        this.request = request;
        mCacheBox = new CacheBox();
    }

    @Override
    public boolean onAnalysisResponse(Call call, okhttp3.Response response) {
        return false;
    }

    @Override
    public EntityCache<T> prepareCache() {
        //check the config of cache
        if (request.getCacheKey() == null) {
            request.cacheKey(HttpUtils.createUrlFromParams(request.getBaseUrl(), request.getParams().urlParamsMap));
        }
        if (request.getCacheMode() == null) {
            request.cacheMode(CacheMode.NO_CACHE);
        }

        CacheMode cacheMode = request.getCacheMode();
        if (cacheMode != CacheMode.NO_CACHE) {
            //noinspection unchecked
            cacheEntity = mCacheBox.getQueryBuilder().equal(EntityCache_.key, request.getCacheKey()).build().findUnique();
            HeaderParser.addCacheHeaders(request, cacheEntity, cacheMode);
            if (cacheEntity != null && cacheEntity.checkExpire(cacheMode, request.getCacheTime(), System.currentTimeMillis())) {
                cacheEntity.setExpire(true);
            }
        }

        if (cacheEntity == null || cacheEntity.isExpire() || cacheEntity.getData() == null || cacheEntity.getResponseHeaders() == null) {
            cacheEntity = null;
        }
        return cacheEntity;
    }

    @Override
    public synchronized Call prepareRawCall() throws Throwable {
        if (executed) throw HttpException.COMMON("Already executed!");
        executed = true;
        rawCall = request.getRawCall();
        if (canceled) rawCall.cancel();
        return rawCall;
    }

    protected Response<T> requestNetworkSync() {
        try {
            okhttp3.Response response = rawCall.execute();
            int responseCode = response.code();

            //network error
            if (responseCode == 404 || responseCode >= 500) {
                return Response.error(false, rawCall, response, HttpException.NET_ERROR());
            }

            T body = request.getConverter().convertResponse(response);
            //save cache when request is successful
            saveCache(response.headers(), body);
            return Response.success(false, body, rawCall, response);
        } catch (Throwable throwable) {
            if (throwable instanceof SocketTimeoutException && currentRetryCount < request.getRetryCount()) {
                currentRetryCount++;
                rawCall = request.getRawCall();
                if (canceled) {
                    rawCall.cancel();
                } else {
                    requestNetworkSync();
                }
            }
            return Response.error(false, rawCall, null, throwable);
        }
    }

    protected void requestNetworkAsync() {
        rawCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call,@NonNull IOException e) {
                if (e instanceof SocketTimeoutException && currentRetryCount < request.getRetryCount()) {
                    //retry when timeout
                    currentRetryCount++;
                    rawCall = request.getRawCall();
                    if (canceled) {
                        rawCall.cancel();
                    } else {
                        rawCall.enqueue(this);
                    }
                } else {
                    if (!call.isCanceled()) {
                        Response<T> error = Response.error(false, call, null, e);
                        onError(error);
                    }
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                int responseCode = response.code();

                //network error
                if (responseCode == 404 || responseCode >= 500) {
                    Response<T> error = Response.error(false, call, response, HttpException.NET_ERROR());
                    onError(error);
                    return;
                }

                if (onAnalysisResponse(call, response)) return;

                try {
                    T body = request.getConverter().convertResponse(response);
                    //save cache when request is successful
                    saveCache(response.headers(), body);
                    Response<T> success = Response.success(false, body, call, response);
                    onSuccess(success);
                } catch (Throwable throwable) {
                    Response<T> error = Response.error(false, call, response, throwable);
                    onError(error);
                }
            }
        });
    }

    /**
     * 请求成功后根据缓存模式，更新缓存数据
     *
     * @param headers 响应头
     * @param data    响应数据
     */
    private void saveCache(Headers headers, T data) {
        if (request.getCacheMode() == CacheMode.NO_CACHE) return;    //不需要缓存,直接返回
        if (data instanceof Bitmap) return;             //Bitmap没有实现Serializable,不能缓存

        EntityCache<T> cache = HeaderParser.createCacheEntity(headers, data, request.getCacheMode(), request.getCacheKey());
        if (cache == null) {
            //服务器不需要缓存，移除本地缓存
            List<EntityCache> cacheTemps = mCacheBox.getQueryBuilder().equal(EntityCache_.key, request.getCacheKey()).build().find();
            mCacheBox.deleteList(cacheTemps);

        } else {
            EntityCache<T> cacheTemps = mCacheBox.getQueryBuilder().equal(EntityCache_.key, request.getCacheKey()).build().findFirst();
          if(cacheTemps!=null){

              cache.setId(cacheTemps.getId());
          }
            //缓存命中，更新缓存
            mCacheBox.update(cache);
        }
    }

    protected void runOnUiThread(Runnable run) {
        OkGo.getInstance().getDelivery().post(run);
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public void cancel() {
        canceled = true;
        if (rawCall != null) {
            rawCall.cancel();
        }
    }

    @Override
    public boolean isCanceled() {
        if (canceled) return true;
        synchronized (this) {
            return rawCall != null && rawCall.isCanceled();
        }
    }
}
