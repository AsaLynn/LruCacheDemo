package com.it.bawei.listview.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.okhttputils.callback.ResultCallback;
import com.example.okhttputils.request.OkHttpRequest;
import com.it.bawei.listview.NewsInfo;
import com.it.bawei.listview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*

ListView加载网络图片!
 */
public class ListViewDemoActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private ListView lv;
    private List<NewsInfo> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lru_cache_demo);
        lv = (ListView) findViewById(R.id.lv_demo);
        //设置适配器.
        list = new ArrayList<>();

        //request1();
        request2();
    }

    private void request2() {
        String url = "http://litchiapi.jstv.com/api/GetFeeds?column=3&PageSize=20&pageIndex=1&val=100511D3BE5301280E0992C73A9DEC41";
        new OkHttpRequest.Builder().url(url).get(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception exception) {
                Log.i(TAG, "onError: " + exception.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                parseData(response);
                lv.setAdapter(new ListAadapter(list));
            }

        });

    }


    private void request1() {
        new Thread() {
            @Override
            public void run() {
                //client
                String url = "http://litchiapi.jstv.com/api/GetFeeds?column=3&PageSize=20&pageIndex=1&val=100511D3BE5301280E0992C73A9DEC41";
                OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request.Builder().url(url).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //
                        Log.i(TAG, "onFailure: --->" + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //获取数据成功.
                        String result = response.body().string();
                        parseData(result);

                        ListViewDemoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新UI
                                lv.setAdapter(new ListAadapter(list));
                            }
                        });
                    }
                });
            }
        }.start();
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
}
