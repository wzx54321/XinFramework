package com.xin.framework.xinframwork.store.entity;

import com.xin.framework.xinframwork.http.cache.CacheMode;
import com.xin.framework.xinframwork.http.model.HttpHeaders;
import com.xin.framework.xinframwork.utils.common.io.IOUtils;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

/**
 * Description :缓存数据库实体
 * Created by xin on 2017/9/15 0015.
 */
@Entity
public class EntityCache<T> implements Serializable {

    @Id
    private long id;

    @Index
    private String key;                    // 缓存key
    private long localExpire;              // 缓存过期时间
    private byte[] HeadByteArray;          // 缓存的实体数据
    private byte[] dataByteArray;          // 缓存的实体数据



    @Transient
    private boolean isExpire;   //缓存是否过期该变量不必保存到数据库，程序运行起来后会动态计算
    @Transient
    private T data;
    @Transient
    private HttpHeaders responseHeaders;   // 缓存的响应头
    @Transient
    public static final long CACHE_NEVER_EXPIRE = -1;        //缓存永不过期

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getLocalExpire() {
        return localExpire;
    }

    public void setLocalExpire(long localExpire) {
        this.localExpire = localExpire;
    }

    public HttpHeaders getResponseHeaders() {
        if (data == null && getHeadByteArray() != null)
            responseHeaders = (HttpHeaders) IOUtils.toObject(getHeadByteArray());
        return responseHeaders;
    }

    public void setResponseHeaders(HttpHeaders responseHeaders) {
        this.responseHeaders = responseHeaders;
        if (responseHeaders != null)
            setHeadByteArray(IOUtils.toByteArray(responseHeaders));
    }

    public byte[] getDataByteArray() {
        return dataByteArray;
    }

    public void setDataByteArray(byte[] dataByteArray) {
        this.dataByteArray = dataByteArray;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean expire) {
        isExpire = expire;
    }

    public T getData() {
        if (data == null && getDataByteArray() != null)
            data = (T) IOUtils.toObject(getDataByteArray());
        return data;
    }

    public void setData(T data) {

        this.data = data;
        if (data != null)
            setDataByteArray(IOUtils.toByteArray(data));
    }


    public byte[] getHeadByteArray() {
        return HeadByteArray;
    }

    public void setHeadByteArray(byte[] headByteArray) {
        HeadByteArray = headByteArray;
    }

    /**
     * @param cacheTime 允许的缓存时间
     * @param baseTime  基准时间,小于当前时间视为过期
     * @return 是否过期
     */
    public boolean checkExpire(CacheMode cacheMode, long cacheTime, long baseTime) {
        //304的默认缓存模式,设置缓存时间无效,需要依靠服务端的响应头控制
        if (cacheMode == CacheMode.DEFAULT) return getLocalExpire() < baseTime;
        if (cacheTime == CACHE_NEVER_EXPIRE) return false;
        return getLocalExpire() + cacheTime < baseTime;
    }


}
