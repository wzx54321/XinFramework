package com.xin.framework.xinframwork;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xin.framework.xinframwork.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements AbsListView.OnScrollListener {
    @BindView(R.id.listView)
    ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterCreated() {

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
        mTitle.onScroll(view,
                firstVisibleItem,
                0);
    }


    static class MyAdapter extends BaseAdapter {

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
