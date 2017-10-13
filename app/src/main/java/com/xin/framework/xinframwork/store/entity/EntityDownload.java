package com.xin.framework.xinframwork.store.entity;

import android.os.SystemClock;

import com.xin.framework.xinframwork.http.OkGo;
import com.xin.framework.xinframwork.http.model.Priority;
import com.xin.framework.xinframwork.http.plugins.up_download.bean.Progress;
import com.xin.framework.xinframwork.http.request.base.Request;
import com.xin.framework.xinframwork.utils.common.io.IOUtils;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

/**
 * Description :下载文件数据库实体
 * Created by xin on 2017/9/15 0015.
 */
@Entity
public class EntityDownload extends Progress {
    @Id
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Index
    private String tag;                              //下载的标识键
    private String url;                              //网址
    private String folder;                           //保存文件夹
    private String filePath;                         //保存文件地址
    private String fileName;                         //保存的文件名
    private float fraction;                          //下载的进度，0-1
    private long totalSize;                          //总字节长度, byte
    private long currentSize;                        //本次下载的大小, byte
    private int status;                              //当前状态
    private int priority;                            //任务优先级
    private long date;                               //创建时间
    private byte[] requestData;

    @Transient
    private long speed;                         //网速，byte/s
    @Transient
    private Request<?, ? extends Request> request;   //网络请求
    @Transient
    private transient long tempSize;                //每一小段时间间隔的网络流量
    @Transient
    private transient long lastRefreshTime;         //最后一次刷新的时间
    @Transient
    private transient List<Long> speedBuffer;       //网速做平滑的缓存，避免抖动过快


    @Transient
    public Throwable exception;                     //当前进度出现的异常


    public EntityDownload() {
        lastRefreshTime = SystemClock.elapsedRealtime();
        totalSize = -1;
        priority = Priority.DEFAULT;
        date = System.currentTimeMillis();
        speedBuffer = new ArrayList<>();
    }

    public static EntityDownload changeProgress(EntityDownload entityDownload, long writeSize, Action action) {
        return changeProgress(entityDownload, writeSize, entityDownload.getTotalSize(), action);
    }

    public static EntityDownload changeProgress(final EntityDownload progress, long writeSize, long totalSize, final Action action) {
        progress.totalSize = totalSize;
        progress.currentSize += writeSize;
        progress.tempSize += writeSize;

        long currentTime = SystemClock.elapsedRealtime();
        boolean isNotify = (currentTime - progress.lastRefreshTime) >= OkGo.REFRESH_TIME;
        if (isNotify || progress.currentSize == totalSize) {
            long diffTime = currentTime - progress.lastRefreshTime;
            if (diffTime == 0) diffTime = 1;
            progress.fraction = progress.currentSize * 1.0f / totalSize;
            progress.speed = progress.bufferSpeed(progress.tempSize * 1000 / diffTime);
            progress.lastRefreshTime = currentTime;
            progress.tempSize = 0;
            if (action != null) {
                action.call(progress);
            }
        }
        return progress;
    }


    public byte[] getRequestData() {
        if (requestData == null && request != null) {
            requestData = IOUtils.toByteArray(request);
        }
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;

        if (requestData != null) {
            setRequest((Request<?, ? extends Request>) IOUtils.toObject(requestData));
        }
    }

    public Request<?, ? extends Request> getRequest() {
        return request;
    }

    public void setRequest(Request<?, ? extends Request> request) {
        this.request = request;
        if (requestData == null) {
            setRequestData(IOUtils.toByteArray(requestData));
        }
    }


    /**
     * 平滑网速，避免抖动过大
     */
    private long bufferSpeed(long speed) {
        speedBuffer.add(speed);
        if (speedBuffer.size() > 10) {
            speedBuffer.remove(0);
        }
        long sum = 0;
        for (float speedTemp : speedBuffer) {
            sum += speedTemp;
        }
        return sum / speedBuffer.size();
    }

    /**
     * 转换进度信息
     */
    public void from(EntityDownload progress) {
        totalSize = progress.totalSize;
        currentSize = progress.currentSize;
        fraction = progress.fraction;
        speed = progress.speed;
        lastRefreshTime = progress.lastRefreshTime;
        tempSize = progress.tempSize;
    }

    public interface Action {
        void call(EntityDownload progress);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public float getFraction() {
        return fraction;
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public long getTempSize() {
        return tempSize;
    }

    public void setTempSize(long tempSize) {
        this.tempSize = tempSize;
    }

    public long getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(long lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    public List<Long> getSpeedBuffer() {
        return speedBuffer;
    }

    public void setSpeedBuffer(List<Long> speedBuffer) {
        this.speedBuffer = speedBuffer;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityDownload progress = (EntityDownload) o;
        return tag != null ? tag.equals(progress.tag) : progress.tag == null;

    }

    @Override
    public int hashCode() {
        return tag != null ? tag.hashCode() : 0;
    }


}
