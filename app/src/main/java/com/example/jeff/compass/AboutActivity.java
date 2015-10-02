package com.example.jeff.compass;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeff on 2015/9/28.
 */
public class AboutActivity extends Activity {

    private Button backButton;

    private String[] titles = new String[]{
            "名称", "版本", "作者", "反馈"
    };
    private String[] contents = new String[]{
            "指南针", "beta 0.91", "@丁金锋JEFF", "jeffdeen@qq.com"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.animator.anim4);
            }
        });

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < titles.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("titles", titles[i]);
            listItem.put("contents", contents[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.simple_item,
                new String[]{"titles", "contents"},
                new int[]{R.id.title, R.id.content});
        ListView list = (ListView) findViewById(R.id.mylist);
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 2:
                        cmb.setText(contents[position].trim());
                        Toast.makeText(AboutActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                    case 3:
                        cmb.setText(contents[position].trim());
                        Toast.makeText(AboutActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {// 覆盖返回键
        finish();
        overridePendingTransition(0, R.animator.anim4);
    }
}
