package com.example.jeff.compass;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AboutDev extends BaseActivity {
    private LinearLayout dev_line;
    private LinearLayout design_line;
    private LinearLayout more_line;
    private Button back_bt;

    private String[] contents = new String[]{
            "@丁金锋JEFF", "@阿难天"
    };
    ClipboardManager cmb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_developer);
        cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        dev_line=(LinearLayout)findViewById(R.id.dev_line);
        design_line=(LinearLayout)findViewById(R.id.design_line);
        more_line=(LinearLayout)findViewById(R.id.more_line);
        back_bt=(Button)findViewById(R.id.backButton);
        ButtonListener listener=new ButtonListener();
        dev_line.setOnClickListener(listener);
        design_line.setOnClickListener(listener);
        more_line.setOnClickListener(listener);
        back_bt.setOnClickListener(listener);
    }
    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dev_line:
                    Toast.makeText(getApplicationContext(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                    cmb.setText(contents[0].trim());
                    break;
                case R.id.design_line:
                    Toast.makeText(getApplicationContext(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                    cmb.setText(contents[1].trim());
                    break;
                case R.id.more_line:
                    Intent intent = new Intent(AboutDev.this, AboutMore.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.in_from_right, R.animator.out_to_left);
                    break;
                case R.id.backButton:
                    finish();
                    overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                    break;
            }
        }
    }
    @Override
    public void onBackPressed() {// 覆盖返回键
        finish();
        overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
    }
}
