
package com.xin.framework.xinframwork.utils.android;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.xin.framework.xinframwork.utils.android.logger.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
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
                    .hasMoreElements(); ) {
                NetworkInterface anInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = anInterface.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        Log.i("IP info:"+inetAddress.getHostAddress(),"");
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
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
            telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
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
                        && !TextUtils.equals(deviceId1, "000000000000000") && !TextUtils.equals(deviceId1, "9774d56d682e549c")/*厂商定制系统的Bug*/) {
                    return deviceId1;
                }
            }
        } catch (Exception e2) {
            Log.printStackTrace(e2);
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
         //   Log.printStackTrace(e);
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

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }




    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    public static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
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





    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            Log.printStackTrace(throwable);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                Log.printStackTrace(exception);
            }
        }
        return null;
    }

    /**
     * whether this process is named with processName
     *
     * @param context
     * @param processName
     * @return <ul>
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     * <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     * true, otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (processInfoList == null) {
            return true;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid && processInfo.processName.equals(processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取渠道信息
     */
    public static ChannelInfo readChannel(Context app) {

        @SuppressWarnings("UnusedAssignment") final long startTime = System.currentTimeMillis();
        return WalleChannelReader.getChannelInfo(app);

        // TODO
       /* if (channelInfo != null) {
            tv.setText(channelInfo.getChannel() + "\n" + channelInfo.getExtraInfo());
        }
        Toast.makeText(this, "ChannelReader takes " + (System.currentTimeMillis() - startTime) + " milliseconds", Toast.LENGTH_SHORT).show();*/
    }

    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}