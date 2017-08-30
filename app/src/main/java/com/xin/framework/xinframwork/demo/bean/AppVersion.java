package com.xin.framework.xinframwork.demo.bean;

import java.io.Serializable;

/**
 * Description :
 * Created by 王照鑫 on 2017/8/29 0029.
 */

public class AppVersion implements Serializable {
    public String upgradeType;    //升级类型	1	Y	0：无需升级 1:建议升级 2：强制升级
    public String appver;//	版本号	10	Y	例
    public String upgradeInfo;//	升级说明	500	Y
    public String downloadUrl;//	下载地址	200	Y	https://


    @Override
    public String toString() {
        return "AppVersion{" +
                "upgradeType='" + upgradeType + '\'' +
                ", appver='" + appver + '\'' +
                ", upgradeInfo='" + upgradeInfo + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
