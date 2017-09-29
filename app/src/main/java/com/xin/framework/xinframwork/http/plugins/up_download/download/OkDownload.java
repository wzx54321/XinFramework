package com.xin.framework.xinframwork.http.plugins.up_download.download;

import com.xin.framework.xinframwork.common.FileConfig;
import com.xin.framework.xinframwork.http.plugins.up_download.StatusConst;
import com.xin.framework.xinframwork.http.plugins.up_download.listener.OnAllTaskEndListener;
import com.xin.framework.xinframwork.http.plugins.up_download.task.DownloadTask;
import com.xin.framework.xinframwork.http.request.base.Request;
import com.xin.framework.xinframwork.store.box.DownLoadBox;
import com.xin.framework.xinframwork.store.entity.EntityDownload;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OkDownload {
    private String folder;                                      //下载的默认文件夹
    private DownloadThreadPool threadPool;                      //下载的线程池
    private ConcurrentHashMap<String, DownloadTask> taskMap;    //所有任务
    private DownLoadBox mBox;

    public static OkDownload getInstance() {
        return OkDownloadHolder.instance;
    }

    public String getFolder() {
        return folder;
    }

    public DownloadThreadPool getThreadPool() {
        return threadPool;
    }

    public DownloadTask getTask(String tag) {
        return taskMap.get(tag);
    }

    public DownloadTask removeTask(String tag) {
        return taskMap.remove(tag);
    }

    public Map<String, DownloadTask> getTaskMap() {
        return taskMap;
    }

    private static class OkDownloadHolder {
        private static final OkDownload instance = new OkDownload();
    }


    private OkDownload() {
        File file = FileConfig.getPublicDir(FileConfig.DIR_DOWNLOAD);
        folder = file.getAbsolutePath();
        threadPool = new DownloadThreadPool();
        taskMap = new ConcurrentHashMap<>();
        mBox = new DownLoadBox();

        //校验数据的有效性，防止下载过程中退出，第二次进入的时候，由于状态没有更新导致的状态错误
        List<EntityDownload> downloadList = mBox.getDownloading();

        for (EntityDownload info : downloadList) {
            if (info.getStatus() == StatusConst.WAITING || info.getStatus() == StatusConst.LOADING
                    || info.getStatus() == StatusConst.PAUSE) {
                info.setStatus(StatusConst.NONE);
            }
        }
        mBox.replace(downloadList);

    }

    public static DownloadTask request(String tag, Request<File, ? extends Request> request) {

        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        DownloadTask task = taskMap.get(tag);
        if (task == null) {
            task = new DownloadTask(tag, request);
            taskMap.put(tag, task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static DownloadTask restore(EntityDownload progress) {
        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        DownloadTask task = taskMap.get(progress.getTag());
        if (task == null) {
            task = new DownloadTask(progress);
            taskMap.put(progress.getTag(), task);
        }
        return task;
    }

    /**
     * 开始所有任务
     */
    public void startAll() {
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                Log.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            task.start();
        }
    }


    /**
     * 暂停全部任务
     */
    public void pauseAll() {
        //先停止未开始的任务
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                Log.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.getStatus() != StatusConst.LOADING) {
                task.pause();
            }
        }
        //再停止进行中的任务
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                Log.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.getStatus() == StatusConst.LOADING) {
                task.pause();
            }
        }
    }


    /**
     * 删除所有任务
     */
    public void removeAll() {
        removeAll(false);
    }

    /**
     * 删除所有任务
     *
     * @param isDeleteFile 删除任务是否删除文件
     */
    public void removeAll(boolean isDeleteFile) {
        Map<String, DownloadTask> map = new HashMap<>(taskMap);
        //先删除未开始的任务
        for (Map.Entry<String, DownloadTask> entry : map.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                Log.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.getStatus() != StatusConst.LOADING) {
                task.remove(isDeleteFile);
            }
        }
        //再删除进行中的任务
        for (Map.Entry<String, DownloadTask> entry : map.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                Log.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.getStatus() == StatusConst.LOADING) {
                task.remove(isDeleteFile);
            }
        }
    }


    public OkDownload setFolder(String folder) {
        this.folder = folder;
        return this;
    }


    public boolean hasTask(String tag) {

        return taskMap.containsKey(tag);
    }

    public void addOnAllTaskEndListener(OnAllTaskEndListener listener) {
        threadPool.getExecutor().addOnAllTaskEndListener(listener);
    }

    public void removeOnAllTaskEndListener(OnAllTaskEndListener listener) {
        threadPool.getExecutor().removeOnAllTaskEndListener(listener);
    }


}
