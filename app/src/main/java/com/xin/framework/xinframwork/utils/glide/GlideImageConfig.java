package com.xin.framework.xinframwork.utils.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.Target;

/**
 * 图片配置
 */

public class GlideImageConfig extends ImageConfig {

    private int cacheStrategy;

    private BitmapTransformation transformation;
    private Target[] targets;
    private ImageView[] imageViews;

    private boolean isClearMemory;

    private boolean isClearDiskCache;


    public GlideImageConfig(Builder builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;

        this.cacheStrategy = builder.cacheStrategy;
        this.transformation = builder.transformation;
        this.targets = builder.targets;
        this.imageViews = builder.imageViews;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
    }

    /**
     * 0:DiskCacheStrategy.all<p>
     * 1:DiskCacheStrategy.NONE<p>
     * 2:DiskCacheStrategy.SOURCE<p>
     * 3:DiskCacheStrategy.RESULT<p>
     */
    public int getCacheStrategy() {
        return cacheStrategy;
    }

    /**
     * glide用它来改变图形的形状
     */
    public BitmapTransformation getTransformation() {
        return transformation;
    }

    public Target[] getTargets() {
        return targets;
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    /**
     * 清理内存缓存
     */
    public boolean isClearMemory() {
        return isClearMemory;
    }

    /**
     * 清理本地缓存
     */
    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String url;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private int cacheStrategy;
        private BitmapTransformation transformation;
        private Target[] targets;
        private ImageView[] imageViews;
        private boolean isClearMemory;//清理内存缓存
        private boolean isClearDiskCache;//清理本地缓存

        private Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        /**
         * 0:DiskCacheStrategy.all<p>
         * 1:DiskCacheStrategy.NONE<p>
         * 2:DiskCacheStrategy.SOURCE<p>
         * 3:DiskCacheStrategy.RESULT<p>
         */
        public Builder cacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        /**
         * glide用它来改变图形的形状
         */
        public Builder transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Builder targets(Target... targets) {
            this.targets = targets;
            return this;
        }

        public Builder imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        /**
         * 清理内存缓存
         */
        public Builder isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        /**
         * 清理本地缓存
         */
        public Builder isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }


        public GlideImageConfig build() {
            return new GlideImageConfig(this);
        }
    }


}
