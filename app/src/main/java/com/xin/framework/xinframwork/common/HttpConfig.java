package com.xin.framework.xinframwork.common;

import android.app.Application;
import android.content.Context;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.http.OkGo;
import com.xin.framework.xinframwork.http.cache.CacheMode;
import com.xin.framework.xinframwork.http.cookie.CookieJarImpl;
import com.xin.framework.xinframwork.http.cookie.store.MemoryCookieStore;
import com.xin.framework.xinframwork.http.https.HttpsUtils;
import com.xin.framework.xinframwork.http.interceptor.HttpLog;
import com.xin.framework.xinframwork.http.model.HttpParams;
import com.xin.framework.xinframwork.store.entity.EntityCache;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Description : 网络请求配置
 * Created by 王照鑫
 */

public class HttpConfig {


    public static final long DEFAULT_MILLISECONDS = 60000; // 默认时间
    public static final long REFRESH_TIME = 300;

    public static void init(Context app) {
        /* ---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    // header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");*/
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     // param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // log相关
        HttpLog httpLog = new HttpLog();
        httpLog.setPrintLevel(HttpLog.Level.BODY); // log打印级别，决定了log显示的详细程度
        //  httpLog.setPrintbinaryBody(true);// 打印二进制Log ,默认不打印
        builder.addInterceptor(httpLog);

        // 超时时间设置，默认60秒
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      // 全局的读取超时时间
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     // 全局的写入超时时间
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   // 全局的连接超时时间
        // 自动管理cookie（或者叫session的保持），以下几种任选其一就行
        if (app.getResources().getBoolean(R.bool.cookie_enable)) {
            builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            // 使用内存保持cookie，app退出后，cookie消失
            // builder.cookieJar(new CookieJarImpl(new SPCookieStore(app)));          // 使用sp保持cookie，如果cookie不过期，则一直有效
            //  builder.cookieJar(new CookieJarImpl(new DBCookieStore(app)));             // 使用数据库保持cookie，如果cookie不过期，则一直有效
        }

        // https相关设置，以下几种方案根据需要自己设置
        // 方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        // 方法二：自定义信任规则，校验服务端证书
        // HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        // 方法三：使用预埋证书，校验服务端证书（自签名证书）
        // HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        // 方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        // HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        // 配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        OkGo.getInstance().init((Application) app) /*必须调用初始化*/
                .setOkHttpClient(builder.build())/*建议设置OkHttpClient，不设置会使用默认的*/
                .setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)/*全局统一缓存模式，默认不使用缓存，可以不传*/
                .setCacheTime(EntityCache.CACHE_NEVER_EXPIRE)/* 全局统一缓存时间，默认永不过期，可以不传*/
                .setRetryCount(3)/*全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0*/
                /*.addCommonHeaders(...)   全局公共头*/
               /* .addCommonParams(...)   公共参数*/
        ;
    }


    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private static class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // 验证主机名是否匹配
            // return hostname.equals(RestApiPath.REST_URI_HOST);
            return true;
        }
    }
}
