package com.xin.framework.xinframwork.hybrid.bean;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description :
 * Created by 王照鑫 on 2017/11/3 0003.
 */

public class WebPostParams<K, V> extends ConcurrentHashMap<K, V> {

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();

        for (Entry<K, V> entry : entrySet()) {


            stringBuffer.append("Key = ").append(entry.getKey()).append(", Value = ").append(entry.getValue());
        }

        return stringBuffer.toString();
    }
}
