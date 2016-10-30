package com.example.jeff.compass;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import us.feras.mdv.MarkdownView;

public class Info_Activity extends BaseActivity {
    private Button back_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_layout);
        MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdownView);
        markdownView.loadMarkdownFile("file:///android_asset/reason.md");

        back_bt=(Button)findViewById(R.id.backButton);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
            }
        });
    }
    @Override
    public void onBackPressed() {// 覆盖返回键
        finish();
        overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
    }
}
