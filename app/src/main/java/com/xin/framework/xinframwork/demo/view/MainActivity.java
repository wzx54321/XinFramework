package com.xin.framework.xinframwork.demo.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xin.framework.xinframwork.R;
import com.xin.framework.xinframwork.base.BaseActivity;
import com.xin.framework.xinframwork.demo.contract.MainContract;
import com.xin.framework.xinframwork.demo.presenter.MainPresenter;
import com.xin.framework.xinframwork.hybrid.activity.CommWebViewActivity;
import com.xin.framework.xinframwork.hybrid.bean.WebOpenInfo;
import com.xin.framework.xinframwork.mvp.PresenterMessage;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements AbsListView.OnScrollListener, MainContract.View {
    @BindView(R.id.listView)
    ListView mListView;


    @Override
    public void createMessage() {
        msg = PresenterMessage.obtain(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterCreated() {

        msg = PresenterMessage.obtain(this);

        mPresenter.checkVersion(msg);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommWebViewActivity.launcher(MainActivity.this, new WebOpenInfo("http://m.youku.com/", null, null));
            }
        });

    }

    @Override
    protected void initView() {
        mListView.setAdapter(new MyAdapter());
        mListView.setOnScrollListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mTitle != null) {
            mTitle.onScroll(view,
                    firstVisibleItem,
                    0);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void handlePresenterMsg(PresenterMessage message) {

    }


    private static class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 1000;
        }

        @Override
        public Object getItem(int position) {
            return "一个快速开发的框架";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = new TextView(parent.getContext());
            ((TextView) convertView).setText("一个快速开发的框架");
            convertView.setPadding(100, 50, 100, 50);
            return convertView;
        }
    }


}
