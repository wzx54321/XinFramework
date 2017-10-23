package com.xin.framework.xinframwork.utils.android.logger;


public final class LogSettings {

    private int methodCount = 2;
    private boolean showThreadInfo = true;
    private int methodOffset = 0;
    private AndroidLogAdapter logAdapter;
    private boolean isWriteLogToFile = false;

    /**
     * Determines to how logs will be printed
     */
    private LogLevel logLevel = LogLevel.FULL/* : LogLevel.NONE*/;

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

    public LogSettings logAdapter(AndroidLogAdapter logAdapter) {
        this.logAdapter = logAdapter;
        return this;
    }

    public boolean isWriteLogToFile() {


        if (logAdapter != null) {
            logAdapter.setWriteLogToFile(isWriteLogToFile);
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
