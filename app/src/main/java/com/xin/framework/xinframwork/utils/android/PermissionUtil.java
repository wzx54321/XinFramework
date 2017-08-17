package com.xin.framework.xinframwork.utils.android;

import android.Manifest;
import android.content.Context;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;


/**
 * Created by jess on 17/10/2016 10:09
 * Contact with jess.yan.effort@gmail.com
 */

public class PermissionUtil {
    public static final String TAG = "Permission";


    private PermissionUtil() {
    }

    public interface RequestPermission {
        void onRequestPermissionSuccess();

        void onRequestPermissionFailure();
    }


    public static void requestPermission(final RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler, String... permissions) {
        if (permissions == null || permissions.length == 0) return;

        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        if (needRequest.size() == 0) {//全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过,则开始申请

            if(errorHandler==null){

                errorHandler = RxErrorHandler
                        .builder()
                        .with(XinApplication.getAppContext())
                        .responseErrorListener(new ResponseErrorListener() {
                            @Override
                            public void handleResponseError(Context context, Throwable t) {
                                Log.e(t, "error handle");

                            }
                        }).build();
            }

            rxPermissions
                    .request(needRequest.toArray(new String[needRequest.size()]))
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                              Log.d("Request permissons success");
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                Log.d("Request permissons failure");
                                requestPermission.onRequestPermissionFailure();
                            }
                        }
                    });
        }

    }


    /**
     * 请求摄像头权限
     */
    public static void launchCamera(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }


    /**
     * 请求外部存储的权限
     */
    public static void externalStorage(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    /**
     * 请求发送短信权限
     */
    public static void sendSms(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.SEND_SMS);
    }


    /**
     * 请求打电话权限
     */
    public static void callPhone(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.CALL_PHONE);
    }


    /**
     * 请求获取手机状态的权限
     */
    public static void readPhonestate(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.READ_PHONE_STATE);
    }

}

