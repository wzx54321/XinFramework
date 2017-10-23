package com.xin.framework.xinframwork.utils.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xin.framework.xinframwork.app.XinApplication;
import com.xin.framework.xinframwork.utils.android.logger.Log;
import com.xin.framework.xinframwork.utils.common.rxerrorhandler.core.RxErrorHandler;
import com.xin.framework.xinframwork.utils.common.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.xin.framework.xinframwork.utils.common.rxerrorhandler.handler.listener.ResponseErrorListener;

import java.util.ArrayList;
import java.util.List;



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

            if (errorHandler == null) {

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
                                Log.d("Request permissions success");
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                Log.d("Request permissions failure");
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
     * 短信所有权限
     */
    public static void SmsAll(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.BROADCAST_SMS,
                Manifest.permission.RECEIVE_MMS, Manifest.permission.RECEIVE_WAP_PUSH);
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
    public static void readPhoneState(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.READ_PHONE_STATE);
    }


    /**
     * 联系人权限
     */
    public static void contacts(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler,Manifest.permission.GET_ACCOUNTS);
    }

    /**
     * 日历权限
     */
    public static void calendar(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR);
    }

    /**
     * 位置权限
     */
    public static void location(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    /**
     * 传感器权限
     */
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public static void sensors(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {

            requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.BODY_SENSORS);

    }


    /**
     * 麦克风权限
     */
    public static void microphone(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.RECORD_AUDIO);
    }

}

