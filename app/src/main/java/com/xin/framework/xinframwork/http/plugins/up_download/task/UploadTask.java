package com.xin.framework.xinframwork.http.plugins.up_download.task;

import com.xin.framework.xinframwork.http.model.Response;
import com.xin.framework.xinframwork.http.plugins.up_download.StatusConst;
import com.xin.framework.xinframwork.http.plugins.up_download.listener.UploadListener;
import com.xin.framework.xinframwork.http.plugins.up_download.upload.OkUpload;
import com.xin.framework.xinframwork.http.request.base.ProgressRequestBody;
import com.xin.framework.xinframwork.http.request.base.Request;
import com.xin.framework.xinframwork.http.utils.HttpUtils;
import com.xin.framework.xinframwork.store.box.UploadBox;
import com.xin.framework.xinframwork.store.entity.EntityUpload;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.Call;

/**
 * Description : 上传任务
 * Created by 王照鑫 on 2017/9/29 0029.
 */

public class UploadTask<T> implements Runnable{
    public EntityUpload progress;
    public Map<Object, UploadListener<T>> listeners;
    private ThreadPoolExecutor executor;
    private PriorityRunnable priorityRunnable;
    private UploadBox mBox;

    public UploadTask(String tag, Request<T, ? extends Request> request) {
        HttpUtils.checkNotNull(tag, "tag == null");
        progress = new EntityUpload();
        progress.setTag(tag);
        progress.setUrl(request.getBaseUrl());
        progress.setStatus(StatusConst.NONE);
        progress.setTotalSize(-1);
        progress.setRequest(request);

        executor = OkUpload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
        mBox=new UploadBox();
    }

    public UploadTask(EntityUpload progress) {
        HttpUtils.checkNotNull(progress, "progress == null");
        this.progress = progress;
        executor = OkUpload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();

        mBox=new UploadBox();
    }

    public UploadTask<T> priority(int priority) {
        progress.setPriority(priority);
        return this;
    }

    public UploadTask<T> save() {
        mBox.replace(progress);
        return this;
    }

    public UploadTask<T> register(UploadListener<T> listener) {
        if (listener != null) {
            listeners.put(listener.tag, listener);
        }
        return this;
    }


    public void unRegister(UploadListener<T> listener) {
        HttpUtils.checkNotNull(listener, "listener == null");
        listeners.remove(listener.tag);
    }

    public void unRegister(String tag) {
        HttpUtils.checkNotNull(tag, "tag == null");
        listeners.remove(tag);
    }

    public UploadTask<T> start() {
        if (OkUpload.getInstance().getTask(progress.getTag()) == null || mBox.get(progress.getTag()) == null) {
            throw new IllegalStateException("you must call UploadTask#save() before UploadTask#start()！");
        }
        if (progress.getStatus() != StatusConst.WAITING && progress.getStatus() != StatusConst.LOADING) {
            postOnStart(progress);
            postWaiting(progress);
            priorityRunnable = new PriorityRunnable(progress.getPriority(), this);
            executor.execute(priorityRunnable);
        } else {
             Log.w("the task with tag " + progress.getTag() + " is already in the upload queue, current task status is " + progress.getStatus());
        }
        return this;
    }


    public void restart() {
        pause();
        progress.setStatus(StatusConst.NONE);
        progress.setCurrentSize(0);
        progress.setFraction(0);
        progress.setSpeed(0);
        mBox.replace(progress);
        start();
    }

    /** 暂停的方法 */
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

    /** 删除一个任务,会删除下载文件 */
    public UploadTask<T> remove() {
        pause();
        mBox.delete(progress.getTag());
        //noinspection unchecked
        UploadTask<T> task = (UploadTask<T>) OkUpload.getInstance().removeTask(progress.getTag());
        postOnRemove(progress);
        return task;
    }

    @Override
    public void run() {
        progress.setStatus(StatusConst.LOADING);
        postLoading(progress);
        final Response<T> response;
        try {
            //noinspection unchecked
            Request<T, ? extends Request> request = (Request<T, ? extends Request>) progress.getRequest();
            final Call rawCall = request.getRawCall();
            request.uploadInterceptor(new ProgressRequestBody.UploadInterceptor() {
                @Override
                public void uploadProgress(EntityUpload innerProgress) {
                    if (rawCall.isCanceled()) return;
                    if (progress.getStatus() != StatusConst.LOADING) {
                        rawCall.cancel();
                        return;
                    }
                    progress.from(innerProgress);
                    postLoading(progress);
                }
            });
            response = request.adapt().execute();
        } catch (Exception e) {
            postOnError(progress, e);
            return;
        }

        if (response.isSuccessful()) {
            postOnFinish(progress, response.body());
        } else {
            postOnError(progress, response.getException());
        }
    }

    private void postOnStart(final EntityUpload progress) {
        progress.setSpeed(0);
        progress.setStatus(StatusConst.NONE);
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onStart(progress);
                }
            }
        });
    }

    private void postWaiting(final EntityUpload progress) {
        progress.setSpeed(0);
        progress.setStatus(StatusConst.WAITING);
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postPause(final EntityUpload progress) {
        progress.setSpeed(0);
        progress.setStatus(StatusConst.PAUSE);
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postLoading(final EntityUpload progress) {
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postOnError(final EntityUpload progress, final Throwable throwable) {
        progress.setSpeed(0);
        progress.setStatus(StatusConst.ERROR);
        progress.exception = throwable;
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onError(progress);
                }
            }
        });
    }

    private void postOnFinish(final EntityUpload progress, final T t) {
        progress.setSpeed(0);
        progress.setFraction(1.0f);
        progress.setStatus(StatusConst.FINISH);
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onFinish(t, progress);
                }
            }
        });
    }

    private void postOnRemove(final EntityUpload progress) {
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onRemove(progress);
                }
                listeners.clear();
            }
        });
    }

    private void updateDatabase(EntityUpload progress) {
        mBox.update(progress);


    }



}
