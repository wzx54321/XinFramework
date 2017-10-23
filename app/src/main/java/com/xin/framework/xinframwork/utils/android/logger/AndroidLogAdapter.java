package com.xin.framework.xinframwork.utils.android.logger;

import android.util.Log;

class AndroidLogAdapter implements LogAdapter {

    private boolean isWriteLogToFile;

    public AndroidLogAdapter(boolean writeLogToFile) {
        this.isWriteLogToFile = writeLogToFile;
    }


    public void setWriteLogToFile(boolean writeLogToFile) {
        isWriteLogToFile = writeLogToFile;
    }

    @Override
    public void d(String tag, String message) {
        Log.d(tag, message);
        writeLog2Native(message);

    }

    @Override
    public void e(String tag, String message) {
        Log.e(tag, message);
        writeLog2Native(message);
    }

    @Override
    public void w(String tag, String message) {
        Log.w(tag, message);
        writeLog2Native(message);
    }

    @Override
    public void i(String tag, String message) {
        Log.i(tag, message);
        writeLog2Native(message);
    }

    @Override
    public void v(String tag, String message) {
        Log.v(tag, message);
        writeLog2Native(message);
    }

    @Override
    public void wtf(String tag, String message) {
        Log.wtf(tag, message);

        writeLog2Native(message);
    }


    private void writeLog2Native(final String content) {
        //noinspection StatementWithEmptyBody
        if (isWriteLogToFile) {
            // TODO 写文件
            /*singleThreadExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    FileUtil.clearCacheFolder(FileConfig.getPublicDir(FileConfig.DIR_LOG),
                            CommonConst.LOG_CACHE_KEEP_DAYS);

                    File localFile = new File(FileConfig.getPublicDir(FileConfig.DIR_LOG),
                            "app-Log-" + DateUtil.getCurrentTime(DateUtil.Format.LOG_DIR_DATE_FORMAT) + FileConfig.FILE_NAME_EXTENSION_LOG);
                    FileUtil.writeStringToFile(localFile,
                            !TextUtils.isEmpty(content) && !"---uncaughtException --".equals(content)
                                    ? DateUtil.getCurrentTime((DateUtil.Format.LOG_MSG_DATE_FORMAT)) + "\n     " + content + "\n" : "\n",
                            true);
                }
            });*/
        }

    }
}
