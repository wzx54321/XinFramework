package com.xin.framework.xinframwork.hybrid.video;

import android.view.View;
import android.webkit.WebChromeClient;

/**
 * Description : 视频接口
 * Created by 王照鑫 on 2017/11/3 0003.
 */

public interface IVideo {

    void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback);


    void onHideCustomView();


    boolean isVideoState();


    boolean event();

}
