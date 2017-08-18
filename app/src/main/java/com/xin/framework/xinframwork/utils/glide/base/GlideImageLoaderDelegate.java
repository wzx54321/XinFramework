package com.xin.framework.xinframwork.utils.glide.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/17 0017.
 */

public class GlideImageLoaderDelegate implements IImageLoaderDelegate<GlideImageConfig> {
    @Override
    public void loadImage(Context ctx, GlideImageConfig config) {
        if (ctx == null) throw new IllegalStateException("Context is required");
        if (config == null) throw new IllegalStateException("GlideImageConfig is required");
        if (TextUtils.isEmpty(config.getUrl())) throw new IllegalStateException("Url is required");
        if (config.getImageView() == null) throw new IllegalStateException("Imageview is required");


        DiskCacheStrategy mDiskCacheStrategy = null;
        switch (config.getCacheStrategy()) {//缓存策略
            case 0:
                mDiskCacheStrategy = DiskCacheStrategy.ALL;
                break;
            case 1:
                mDiskCacheStrategy = DiskCacheStrategy.NONE;
                break;
            case 2:
                mDiskCacheStrategy = DiskCacheStrategy.DATA;
                break;
            case 3:
                mDiskCacheStrategy = DiskCacheStrategy.RESOURCE;
                break;
            case 5:
                mDiskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
                break;
        }


        GlideRequest<Drawable> requestBuilder = GlideApp.with(ctx).load(config.getUrl()).centerCrop().diskCacheStrategy(mDiskCacheStrategy);


        if (config.getTransformation() != null) {//glide用它来改变图形的形状
            requestBuilder.transform(config.getTransformation());
        }
        if (config.getPlaceholder() != 0)//设置占位符
            requestBuilder.placeholder(config.getPlaceholder());

        if (config.getErrorPic() != 0)//设置错误的图片
            requestBuilder.error(config.getErrorPic());

        requestBuilder
                .into(config.getImageView());


    }

    @Override
    public void clear(final Context ctx, GlideImageConfig config) {
        if (ctx == null) throw new IllegalStateException("Context is required");
        if (config == null) throw new IllegalStateException("GlideImageConfig is required");

        if (config.getImageViews() != null && config.getImageViews().length > 0) {//取消在执行的任务并且释放资源
            for (ImageView imageView : config.getImageViews()) {
                GlideApp.with(ctx).clear(imageView);
            }
        }

        if (config.getTargets() != null && config.getTargets().length > 0) {//取消在执行的任务并且释放资源
            for (Target target : config.getTargets())
                GlideApp.with(ctx).clear(target);
        }


        if (config.isClearDiskCache()) {//清除本地缓存
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            GlideApp.get(ctx).clearDiskCache();
                        }
                    });
        }

        if (config.isClearMemory()) {//清除内存缓存
            Glide.get(ctx).clearMemory();
        }


    }
}
