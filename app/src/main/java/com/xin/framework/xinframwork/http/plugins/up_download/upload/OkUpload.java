package com.xin.framework.xinframwork.http.plugins.up_download.upload;

import com.xin.framework.xinframwork.http.plugins.up_download.StatusConst;
import com.xin.framework.xinframwork.http.plugins.up_download.listener.OnAllTaskEndListener;
import com.xin.framework.xinframwork.http.plugins.up_download.task.UploadTask;
import com.xin.framework.xinframwork.http.request.base.Request;
import com.xin.framework.xinframwork.store.box.UploadBox;
import com.xin.framework.xinframwork.store.entity.EntityUpload;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : 全局的上传管理
 * Created by xin on 2017/9/29 0029.
 */

public class OkUpload {
    private Map<String, UploadTask<?>> taskMap;         //所有任务
    private UploadThreadPool threadPool;                //上传的线程池
    private UploadBox mBox;


    public static OkUpload getInstance() {

        return OkUploadHolder.instance;
    }

    private static class OkUploadHolder {
        static final OkUpload instance = new OkUpload();
    }

    private OkUpload() {
        threadPool = new UploadThreadPool();
        taskMap = new LinkedHashMap<>();
        mBox = new UploadBox();
        //校验数据的有效性，防止下载过程中退出，第二次进入的时候，由于状态没有更新导致的状态错误
        List<EntityUpload> taskList = mBox.getUploading();

        for (EntityUpload info : taskList) {
            if (info.getStatus() == StatusConst.WAITING || info.getStatus() == StatusConst.LOADING || info.getStatus() == StatusConst.PAUSE) {
                info.setStatus(StatusConst.NONE);
            }
        }

        mBox.replace(taskList);

    }


    public static <T> UploadTask<T> request(String tag, Request<T, ? extends Request> request) {
        Map<String, UploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        //noinspection unchecked
        UploadTask<T> task = (UploadTask<T>) taskMap.get(tag);
        if (task == null) {
            task = new UploadTask<>(tag, request);
            taskMap.put(tag, task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static <T> UploadTask<T> restore(EntityUpload progress) {
        Map<String, UploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        //noinspection unchecked
        UploadTask<T> task = (UploadTask<T>) taskMap.get(progress.getTag());
        if (task == null) {
            task = new UploadTask<>(progress);
            taskMap.put(progress.getTag(), task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static List<UploadTask<?>> restore(List<EntityUpload> progressList) {
        Map<String, UploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        List<UploadTask<?>> tasks = new ArrayList<>();
        for (EntityUpload progress : progressList) {
            UploadTask<?> task = taskMap.get(progress.getTag());
            if (task == null) {
                task = new UploadTask<>(progress);
                taskMap.put(progress.getTag(), task);
            }
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * 开始所有任务
     */
    public void startAll() {
        for (Map.Entry<String, UploadTask<?>> entry : taskMap.entrySet()) {
            UploadTask<?> task = entry.getValue();
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
        for (Map.Entry<String, UploadTask<?>> entry : taskMap.entrySet()) {
            UploadTask<?> task = entry.getValue();
            if (task == null) {
                Log.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.getStatus() != StatusConst.LOADING) {
                task.pause();
            }
        }
        //再停止进行中的任务
        for (Map.Entry<String, UploadTask<?>> entry : taskMap.entrySet()) {
            UploadTask<?> task = entry.getValue();
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
        Map<String, UploadTask<?>> map = new HashMap<>(taskMap);
        //先删除未开始的任务
        for (Map.Entry<String, UploadTask<?>> entry : map.entrySet()) {
            UploadTask<?> task = entry.getValue();
            if (task == null) {
                Log.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.getStatus() != StatusConst.LOADING) {
                task.remove();
            }
        }
        //再删除进行中的任务
        for (Map.Entry<String, UploadTask<?>> entry : map.entrySet()) {
            UploadTask<?> task = entry.getValue();
            if (task == null) {
                Log.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.getStatus() == StatusConst.LOADING) {
                task.remove();
            }
        }
    }


    public UploadThreadPool getThreadPool() {
        return threadPool;
    }

    public Map<String, UploadTask<?>> getTaskMap() {
        return taskMap;
    }

    public UploadTask<?> getTask(String tag) {
        return taskMap.get(tag);
    }

    public boolean hasTask(String tag) {
        return taskMap.containsKey(tag);
    }

    public UploadTask<?> removeTask(String tag) {
        return taskMap.remove(tag);
    }


    public void addOnAllTaskEndListener(OnAllTaskEndListener listener) {
        threadPool.getExecutor().addOnAllTaskEndListener(listener);
    }

    public void removeOnAllTaskEndListener(OnAllTaskEndListener listener) {
        threadPool.getExecutor().removeOnAllTaskEndListener(listener);
    }

}
