package com.xin.framework.xinframwork.http.plugins.glide.base;

import android.content.Context;

/**
 * Description : ImageLoader 委托接口
 * Created by xin on 2017/8/17 0017.

 */

public interface IImageLoaderDelegate<T extends ImageConfig> {
    void loadImage(Context ctx, T config);
    void clear(Context ctx, T config);
}
