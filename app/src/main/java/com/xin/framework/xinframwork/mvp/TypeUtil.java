package com.xin.framework.xinframwork.mvp;

import com.xin.framework.xinframwork.utils.android.logger.Log;

import java.lang.reflect.ParameterizedType;

/**
 * Description : （反射获取泛型参数）
 */


public class TypeUtil {
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
            Log.e(e,"TypeUtil 没有获取到对应的presenter");
        }
        return null;
    }
}
