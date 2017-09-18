package com.xin.framework.xinframwork.store.entity;

import com.xin.framework.xinframwork.http.request.base.Request;
import com.xin.framework.xinframwork.utils.common.io.IOUtils;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

/**
 * Description :上传文件数据数据库实体
 * Created by 王照鑫 on 2017/9/15 0015.
 */
@Entity
public class EntityUpload {
    @Id
    private long id;

    @Index
    private String tag;//下载的标识键
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
    public Request<?, ? extends Request> request;   //网络请求
    @Transient
    public transient long speed;                    //网速，byte/s


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public byte[] getRequestData() {
        if (requestData == null && request != null) {
            requestData = IOUtils.toByteArray(request);
        }
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;

        if(requestData!=null){
            setRequest((Request<?, ? extends Request>) IOUtils.toObject(requestData));
        }
    }

    public Request<?, ? extends Request> getRequest() {
        return request;
    }

    public void setRequest(Request<?, ? extends Request> request) {
        this.request = request;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }
}
