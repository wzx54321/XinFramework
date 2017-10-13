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
package com.xin.framework.xinframwork.http.cookie.store;

import android.content.Context;

import com.xin.framework.xinframwork.store.box.CookieBox;
import com.xin.framework.xinframwork.store.entity.EntityCookie;
import com.xin.framework.xinframwork.store.entity.EntityCookie_;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：使用数据库 持久化存储 cookie
 * 修订历史：alter by xin 使用objectBox替换sqlite
 * ================================================
 */
public class DBCookieStore implements CookieStore {


    /**
     * 数据结构如下
     * Url.host -> cookie1.name@cookie1.domain,cookie2.name@cookie2.domain,cookie3.name@cookie3.domain
     * cookie_cookie1.name@cookie1.domain -> cookie1
     * cookie_cookie2.name@cookie2.domain -> cookie2
     */
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;
    private final CookieBox mCookieBox;

    public DBCookieStore(Context context) {
        mCookieBox = new CookieBox();


        cookies = new HashMap<>();

        List<EntityCookie> cookieList = mCookieBox.queryAll();

        for (EntityCookie entityCookie : cookieList) {
            if (!cookies.containsKey(entityCookie.getHost())) {
                cookies.put(entityCookie.getHost(), new ConcurrentHashMap<String, Cookie>());
            }
            Cookie cookie = entityCookie.getCookie();
            cookies.get(entityCookie.getHost()).put(getCookieToken(cookie), cookie);
        }
    }

    private String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    /**
     * 当前cookie是否过期
     */
    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    /**
     * 将url的所有Cookie保存在本地
     */
    @Override
    public synchronized void saveCookie(HttpUrl url, List<Cookie> urlCookies) {
        for (Cookie cookie : urlCookies) {
            saveCookie(url, cookie);
        }
    }

    @Override
    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        if (!cookies.containsKey(url.host())) {
            cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
        }
        //当前cookie是否过期
        if (isCookieExpired(cookie)) {
            removeCookie(url, cookie);
        } else {
            //内存缓存
            cookies.get(url.host()).put(getCookieToken(cookie), cookie);



            EntityCookie entityCookie = mCookieBox.getQueryBuilder().equal(EntityCookie_.cookieToken, getCookieToken(cookie)).build().findFirst();

            if (entityCookie != null) {
                entityCookie.setCookie(cookie);
            } else {
                entityCookie = new EntityCookie(url.host(), cookie);

                entityCookie.setCookieByte();
            }

            mCookieBox.insert(entityCookie);

        }
    }

    /**
     * 根据当前url获取所有需要的cookie,只返回没有过期的cookie
     */
    @Override
    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList<>();
        if (!cookies.containsKey(url.host())) return ret;


        List<EntityCookie> entityCookies=mCookieBox.getQueryBuilder().equal(EntityCookie_.host,url.host()).build().find();
        for (EntityCookie entityCookie : entityCookies) {
            Cookie cookie = entityCookie.getCookie();
            if (isCookieExpired(cookie)) {
                removeCookie(url, cookie);
            } else {
                ret.add(cookie);
            }
        }
        return ret;
    }

    /**
     * 根据url移除当前的cookie
     */
    @Override
    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        if (!cookies.containsKey(url.host())) return false;
        String cookieToken = getCookieToken(cookie);
        if (!cookies.get(url.host()).containsKey(cookieToken)) return false;

        //内存移除
        cookies.get(url.host()).remove(cookieToken);

        EntityCookie entityCookie = mCookieBox.getQueryBuilder().equal(EntityCookie_.host, url.host()).and()
                .equal(EntityCookie_.name, cookie.name()).and().equal(EntityCookie_.domain, cookie.domain()).build().findFirst();

        if (entityCookie != null) {
            mCookieBox.delete(entityCookie);
        }

        return true;
    }

    @Override
    public synchronized boolean removeCookie(HttpUrl url) {
        if (!cookies.containsKey(url.host())) return false;

        //内存移除
        cookies.remove(url.host());

        //数据库移除
        List<EntityCookie> entityCookies=mCookieBox.getQueryBuilder().equal(EntityCookie_.host,url.host()).build().find();
        mCookieBox.deleteList(entityCookies);
        return true;
    }

    @Override
    public synchronized boolean removeAllCookie() {
        //内存移除
        cookies.clear();
        //数据库移除


        mCookieBox.deleteAll();
        return true;
    }

    /**
     * 获取所有的cookie
     */
    @Override
    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> ret = new ArrayList<>();
        for (String key : cookies.keySet()) {
            ret.addAll(cookies.get(key).values());
        }
        return ret;
    }

    @Override
    public synchronized List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList<>();
        Map<String, Cookie> mapCookie = cookies.get(url.host());
        if (mapCookie != null) ret.addAll(mapCookie.values());
        return ret;
    }
}
