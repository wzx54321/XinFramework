
package com.xin.framework.utils.android;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Description :获取系统参数
 */
public class SysUtils {

    /**
     * 获取手机ip地址
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取设备唯一标识
     * 
     * @param ctx 上下文
     * @return 设备唯一标识
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context ctx) {
        String deviceId1;
        String deviceId2;
        TelephonyManager telephonyManager;
        try {
            telephonyManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
            // MTK平台获取方式，如果不是此平台，则是高通平台
            deviceId1 = getOperatorBySlot(telephonyManager, "getDeviceIdGemini", 0);
            deviceId2 = getOperatorBySlot(telephonyManager, "getDeviceIdGemini", 1);
            // 如果获取MTK平台失败，则获取高通平台的
            if (TextUtils.isEmpty(deviceId1) && TextUtils.isEmpty(deviceId2)) {
                deviceId1 = getOperatorBySlot(telephonyManager, "getDeviceId", 0);
                deviceId2 = getOperatorBySlot(telephonyManager, "getDeviceId", 1);
            }
            // 优先卡1，如果卡1获取不到，则用卡2，都获取不到，则用设备id,最后考虑自动生成
            if (!TextUtils.isEmpty(deviceId1)) {
                return deviceId1;
            } else if (!TextUtils.isEmpty(deviceId2)) {
                return deviceId2;
            } else {
                if (telephonyManager != null) {
                    deviceId1 = telephonyManager.getDeviceId();
                }
                if (!TextUtils.isEmpty(deviceId1)
                        && !TextUtils.equals(deviceId1, "000000000000000")&&!TextUtils.equals(deviceId1, "9774d56d682e549c")/*厂商定制系统的Bug*/) {
                    return deviceId1;
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return Build.SERIAL != null ? Build.SERIAL : UUID.randomUUID().toString();
    }

    private static String getOperatorBySlot(TelephonyManager telephony, String predictedMethodName,
            int slotID) {
        if (telephony == null) {
            return null;
        }
        String inumeric = null;
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);
            if (ob_phone != null) {
                inumeric = ob_phone.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inumeric;
    }

    /**
     * 获取版本号
     */

    public static String getVersion(Context context) {
        String name = "3.0";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return name;
        }
    }

    /**
     * 获取build号
     */
    public static String getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                0);
        return pi.versionCode + "";
    }

    /**
     * 获取设备名称
     */
    public static String getDeviceName() {
        return Build.MODEL;
    }



    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static String getCurProcessName(Context ctx) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null || activityManager.getRunningAppProcesses() == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }


}
