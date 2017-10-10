package com.xin.framework.xinframwork.demo.view;

import android.content.Intent;
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
import com.xin.framework.xinframwork.mvp.PresenterMessage;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements AbsListView.OnScrollListener, MainContract.View {
    @BindView(R.id.listView)
    ListView mListView;


    @Override
    public void createMessage() {
        msg=PresenterMessage.obtain(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterCreated() {

        msg = PresenterMessage.obtain(this);
        mPresenter.onStart();
        mPresenter.checkVersion(msg);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
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
    protected void initTitleBar() {

        if (mTitle == null) // mTitle可能为空，不判断会警告
            return;
        mTitle.setImmersive(mIsImmersive);
        mTitle.setLeftText("返回");
        mTitle.setTitleColor(R.color._00c853_green_a700);
        mTitle.setBackgroundColor(getResources().getColor(R.color._03a9f4_light_blue_500));
        mTitle.setTitle("新歌的");


        mTitle.setTransparentEnabled(true,
                20,
                60);

        mTitle.addViewToFadeListTitle();


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
            return "sjldajlkdjakldjsakljdsklajdk";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = new TextView(parent.getContext());
            ((TextView) convertView).setText("sjldajlkdjakldjsakljdsklajdk");
            convertView.setPadding(100, 50, 100, 50);
            return convertView;
        }
    }



}
