package com.xin.framework.xinframwork.http.plugins.up_download;

/**
 * Description :下载上传状态
 * Created by 王照鑫 on 2017/9/28 0028.
 */

public interface StatusConst {

    int NONE = 0;         //无状态
    int WAITING = 1;      //等待
    int LOADING = 2;      //下载中
    int PAUSE = 3;        //暂停
    int ERROR = 4;        //错误
    int FINISH = 5;       //完成

}
