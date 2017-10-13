package com.xin.framework.xinframwork.http.plugins.up_download.listener;

import com.xin.framework.xinframwork.store.entity.EntityDownload;

import java.io.File;

/**
 * Description : 下载监听
 * Created by xin on 2017/9/28 0028.
 */

public abstract class DownloadListener implements ProgressListener<File, EntityDownload> {

    public final Object tag;

    public DownloadListener(Object tag) {
        this.tag = tag;
    }


}
