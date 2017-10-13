package com.xin.framework.xinframwork.http.plugins.up_download.task;

/**
 * Description : Runnable对象的优先级封装
 * Created by xin on 2017/9/28 0028.
 */

public class PriorityRunnable extends PriorityObject<Runnable> implements Runnable{



    public PriorityRunnable(int priority, Runnable obj) {
        super(priority, obj);
    }

    @Override
    public void run() {
        this.obj.run();
    }
}
