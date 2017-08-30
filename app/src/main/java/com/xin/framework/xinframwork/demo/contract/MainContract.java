package com.xin.framework.xinframwork.demo.contract;

import com.xin.framework.xinframwork.mvp.IModel;
import com.xin.framework.xinframwork.mvp.IPresenter;
import com.xin.framework.xinframwork.mvp.IView;
import com.xin.framework.xinframwork.mvp.PresenterMessage;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/25 0025.
 */

public interface MainContract {

    interface Presenter extends IPresenter {

        /**
         * 检查版本号
         * @param msg
         */
        void checkVersion(PresenterMessage msg);
    }

    interface Model extends IModel {

    }


    interface View extends IView {

    }
}
