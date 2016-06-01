package com.example.jeff.compass;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jeff on 2015/9/28.
 */
public class AboutActivity extends Activity  {

    private Button backButton;

    private String[] titles = new String[]{
            "开发", "设计", "反馈"
    };
    private String[] contents = new String[]{
            "@丁金锋JEFF", "@阿难天", "jeffdeen@qq.com"
    };


    private LinearLayout linearLayout;
    private LinearLayout updateLinlayout;
    private LinearLayout feedbackLinlayout;
    private LinearLayout aboutDevLinlayout;
    private TextView update_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);
        backButton = (Button) findViewById(R.id.backButton);
        linearLayout = (LinearLayout) findViewById(R.id.devBackground);
        updateLinlayout=(LinearLayout)findViewById(R.id.update);
        feedbackLinlayout=(LinearLayout)findViewById(R.id.feedback);
        aboutDevLinlayout=(LinearLayout)findViewById(R.id.aboutDev);
        ButtonListener listener=new ButtonListener();
        updateLinlayout.setOnClickListener(listener);
        feedbackLinlayout.setOnClickListener(listener);
        aboutDevLinlayout.setOnClickListener(listener);
        update_text=(TextView)findViewById(R.id.update_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(0, R.animator.anim4);
            }
        });

        /*List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
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
                cmb.setText(contents[position].trim());
                Toast.makeText(getApplicationContext(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.update:
                    break;
                case R.id.feedback:
                    break;
                case R.id.aboutDev:
                    break;
            }
        }
    }
    @Override
    public void onBackPressed() {// 覆盖返回键
        finish();
        overridePendingTransition(0, R.animator.anim4);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
