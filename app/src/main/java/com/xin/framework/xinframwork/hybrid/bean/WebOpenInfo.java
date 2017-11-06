package com.xin.framework.xinframwork.hybrid.bean;

import java.io.Serializable;

/**
 * Description :
 * Created by 王照鑫 on 2017/11/3 0003.
 */

public class WebOpenInfo implements Serializable {

    private String url;

    private String htmlContent;


    private WebPostParams<String, String> params;




    public WebOpenInfo(String url, String htmlContent, WebPostParams<String, String> params) {
        this.url = url;
        this.htmlContent = htmlContent;
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }


    public WebPostParams<String, String> getParams() {
        return params;
    }

    public void setParams(WebPostParams<String, String> params) {
        this.params = params;
    }
}
