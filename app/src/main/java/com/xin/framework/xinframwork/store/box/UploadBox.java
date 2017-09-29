package com.xin.framework.xinframwork.store.box;

import com.xin.framework.xinframwork.http.plugins.up_download.StatusConst;
import com.xin.framework.xinframwork.store.box.base.BaseBoxManager;
import com.xin.framework.xinframwork.store.entity.EntityUpload;
import com.xin.framework.xinframwork.store.entity.EntityUpload_;

import java.util.List;

/**
 * Description : 上传 数据库 Box
 * Created by 王照鑫 on 2017/9/28 0028.
 */

public class UploadBox extends BaseBoxManager<EntityUpload> {

    public UploadBox() {
        super(EntityUpload.class);
    }

    @Override
    public String getTableName() {
        return EntityUpload_.__DB_NAME;
    }


    /**
     * 获取上传任务
     */
    public EntityUpload get(String tag) {
        return getQueryBuilder().equal(EntityUpload_.tag, tag).build().findUnique();
    }

    /**
     * 移除上传任务
     */
    public void delete(String taskKey) {
        delete(get(taskKey));
    }


    /**
     * 获取所有上传信息
     */
    public List<EntityUpload> getAll() {

        return getQueryBuilder().order(EntityUpload_.date).build().find();
    }

    /**
     * 获取所有上传信息
     */
    public List<EntityUpload> getFinished() {

        return getQueryBuilder().equal(EntityUpload_.status, StatusConst.FINISH).order(EntityUpload_.date).build().find();

    }

    /**
     * 获取所有上传信息
     */
    public List<EntityUpload> getUploading() {

        return getQueryBuilder().notIn(EntityUpload_.status, new int[]{StatusConst.FINISH}).order(EntityUpload_.date).build().find();

    }

    /**
     * 清空上传任务
     */
    public void clear() {
        deleteAll();
    }

    public void replace(List<EntityUpload> taskList) {
        if (taskList != null)
            for (EntityUpload upload : taskList) {
                update(upload);
            }
    }


    public void replace(EntityUpload entityDownload) {
        update(entityDownload);
    }

    @Override
    public long update(EntityUpload progress) {


        EntityUpload entityUpload = get(progress.getTag());
        if (entityUpload != null)
            progress.setId(entityUpload.getId());


        return updateNew(progress);
    }


    private long updateNew(EntityUpload object) {
        object.getRequestData();
        return super.update(object);
    }
}
