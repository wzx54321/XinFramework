package com.xin.framework.xinframwork.http.request.base;

import android.support.annotation.NonNull;

import com.xin.framework.xinframwork.http.callback.Callback;
import com.xin.framework.xinframwork.http.utils.HttpUtils;
import com.xin.framework.xinframwork.store.entity.EntityUpload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：包装的请求体，处理进度，可以处理任何的 RequestBody，
 * 修订历史：
 * ================================================
 */
public class ProgressRequestBody<T> extends RequestBody {

    private RequestBody requestBody;         //实际的待包装请求体
    private Callback<T> callback;
    private UploadInterceptor interceptor;

    public ProgressRequestBody(RequestBody requestBody, Callback<T> callback) {
        this.requestBody = requestBody;
        this.callback = callback;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }


    @Override
    public long contentLength() {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


    private final   class CountingSink extends ForwardingSink{
        private EntityUpload progress;

        public CountingSink(Sink delegate) {
            super(delegate);
            progress=new EntityUpload();

            progress.setTotalSize(contentLength());
        }


        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            EntityUpload.changeProgress(progress, byteCount, new EntityUpload.Action() {
                @Override
                public void call(EntityUpload progress) {
                    if (interceptor != null) {
                        interceptor.uploadProgress(progress);
                    } else {
                        onProgress(progress);
                    }
                }
            });

        }
    }

    private void onProgress(final EntityUpload progress) {
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.uploadProgress(progress);
                }
            }
        });
    }

    public void setInterceptor(UploadInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public interface UploadInterceptor {
        void uploadProgress(EntityUpload progress);
    }
}
