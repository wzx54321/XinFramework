package com.xin.framework.xinframwork.hybrid.download;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.DownloadListener;

import com.xin.framework.xinframwork.common.FileConfig;
import com.xin.framework.xinframwork.http.OkGo;
import com.xin.framework.xinframwork.http.plugins.up_download.download.OkDownload;
import com.xin.framework.xinframwork.http.request.GetRequest;
import com.xin.framework.xinframwork.store.entity.EntityDownload;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.common.assist.Network;
import com.xin.framework.xinframwork.utils.common.utils.FileUtil;

import java.io.File;


/**
 * 描述： webView下载附件使用
 *
 * @author wangzhaoxin
 * @since JDK1.8
 */
public class WebDownLoadListener implements DownloadListener {

    private Handler downloadHandler;

    private Context context;


    public WebDownLoadListener(Context context) {
        this.context = context.getApplicationContext();
        downloadHandler = new DownloadHandler(this.context);
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

        try {
            doDownload(url);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }


    private void doDownload(String url) {


        File downLoadDir = FileUtil.getPublicDir(FileConfig.DIR_WEB_DOWNLOAD);
        Log.i("on WebView download file Start" + "downLoadDir=" + downLoadDir);
        if (FileUtil.createDir(downLoadDir)) {
            if (Network.isAvailable(context)) {
                GetRequest<File> request = OkGo.get(url);
                OkDownload.getInstance().setFolder(downLoadDir.getAbsolutePath());
                OkDownload.request(url, request)//
                        .priority(999)
                        .save()//保存到数据库
                        .register(new com.xin.framework.xinframwork.http.plugins.up_download.listener.DownloadListener(url) {
                            @Override
                            public void onStart(EntityDownload progress) {

                            }

                            @Override
                            public void onProgress(EntityDownload progress) {

                            }

                            @Override
                            public void onError(EntityDownload progress) {
                                OkDownload.getInstance().setFolder(FileConfig.DIR_DOWNLOAD);
                            }

                            @Override
                            public void onFinish(File file, EntityDownload progress) {
                                Message msg = downloadHandler.obtainMessage(0, file.getAbsolutePath());
                                msg.sendToTarget();

                                OkDownload.getInstance().setFolder(FileConfig.DIR_DOWNLOAD);
                            }

                            @Override
                            public void onRemove(EntityDownload progress) {

                            }
                        })
                        .start();
            } else {
                Log.d("webView下载 网络异常");
            }
        }
    }

    private static class DownloadHandler extends Handler {

        private Context context;

        DownloadHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String path = ((String) msg.obj);
            if (TextUtils.isEmpty(path)) {
                Log.d(" webView download: network_unavailable");
                return;
            }
            Log.d(" webView download: 文件保存在--->" + path);
            File file = new File(path);
            Log.i("downloadHandler", "Path=" + file.getAbsolutePath());

            try {
                Intent intent = FileUtil.getFileIntent(file);
                context.startActivity(intent);
            } catch (Exception ex) {
                Log.printStackTrace(ex);
            }
        }
    }

    ;


}
