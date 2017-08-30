package com.xin.framework.xinframwork.mvp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Description : （反射获取泛型参数）
 */


public class TypeUtil {
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
           // Log.e(e,"TypeUtil 没有对应的presenter");
        }
        return null;
    }


    public static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }


}
