package com.xin.framework.xinframwork.common;

import android.content.Context;

import com.xin.framework.xinframwork.store.entity.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * 使用objectBox,具体使用文档参照：http://objectbox.io/documentation/entity-annotations/
 * Description : 数据库配置
 * Created by xin on 2017/9/14 0014.
 */

public class DBConfig {
    private static BoxStore boxStore;


    public static void init(Context app) {

        boxStore = MyObjectBox.builder().androidContext(app).build();

    }

    public static BoxStore getBoxStore() {

        if (boxStore == null)
            throw new NullPointerException("did not init DBConfig");

        return boxStore;
    }


}
