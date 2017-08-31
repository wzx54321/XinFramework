package com.xin.framework.xinframwork.http.glide;

import android.content.Context;

import com.xin.framework.xinframwork.http.glide.base.IImageLoaderDelegate;
import com.xin.framework.xinframwork.http.glide.base.ImageConfig;


public final class ImageLoader {
    private IImageLoaderDelegate mStrategy;


    public ImageLoader(IImageLoaderDelegate strategy) {
        setLoadImgStrategy(strategy);
    }


    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }

    public <T extends ImageConfig> void clear(Context context, T config) {
        this.mStrategy.clear(context, config);
    }


    public void setLoadImgStrategy(IImageLoaderDelegate strategy) {
        this.mStrategy = strategy;
    }

}
