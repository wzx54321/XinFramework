package com.xin.framework.xinframwork.hybrid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.base.BaseActivity;
import com.xin.framework.xinframwork.common.IntentParameter;
import com.xin.framework.xinframwork.hybrid.bean.WebOpenInfo;
import com.xin.framework.xinframwork.hybrid.fragment.CommonWebFragment;

/**
 * Description : 通用的Webview Activity
 * Created by 王照鑫 on 2017/11/3 0003.
 */

public class CommWebViewActivity extends BaseActivity {


    public static void launcher(Context context, @NonNull WebOpenInfo info) {
        Intent intent = new Intent(context, CommWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentParameter.extras.WEB_OPEN_INFO, info);
        intent.putExtras(bundle);
        if (!(context instanceof Activity))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    public void createMessage() {
    }

    @Override
    protected void afterCreated() {

        Bundle bundle = getIntent().getExtras();
        CommonWebFragment mWebFragment = CommonWebFragment.getInstance(bundle);
        loadRootFragment(R.id.comm_root_web_activity, mWebFragment);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_activity_web;
    }


}
