/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xin.framework.xinframwork.store.box.base;

import com.xin.framework.xinframwork.common.DBConfig;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

/**
 * Box统一管理
 * @author 王照鑫
 * @param <T> 数据库表实体类
 */
public abstract class BaseBoxManager<T> {


    private String TAG;
    protected Box<T> mBox;
    Class<T> tClass;




    public BaseBoxManager(Class<T> entityClazz) {
        TAG = getClass().getSimpleName();
        this.tClass = entityClazz;
        mBox = DBConfig.getBoxStore().boxFor(tClass);

    }


    protected final void closeDatabase() {
        mBox.closeThreadResources();
    }

    /**
     * 插入一条记录
     *
     * @return The ID of the object within its box.
     */
    public long insert(T entity) {
        if (entity == null) return -1;

        return mBox.put(entity);


    }


    /**
     * 插入多条记录
     */
    public void insert(List<T> entitys) {
        if (entitys != null)

            mBox.put(entitys);

    }


    /**
     * 删除所有数据
     */
    public void deleteAll() {
        mBox.removeAll();
    }


    /**
     * 根据条件删除数据库中的数据
     */
    public void delete(T object) {
        mBox.remove(object);
    }

    /**
     * 删除多条数据
     */
    public void deleteList(List<T> objects) {
        mBox.remove(objects);
    }



    /**
     * 更新一条记录
     */
    public long update(T object ) {

        return  mBox.put(object);
    }



    /**
     *
     * @return Returns a builder to create queries for Object matching supplied criteria.
     */
    public QueryBuilder<T> getQueryBuilder(){
        return mBox.query();
    }

    /**
     * 查询并返回所有对象的集合
     */
    public List<T> queryAll( ) {
        return getQueryBuilder().build().find();
    }

    /**
     * 查询并返回 第一个对象
     */
    public T QueryFirst(){
        return getQueryBuilder().build().findFirst();
    }




    /**
     * 获取对应的表名
     */
    public abstract  String getTableName();


}
