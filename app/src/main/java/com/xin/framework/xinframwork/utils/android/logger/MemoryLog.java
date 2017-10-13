/*
 * 创建日期：2014-4-15
 */
package com.xin.framework.xinframwork.utils.android.logger;

import android.os.Debug;

import com.xin.framework.xinframwork.BuildConfig;

/**
 * 描述：打印当前应用占用的内存情况
 * 
 * @author Xin
 */
public class MemoryLog {

    public static boolean       DEBUG_MEMORY = BuildConfig.DEBUG;      // true表示支持打印内存占用情况，false表示不支持打印占用情况


    /**
     * 打印Dalvik内存占用和native占用情况
     * 
     * @param info  相关信息
     */
    public static void printMemory(String info) {
        if (  !DEBUG_MEMORY) {
            return;
        }
        // System.gc();(gc会导致系统回收垃圾，使系统慢)
        printDalvikMemory(info);
        nativePrintMemory(info);
        memoryApp(info);
    }

    /**
     * 打印Dalvik内存占用情况
     * 
     * @param info 相关信息
     */
    public static void printDalvikMemory(String info) {
        if (  !DEBUG_MEMORY) {
            return;
        }
        long totalMemory = Runtime.getRuntime()
                                  .totalMemory();
        long freeMemory = Runtime.getRuntime()
                                 .freeMemory();
        long usedMemory = (totalMemory - freeMemory) >> 10;
        freeMemory = freeMemory >> 10;
        totalMemory = totalMemory >> 10;
        String content = info + "-->totalMemory:" + totalMemory + ",freeMemory:" + freeMemory + ",usedMemory:" + usedMemory + "\n";
        Log .d(
            content);
    }

    /**
     * 打印native内存占用情况
     * 
     * @param info 相关信息
     */
    public static void nativePrintMemory(String info) {
        if (  !DEBUG_MEMORY) {
            return;
        }
        long totalNativeMemory = Debug.getNativeHeapAllocatedSize();
        long freeNativeMemory = Debug.getNativeHeapFreeSize();
        long nativeMemory = Debug.getNativeHeapSize();
        String content =
            info + "-->totalNativeMemory:" + (totalNativeMemory >> 10) + ",freeNativeMemory:" + (freeNativeMemory >> 10) + ",nativeMemory:" + (nativeMemory >> 10) + "\n";
        Log.d(
            content);
    }

    /**
     * 打印内存信息
     */
    public static void memoryApp(String info) {

        if ( !DEBUG_MEMORY) {
            return;
        }

        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        int dalvikPrivateDirty = memoryInfo.dalvikPrivateDirty;
        int dalvikPss = memoryInfo.dalvikPss;
        int dalvikSharedDirty = memoryInfo.dalvikSharedDirty;
        int nativePrivateDirty = memoryInfo.nativePrivateDirty;
        int nativePss = memoryInfo.nativePss;
        int nativeSharedDirty = memoryInfo.nativeSharedDirty;
        int otherPss = memoryInfo.otherPss;
        int otherSharedDirty = memoryInfo.otherSharedDirty;
        String content =
            info + "-->dalvikPrivateDirty：" + dalvikPrivateDirty + ",dalvikPss:" + dalvikPss + ",dalvikSharedDirty:" + dalvikSharedDirty + ",nativePrivateDirty:" +
                    nativePrivateDirty + ",nativePss:" + nativePss + ",nativeSharedDirty:" + nativeSharedDirty + ",otherPss:" + otherPss + ",otherSharedDirty:" + otherSharedDirty +
                    "\n";
        Log.d(
            content);
    }

}
