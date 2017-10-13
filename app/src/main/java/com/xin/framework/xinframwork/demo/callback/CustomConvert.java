package com.xin.framework.xinframwork.demo.callback;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.http.common.HttpConst;
import com.xin.framework.xinframwork.http.convert.Converter;
import com.xin.framework.xinframwork.http.model.CustomData;
import com.xin.framework.xinframwork.http.utils.Convert;
import com.xin.framework.xinframwork.mvp.TypeUtil;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.common.data.cipher.Base64Cipher;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description : 自定义转换类,根据自己应用的需求自定义
 * Created by xin on 2017/8/29 0029.
 */

public class CustomConvert<T> implements Converter<T> {

    private Class clazz;


    public CustomConvert(Class clazz) {
        this.clazz = clazz;
    }


    @Override
    public T convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;

        String responseStr = body.string();


        if (XinApplication.getAppContext().getResources().getBoolean(R.bool.http_params_base64_enable)) {
            try {
                responseStr = new String(new Base64Cipher().decrypt(responseStr.getBytes()),
                        "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        Log.i("convertResponse  :" + responseStr);
        response.close();

        return parseResult(responseStr);
    }

    private T parseResult(String responseStr) {


        Type objectType = TypeUtil.type(CustomData.class, clazz);
        CustomData lzyResponse = Convert.fromJson(responseStr, objectType);

        int code = lzyResponse.code;

        //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
        if (code == HttpConst.RESULT_OK) {
            return (T) lzyResponse;
        } else {
            //直接将服务端的错误信息抛出，onError中可以获取
            throw new IllegalStateException("(" + code + ")" + lzyResponse.msg);
        }

    }


}
