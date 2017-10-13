package com.xin.framework.xinframwork.http.plugins.glide.base;

import android.widget.ImageView;

/**
 * Created by xin on 2017/8/17 0017.
 * <p>
 * Email： ittfxin@126.com
 */

public class ImageConfig {

    protected String url;
    protected ImageView imageView;
    protected int placeholder;
    protected int errorPic;


    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }
}
