package com.xin.framework.xinframwork.http.plugins.up_download.task;


import android.text.TextUtils;

import com.xin.framework.xinframwork.http.exception.HttpException;
import com.xin.framework.xinframwork.http.exception.OkGoException;
import com.xin.framework.xinframwork.http.exception.StorageException;
import com.xin.framework.xinframwork.http.model.HttpHeaders;
import com.xin.framework.xinframwork.http.plugins.up_download.StatusConst;
import com.xin.framework.xinframwork.http.plugins.up_download.download.OkDownload;
import com.xin.framework.xinframwork.http.plugins.up_download.listener.DownloadListener;
import com.xin.framework.xinframwork.http.request.base.Request;
import com.xin.framework.xinframwork.http.utils.HttpUtils;
import com.xin.framework.xinframwork.store.box.DownLoadBox;
import com.xin.framework.xinframwork.store.entity.EntityDownload;
import com.xin.framework.xinframwork.store.entity.EntityDownload_;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.common.io.IOUtils;
import com.xin.framework.xinframwork.utils.common.utils.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description : 下载任务
 * Created by xin on 2017/9/28 0028.
 */

public class DownloadTask implements Runnable {
    private static final int BUFFER_SIZE = 1024 * 8;
    public EntityDownload progress;
    public Map<Object, DownloadListener> listeners;
    private ThreadPoolExecutor executor;
    private PriorityRunnable priorityRunnable;

    private DownLoadBox mBox;

    public DownloadTask(String tag, Request<File, ? extends Request> request) {
        HttpUtils.checkNotNull(tag, "tag == null");
        progress = new EntityDownload();
        progress.setTag(tag);
        progress.setFolder(OkDownload.getInstance().getFolder());
        progress.setUrl(request.getBaseUrl());
        progress.setStatus(StatusConst.NONE);
        progress.setTotalSize(-1);
        progress.setRequest(request);
        progress.setRequestData(progress.getRequestData());
        executor = OkDownload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
        mBox = new DownLoadBox();
    }


    public DownloadTask(EntityDownload progress) {
        HttpUtils.checkNotNull(progress, "progress == null");
        this.progress = progress;
        executor = OkDownload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
        mBox = new DownLoadBox();
    }


    public DownloadTask folder(String folder) {
        if (folder != null && !TextUtils.isEmpty(folder.trim())) {
            progress.setFolder(folder);
        } else {
            Log.w("folder is null, ignored!");
        }
        return this;
    }


    public DownloadTask fileName(String fileName) {
        if (fileName != null && !TextUtils.isEmpty(fileName.trim())) {
            progress.setFileName(fileName);
        } else {
            Log.w("fileName is null, ignored!");
        }
        return this;
    }

    public DownloadTask priority(int priority) {
        progress.setPriority(priority);
        return this;
    }


    public DownloadTask save() {
        if (!TextUtils.isEmpty(progress.getFolder()) && !TextUtils.isEmpty(progress.getFileName())) {
            progress.setFilePath(new File(progress.getFolder(), progress.getFileName()).getAbsolutePath());
        }
        mBox.update(progress);
        return this;
    }


    /**
     * 祖册进度监听
     *
     * @param listener
     * @return
     */
    public DownloadTask register(DownloadListener listener) {
        if (listener != null) {
            listeners.put(listener.tag, listener);
        }
        return this;
    }

    public void unRegister(DownloadListener listener) {
        HttpUtils.checkNotNull(listener, "listener == null");
        listeners.remove(listener.tag);
    }


    public void start() {
        if (OkDownload.getInstance().getTask(progress.getTag()) == null || mBox.get(progress.getTag()) == null) {
            throw new IllegalStateException("you must call DownloadTask#save() before DownloadTask#start()！");
        }
        if (progress.getStatus() == StatusConst.NONE || progress.getStatus() == StatusConst.PAUSE || progress.getStatus() == StatusConst.ERROR) {
            postOnStart(progress);
            postWaiting(progress);
            priorityRunnable = new PriorityRunnable(progress.getPriority(), this);
            executor.execute(priorityRunnable);
        } else if (progress.getStatus() == StatusConst.FINISH) {
            if (progress.getFilePath() == null) {
                postOnError(progress, new StorageException("the file of the task with tag:" + progress.getTag() + " may be invalid or damaged, please call the method restart() to download again！"));
            } else {
                File file = new File(progress.getFilePath());
                if (file.exists() && file.length() == progress.getTotalSize()) {
                    postOnFinish(progress, new File(progress.getFilePath()));
                } else {
                    postOnError(progress, new StorageException("the file " + progress.getFilePath() + " may be invalid or damaged, please call the method restart() to download again！"));
                }
            }
        } else {
            Log.w("the task with tag " + progress.getTag() + " is already in the download queue, current task status is " + progress.getStatus());
        }
    }


    public void restart() {
        pause();
        if (!TextUtils.isEmpty(progress.getFilePath()))

            FileUtil.deleteFile(new File(progress.getFilePath()));

        progress.setStatus(StatusConst.NONE);

        progress.setCurrentSize(0);
        progress.setFraction(0);
        progress.setSpeed(0);
        mBox.update(progress);
        start();
    }

    /**
     * 暂停的方法
     */
    public void pause() {
        executor.remove(priorityRunnable);
        if (progress.getStatus() == StatusConst.WAITING) {
            postPause(progress);
        } else if (progress.getStatus() == StatusConst.LOADING) {
            progress.setSpeed(0);
            progress.setStatus(StatusConst.PAUSE);
        } else {
            Log.w("only the task with status WAITING(1) or LOADING(2) can pause, current status is " + progress.getStatus());
        }
    }

    /**
     * 删除一个任务,会删除下载文件
     */
    public void remove() {
        remove(false);
    }


    /**
     * 删除一个任务,会删除下载文件
     */
    public DownloadTask remove(boolean isDeleteFile) {
        pause();

        if (isDeleteFile && !TextUtils.isEmpty(progress.getFilePath()))
            FileUtil.deleteFileDir(new File(progress.getFilePath()));

        mBox.delete(mBox.getQueryBuilder().equal(EntityDownload_.tag, progress.getTag()).build().findUnique());
        DownloadTask task = OkDownload.getInstance().removeTask(progress.getTag());
        postOnRemove(progress);
        return task;
    }


    @Override
    public void run() {
        //check breakpoint
        long startPosition = progress.getCurrentSize();
        if (startPosition < 0) {
            postOnError(progress, OkGoException.BREAKPOINT_EXPIRED());
            return;
        }
        if (startPosition > 0) {
            if (!TextUtils.isEmpty(progress.getFilePath())) {
                File file = new File(progress.getFilePath());
                if (!file.exists()) {
                    postOnError(progress, OkGoException.BREAKPOINT_NOT_EXIST());
                    return;
                }
            }
        }

        //request network from startPosition
        Response response;
        try {
            Request<?, ? extends Request> request = progress.getRequest();
            request.headers(HttpHeaders.HEAD_KEY_RANGE, "bytes=" + startPosition + "-");
            response = request.execute();
        } catch (IOException e) {
            postOnError(progress, e);
            return;
        }

        //check network data
        int code = response.code();
        if (code == 404 || code >= 500) {
            postOnError(progress, HttpException.NET_ERROR());
            return;
        }
        ResponseBody body = response.body();
        if (body == null) {
            postOnError(progress, new HttpException("response body is null"));
            return;
        }
        if (progress.getTotalSize() == -1) {
            progress.setTotalSize(body.contentLength());
        }

        //create filename
        String fileName = progress.getFileName();
        if (TextUtils.isEmpty(fileName)) {
            fileName = HttpUtils.getNetFileName(response, progress.getUrl());
            progress.setFileName(fileName);
        }
        if (!FileUtil.createNewFileAndParentDir(new File(progress.getFolder()))) {
            postOnError(progress, StorageException.NOT_AVAILABLE());
            return;
        }

        //create and check file
        File file;
        if (TextUtils.isEmpty(progress.getFilePath())) {
            file = new File(progress.getFolder(), fileName);
            progress.setFilePath(file.getAbsolutePath());
        } else {
            file = new File(progress.getFilePath());
        }
        if (startPosition > 0 && !file.exists()) {
            postOnError(progress, OkGoException.BREAKPOINT_EXPIRED());
            return;
        }
        if (startPosition > progress.getTotalSize()) {
            postOnError(progress, OkGoException.BREAKPOINT_EXPIRED());
            return;
        }
        if (startPosition == 0 && file.exists()) {
            FileUtil.deleteFile(file);
        }
        if (startPosition == progress.getTotalSize() && startPosition > 0) {
            if (file.exists() && startPosition == file.length()) {
                postOnFinish(progress, file);
                return;
            } else {
                postOnError(progress, OkGoException.BREAKPOINT_EXPIRED());
                return;
            }
        }

        //start downloading
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(startPosition);
            progress.setCurrentSize(startPosition);
        } catch (Exception e) {
            postOnError(progress, e);
            return;
        }
        try {

            mBox.replace(progress);
            download(body.byteStream(), randomAccessFile, progress);
        } catch (IOException e) {
            postOnError(progress, e);
            return;
        }

        //check finish status
        if (progress.getStatus() == StatusConst.PAUSE) {
            postPause(progress);
        } else if (progress.getStatus() == StatusConst.LOADING) {
            if (file.length() == progress.getTotalSize()) {
                postOnFinish(progress, file);
            } else {
                postOnError(progress, OkGoException.BREAKPOINT_EXPIRED());
            }
        } else {
            postOnError(progress, OkGoException.UNKNOWN());
        }
    }


    /**
     * 执行文件下载
     */
    private void download(InputStream input, RandomAccessFile out, EntityDownload progress) throws IOException {
        if (input == null || out == null) return;

        progress.setStatus(StatusConst.LOADING);
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        int len;
        try {
            while ((len = in.read(buffer, 0, BUFFER_SIZE)) != -1 && progress.getStatus() == StatusConst.LOADING) {
                out.write(buffer, 0, len);

                EntityDownload.changeProgress(progress, len, progress.getTotalSize(), new EntityDownload.Action() {
                    @Override
                    public void call(EntityDownload progress) {
                        postLoading(progress);
                    }
                });
            }
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(input);
        }
    }


    private void postOnStart(final EntityDownload progress) {
        progress.setSpeed(0);
        progress.setStatus(StatusConst.NONE);
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadListener listener : listeners.values()) {
                    listener.onStart(progress);
                }
            }
        });
    }

    private void postWaiting(final EntityDownload progress) {
        progress.setSpeed(0);
        progress.setStatus(StatusConst.WAITING);
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postPause(final EntityDownload progress) {
        progress.setSpeed(0);
        progress.setStatus(StatusConst.PAUSE);
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postLoading(final EntityDownload progress) {
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postOnError(final EntityDownload progress, final Throwable throwable) {
        progress.setSpeed(0);
        progress.setStatus(StatusConst.ERROR);
        progress.exception = throwable;
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onError(progress);
                }
            }
        });
    }


    private void postOnFinish(final EntityDownload progress, final File file) {
        progress.setSpeed(0);
        progress.setFraction(1.0f);
        progress.setStatus(StatusConst.FINISH);
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onFinish(file, progress);
                }
            }
        });
    }


    private void postOnRemove(final EntityDownload progress) {
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadListener listener : listeners.values()) {
                    listener.onRemove(progress);
                }
                listeners.clear();
            }
        });
    }

    private void updateDatabase(EntityDownload progress) {
        mBox.replace(progress);
    }


}
