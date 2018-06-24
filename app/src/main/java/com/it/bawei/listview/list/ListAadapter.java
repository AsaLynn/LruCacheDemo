package com.it.bawei.listview.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.okhttputils.request.OkHttpRequest;
import com.it.bawei.listview.NewsInfo;
import com.it.bawei.listview.R;

import java.util.List;

/**
 *
 */

class ListAadapter extends BaseAdapter {

    private List<NewsInfo> mList;

    public ListAadapter(List<NewsInfo> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHodler hodler = null;
        if (convertView == null) {
            //参数:1,context.3,布局文件id.父控件
            convertView = View.inflate(parent.getContext(), R.layout.item_demo, null);
            hodler = new ViewHodler();
            hodler.iv_demo = convertView.findViewById(R.id.iv_demo);
            hodler.tv_summary = convertView.findViewById(R.id.tv_summary);
            hodler.tv_subject = convertView.findViewById(R.id.tv_subject);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        hodler.tv_subject.setText(mList.get(position).getSubject());
        hodler.tv_summary.setText(mList.get(position).getSummary());
        hodler.iv_demo.setTag(mList.get(position).getCover());
        String baseUrl = "http://litchiapi.jstv.com";
        new OkHttpRequest.Builder().url(baseUrl + mList.get(position).getCover()).errResId(R.mipmap.ic_launcher).imageView(hodler.iv_demo).displayImage();
        return convertView;
    }

    class ViewHodler {
        ImageView iv_demo;
        TextView tv_subject;
        TextView tv_summary;
    }

}
