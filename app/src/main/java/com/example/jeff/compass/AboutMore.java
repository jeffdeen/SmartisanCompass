package com.example.jeff.compass;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutMore extends BaseActivity {
    private Button back_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_layout);
        back_bt=(Button)findViewById(R.id.backButton);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
            }
        });
    }
}
