package com.xin.framework.xinframwork.hybrid.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 描述：  下载使用
 *
 * @Author wangzhaoxin
 * @since JDK1.8
 */
public class WebDownloadRunnable implements Runnable {

    private static final int BUFFER_SIZE = 4096;

    /**
     * webView 使用的cookie
     */
    public static String sWebCookie;

    private File downloadFile;

    private String url;
    private Handler handler;

    private boolean mAborted;

    /**
     * Contructor.
     */
    public WebDownloadRunnable(Context context, String url, Handler handler, File file) {
        this.downloadFile = file;
        @SuppressWarnings("UnusedAssignment") Context context1 = context;
        this.url = url;
        this.handler = handler;
        mAborted = false;
    }


    @Override
    public void run() {

        if (downloadFile != null) {

            fileCheck(downloadFile);

            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {


                URL url = new URL(this.url);
                URLConnection conn = url.openConnection();

                InputStream is = conn.getInputStream();

                int size = conn.getContentLength();


                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(new FileOutputStream(downloadFile));

                boolean downLoading = true;
                byte[] buffer = new byte[BUFFER_SIZE];
                int downloaded = 0;
                int read;

                while ((downLoading) && (!mAborted)) {

                    if ((size - downloaded < BUFFER_SIZE) &&
                            (size - downloaded > 0)) {
                        buffer = new byte[size - downloaded];
                    }

                    read = bis.read(buffer);

                    if (read > 0) {
                        bos.write(buffer, 0, read);
                        downloaded += read;

                    } else {
                        downLoading = false;
                    }
                }

                Message msg = handler.obtainMessage(0, downloadFile.getAbsolutePath());
                msg.sendToTarget();

            } catch (Exception e) {

            } finally {

                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ioe) {

                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ioe) {

                    }
                }
            }

            if (mAborted) {
                fileCheck(downloadFile);
            }

        }


    }

    /**
     * Abort this download.
     */
    public void abort() {
        mAborted = true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void fileCheck(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
