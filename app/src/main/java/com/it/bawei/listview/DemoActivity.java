package com.it.bawei.listview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.it.bawei.listview.list.ListViewDemoActivity;
import com.it.bawei.listview.lrucache.LruCacheDemoActivity;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                Intent intent1 = new Intent();
                intent1.setClass(this,ListViewDemoActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn2:
                Intent intent2 = new Intent();
                intent2.setClass(this,LruCacheDemoActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
