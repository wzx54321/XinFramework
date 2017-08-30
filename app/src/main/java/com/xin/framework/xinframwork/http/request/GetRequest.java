package com.xin.framework.xinframwork.http.request;

import com.xin.framework.xinframwork.http.model.HttpMethod;
import com.xin.framework.xinframwork.http.request.base.NoBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/23 0023.
 * Job number：147109
 * Email： wangzhaoxin@syswin.com
 * Person in charge :
 * Leader：guohaichun
 */

public class GetRequest<T> extends NoBodyRequest {

    public GetRequest(String url) {
        super(url);
    }


    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.get().url(url).tag(tag).build();
    }


}
