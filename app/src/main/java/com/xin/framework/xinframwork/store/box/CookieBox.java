package com.xin.framework.xinframwork.store.box;

import com.xin.framework.xinframwork.store.box.base.BaseBoxManager;
import com.xin.framework.xinframwork.store.entity.EntityCookie;
import com.xin.framework.xinframwork.store.entity.EntityCookie_;

/**
 * Description :
 * Created by 王照鑫 on 2017/9/15 0015.
 */

public class CookieBox extends BaseBoxManager<EntityCookie> {


    public CookieBox() {
        super(EntityCookie.class);
    }

    @Override
    public String getTableName() {
        return EntityCookie_.__DB_NAME;
    }


}
