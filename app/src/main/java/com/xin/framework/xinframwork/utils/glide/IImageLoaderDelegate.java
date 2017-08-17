package com.xin.framework.xinframwork.utils.glide;

import android.content.Context;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/17 0017.

 */

public interface IImageLoaderDelegate<T extends ImageConfig> {
    void loadImage(Context ctx, T config);
    void clear(Context ctx, T config);
}
