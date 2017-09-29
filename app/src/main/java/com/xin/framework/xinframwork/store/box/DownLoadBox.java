package com.xin.framework.xinframwork.store.box;

import com.xin.framework.xinframwork.http.plugins.up_download.StatusConst;
import com.xin.framework.xinframwork.store.box.base.BaseBoxManager;
import com.xin.framework.xinframwork.store.entity.EntityDownload;
import com.xin.framework.xinframwork.store.entity.EntityDownload_;

import java.util.List;

/**
 * Description : 下载数据
 * Created by 王照鑫 on 2017/9/28 0028.
 */

public class DownLoadBox extends BaseBoxManager<EntityDownload> {

    public DownLoadBox() {
        super(EntityDownload.class);
    }

    @Override
    public String getTableName() {
        return EntityDownload_.__DB_NAME;
    }


    /**
     * 获取所有下载信息
     */
    public List<EntityDownload> getDownloading() {
        return getQueryBuilder().notIn(EntityDownload_.status, new int[]{StatusConst.FINISH}).
                order(EntityDownload_.date).build().find();
    }

    public void replace(List<EntityDownload> downloadList) {
        if (downloadList != null) {
            for (EntityDownload entity : downloadList) {
                update(entity);
            }
        }
    }


    public void replace(EntityDownload entityDownload) {
        update(entityDownload);
    }

    /**
     * 获取下载任务
     */
    public EntityDownload get(String tag) {
        return getQueryBuilder().equal(EntityDownload_.tag, tag).build().findUnique();
    }


    /**
     * 移除下载任务
     */
    public void delete(String taskKey) {

        delete(getQueryBuilder().equal(EntityDownload_.tag, taskKey).build().findUnique());

    }

    /**
     * 更新下载任务
     */
    @Override
    public long update(EntityDownload progress) {
        EntityDownload entityDownload = get(progress.getTag());
        if (entityDownload != null)
            progress.setId(entityDownload.getId());


        return updateNew(progress);
    }


    private long updateNew(EntityDownload object) {
        object.getRequestData();
        return super.update(object);
    }


    /**
     * 获取所有下载信息
     */
    public List<EntityDownload> getAll() {
        return getQueryBuilder().order(EntityDownload_.date).build().find();
    }

    /**
     * 获取所有下载信息
     */
    public List<EntityDownload> getFinished() {
        return getQueryBuilder().equal(EntityDownload_.status, StatusConst.FINISH).order(EntityDownload_.date).build().find();
    }


    /**
     * 清空下载任务
     */
    public void clear() {
        deleteAll();
    }
}

