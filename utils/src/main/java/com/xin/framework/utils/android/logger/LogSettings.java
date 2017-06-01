package com.xin.framework.utils.android.logger;


import com.xin.framework.utils.BuildConfig;

public final class LogSettings {

    private int methodCount = 2;
    private boolean showThreadInfo = true;
    private int methodOffset = 0;
    private LogAdapter logAdapter;
    private boolean isWriteLogToFile = false;

    /**
     * Determines to how logs will be printed
     */
    private LogLevel logLevel = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;

    public LogSettings hideThreadInfo() {
        showThreadInfo = false;
        return this;
    }

    public LogSettings methodCount(int methodCount) {
        if (methodCount < 0) {
            methodCount = 0;
        }
        this.methodCount = methodCount;
        return this;
    }

    public LogSettings logLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public LogSettings methodOffset(int offset) {
        this.methodOffset = offset;
        return this;
    }

    public LogSettings logAdapter(LogAdapter logAdapter) {
        this.logAdapter = logAdapter;
        return this;
    }

    public boolean isWriteLogToFile() {
        if(logAdapter == null){
            if(logAdapter instanceof AndroidLogAdapter){
                ((AndroidLogAdapter) logAdapter).setWriteLogToFile(isWriteLogToFile());
            }
        }

        return isWriteLogToFile;
    }

    public LogSettings setWriteLogToFile(boolean writeLogToFile) {
        isWriteLogToFile = writeLogToFile;
        return this;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public int getMethodOffset() {
        return methodOffset;
    }

    public LogAdapter getLogAdapter() {
        if (logAdapter == null) {
            logAdapter = new AndroidLogAdapter(isWriteLogToFile);
        }


        return logAdapter;
    }

    public void reset() {
        methodCount = 2;
        methodOffset = 0;
        showThreadInfo = true;
        logLevel = LogLevel.FULL;
    }
}
