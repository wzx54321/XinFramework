package com.xin.framework.xinframwork.store.box;

import com.xin.framework.xinframwork.store.box.base.BaseBoxManager;
import com.xin.framework.xinframwork.store.entity.EntityCache;
import com.xin.framework.xinframwork.store.entity.EntityCache_;

/**
 * Description : 缓存操作BOX
 * Created by xin on 2017/9/15 0015.
 */

public class CacheBox extends BaseBoxManager<EntityCache> {


    public CacheBox() {
        super(EntityCache.class);
    }

    @Override
    public String getTableName() {
        return EntityCache_.__DB_NAME;
    }
}
