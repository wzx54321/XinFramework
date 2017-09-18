package com.xin.framework.xinframwork.store.box;

import com.xin.framework.xinframwork.store.box.base.BaseBoxManager;
import com.xin.framework.xinframwork.store.entity.EntityCache;
import com.xin.framework.xinframwork.store.entity.EntityCache_;

/**
 * Description :
 * Created by 王照鑫 on 2017/9/15 0015.
 */

public class CacheBox extends BaseBoxManager<EntityCache> {


    public CacheBox() {

     /*   *//*Parameterized*//*Class<EntityCache> objectType = TypeUtil.type(EntityCache.class, clzz);
      *//*  Type[] argue = objectType.getActualTypeArguments();
        Class<EntityCache> aClass = (Class) argue[0];*/
        super(EntityCache.class);
    }

    @Override
    public String getTableName() {
        return EntityCache_.__DB_NAME;
    }
}
