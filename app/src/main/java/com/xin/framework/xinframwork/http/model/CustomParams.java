package com.xin.framework.xinframwork.http.model;

import android.content.Context;
import android.text.TextUtils;

import com.xin.framework.xinframwork.BuildConfig;
import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.content.SPManager;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.common.data.cipher.Base64Cipher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Description : 定制请求参数
 * <p>
 * 接口要求：
 * <p>所有接口都是采用https协议，post请求，字符编码为UTF-8.</p>
 * <p>请求URL：开发环境： https://devapi.xxxxx.com/  测试环境： https://textapi.xxxxx.com/   正式环境： https://api.xxxxxxx.com/
 * <p>开发环境：在开发新需求时，服务端会将代码部署在这个环境，客户端也需要连接到这个环境调试代码．
 * <p> 测试环境：当新需求开发完成并在开发环境测试通过后，准备上线前，部署到测试环境，测试人员在这个环境做测试．
 * <p>正式环境：供最终用户使用的环境，部署时，是将测试环境测试通过的代码拷贝到正式环境．
 * <p>不同的接口使用URI来区分， 如登录接口 https://api.xxxxx.com/login   添加好友接口https://api.xxxxx.com/user/addFriend
 * <p> 请求参数只有一个，参数名为req
 * <p>参数值为  将json做base64编码后的字段串，示例  req=xxx
 * <p>参数分为公共参数和业务参数.
 * <p> json中的参数值只允许为字符串或空字符串，不能出现以下情况{"key":null}  {"key": }  {"key": 23};
 * <p>接口返回json中同样有公共参数和业务参数，并且做base64编码。
 * <p>
 * <p>示例：
 * <p>{
 * <p> "appid":"xxx",
 * <p> "appver":"1.0.0.0",
 * <p> "deviceid":"xxx",
 * <p> "cid":"xxx",
 * <p> "ts":"201512221516333",
 * <p> "uid":"xxx",
 * <p> "token":"xxx",
 * <p> "data":{
 * <p> ...
 * <p> }
 * <p>}
 * <p>
 * <p>
 * <p>
 * Created by xin on 2017/8/22 0022.
 */

public class CustomParams implements Serializable {
    private String appid;           // 4位的整数，用于区分app和请求来源。
    private String appver;          // app版本号+buildCode，用于区分版本和数据统计
    private String deviceid;        // 硬件设备号
    private String cid;             // 渠道号
    private String ts;              // 时间戳
    private String uid;             // 用于记录注册登录用户的身份
    private String token;           // 用于验证身份的合法性和登录的有效性，同时避免接口被刷
    public JSONObject data;        // 业务参数
    private JSONObject mParams;

    private Context mApp;


    public CustomParams(Context app) {
        mApp = app;
        mParams = new JSONObject();
        appid = "1002";
        appver = BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE;
        deviceid = SPManager.getInstance().getDeviceId();
        cid = SPManager.getInstance().getChannel();
        uid = SPManager.getInstance().getUserID();
        token = SPManager.getInstance().getUserToken();
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }


    public String CreateParams(JSONObject data) {
        setData(data);
        try {
            mParams.put("appid", appid);
            mParams.put("appver", appver);
            mParams.put("deviceid", deviceid);
            mParams.put("cid", cid);
            mParams.put("ts", System.currentTimeMillis());
            if (!TextUtils.isEmpty(uid))
                mParams.put("uid", uid);
            if (!TextUtils.isEmpty(token))
                mParams.put("token", token);
            if (this.data != null)
                mParams.put("from", this.data);
        } catch (JSONException e) {
            Log.e(e, "CreateParams JSONException");
        }

        String jsonStr = mParams.toString();
        Log.i("Params:" + jsonStr);
        try {
            return mApp.getResources().getBoolean(R.bool.http_params_base64_enable)
                    ? new String(new Base64Cipher().encrypt(jsonStr.getBytes()), "UTF-8") : jsonStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }


    public Observable<String> build(final JSONObject data) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(CreateParams(data));
            }
        }).subscribeOn(Schedulers.io());


      /*  Disposable disposable = observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.i(TAG,s);
            }
        });
        comDisposable.add(disposable);*/
    }


}
