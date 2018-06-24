package com.it.bawei.listview.lrucache;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.okhttputils.callback.ResultCallback;
import com.example.okhttputils.request.OkHttpRequest;
import com.it.bawei.listview.NewsInfo;
import com.it.bawei.listview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/*

图片内存缓存技术.
 */
public class LruCacheDemoActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private ListView lv;
    private List<NewsInfo> list;
    protected MyAadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lru_cache_demo);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("列表");
        lv = (ListView) findViewById(R.id.lv_demo);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LruCacheDemoActivity.this, "第" + position + "条数据图片被移除了!", Toast.LENGTH_SHORT).show();
                adapter.remove(list.get(position).getCover());
            }
        });

        //设置适配器.
        list = new ArrayList<>();

        request();
    }

    private void request() {
        String url = "http://litchiapi.jstv.com/api/GetFeeds?column=4&PageSize=20&pageIndex=1&val=100511D3BE5301280E0992C73A9DEC41";
        new OkHttpRequest.Builder().url(url).errResId(R.mipmap.ic_launcher).get(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception exception) {
                Log.i(TAG, "onError: " + exception.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                parseData(response);
                adapter = new MyAadapter(list);
                lv.setAdapter(adapter);
            }

        });
    }

    private void parseData(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject paramz = jsonObject.getJSONObject("paramz");
            JSONArray feeds = paramz.getJSONArray("feeds");
            for (int i = 0; i < feeds.length(); i++) {
                NewsInfo info = new NewsInfo();
                JSONObject object = feeds.getJSONObject(i);
                JSONObject data = object.getJSONObject("data");
                String cover = data.getString("cover");
                String subject = data.getString("subject");
                String summary = data.getString("summary");
                info.setCover(cover);
                info.setSummary(summary);
                info.setSubject(subject);
                list.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action1) {
            if (null != adapter) {
                adapter.evictAll();
            }
            return true;
        } else if (id == R.id.action2) {
            if (null != adapter) {
                Toast.makeText(this, "已经缓存的大小是" + adapter.size() + "kb", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action3) {
            if (null != adapter) {
                Toast.makeText(this, "缓存容量是" + adapter.maxSize() + "kb", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action4) {
            if (null != adapter) {
                Toast.makeText(this, "获取到缓存的次数" + adapter.hitCount() + "次", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action5) {
            if (null != adapter) {
                Toast.makeText(this, "获取不到缓存次数" + adapter.missCount() + "次", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action6) {
            if (null != adapter) {
                Toast.makeText(this, "创建缓存资源次数" + adapter.createCount() + "次", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action7) {
            if (null != adapter) {
                Toast.makeText(this, "添加缓存次数" + adapter.putCount() + "次", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action8) {
            if (null != adapter) {
                Toast.makeText(this, "清除缓存次数" + adapter.evictionCount() + "次", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action9) {
            if (null != adapter) {
                adapter.resize();
            }
        } else if (id == R.id.action10) {
            if (null != adapter) {
                adapter.trimToSize(500);

            }
        }
        //resize
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
