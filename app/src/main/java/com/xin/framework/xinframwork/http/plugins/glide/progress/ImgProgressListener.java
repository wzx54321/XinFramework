package com.xin.framework.xinframwork.http.plugins.glide.progress;


import com.xin.framework.xinframwork.http.plugins.glide.progress.body.ProgressInfo;

/**
 * Description : Progress监听接口
 * Created by 王照鑫 on 2017/8/31 0031.
 */

public interface ImgProgressListener {

    /**
     * 进度监听
     *
     * @param progressInfo
     */
    void onProgress(ProgressInfo progressInfo);

    void onError(long id, Exception e);
}
