package com.it.bawei.listview.lrucache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.okhttputils.callback.ResultCallback;
import com.example.okhttputils.request.OkHttpRequest;
import com.it.bawei.listview.NewsInfo;
import com.it.bawei.listview.R;

import java.io.InputStream;
import java.util.List;

import okhttp3.Request;

/**
 *
 */

class MyAadapter extends BaseAdapter {

    private List<NewsInfo> mList;
    private LruCache<String, Bitmap> lruCache;
    private String TAG = this.getClass().getSimpleName();
    private String strTAG = "--->***";
    protected final int maxMemory;

    public MyAadapter(List<NewsInfo> list) {
        mList = list;
        //获取分配给应用的最大内存.
        maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        //分配给缓存大小.
        int cacheMemory = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(cacheMemory) {
            //计算Bitmap大小的.
            @Override
            protected int sizeOf(String key, Bitmap value) {
//               return super.sizeOf(key,value);//默认返回缓存的数量
                return value.getByteCount() / 1024;
            }
        };
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHodler hodler = null;
        if (convertView == null) {
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
        //如果缓存中有,直接取出来,给Imageview.
        //否则,就下载.

        final String url = mList.get(position).getCover();
        Bitmap bitmap = lruCache.get(url);
        if (bitmap != null) {
            hodler.iv_demo.setImageBitmap(bitmap);
            Log.i(TAG, strTAG + "图片从缓存中加载了position:" + position);
        } else {
            String imageUrl = "http://litchiapi.jstv.com" + url;
            new OkHttpRequest.Builder().url(imageUrl).errResId(R.mipmap.ic_launcher).imageView(hodler.iv_demo).displayImage(new ResultCallback<InputStream>() {
                    @Override
                    public void onError(Request request, Exception exception) {
                        Log.i(TAG, "onError: " + exception.getMessage());
                    }

                @Override
                public void onResponse(InputStream response) {
                    lruCache.put(url, BitmapFactory.decodeStream(response));
                    Log.i(TAG, strTAG + " 加载网络图片了,图片加入缓存了position:" + position);
                }
            });
        }

        return convertView;
    }

    public void evictAll() {
        if (null != lruCache) {
            //清楚全部。
            lruCache.evictAll();
        }
    }

    public void remove(String cover) {
        if (null != lruCache.get(cover)) {
            lruCache.remove(cover);
        }
    }

    public int size() {
        //已经缓存的大小。
        return lruCache.size();
    }

    public int maxSize() {
        return lruCache.maxSize();
    }

    public int hitCount() {
        //获取缓存得到次数
        return lruCache.hitCount();
    }

    public int missCount() {
        //获取不到缓存次数
        return lruCache.missCount();
    }

    public int createCount() {
        //创建缓存次数0
        return lruCache.createCount();
    }

    public int putCount() {
        //添加缓存次数
        return lruCache.putCount();
    }

    public int evictionCount() {
        //清除缓存次数
        return lruCache.evictionCount();
    }

    public void resize() {
        //重新设置缓存容量.
        lruCache.resize(maxMemory / 4);
    }

    public void trimToSize(int i) {
        //删除缓存到容量为i之下
        lruCache.trimToSize(i);
    }

    class ViewHodler {
        ImageView iv_demo;
        TextView tv_subject;
        TextView tv_summary;
    }
}
