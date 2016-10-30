package com.example.jeff.compass;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

/**
 * Created by Jeff on 2015/9/28.
 */
public class AboutActivity extends BaseActivity  {

    private Button backButton;



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

    }
    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.update:
                    PgyUpdateManager.register(AboutActivity.this,
                            new UpdateManagerListener() {
                                @Override
                                public void onUpdateAvailable(final String result) {
                                    final AppBean appBean = getAppBeanFromString(result);
                                    new AlertDialog.Builder(AboutActivity.this)
                                            .setTitle("更新")
                                            .setMessage(appBean.getReleaseNote())
                                            .setPositiveButton(
                                                    "确定",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            startDownloadTask(
                                                                    AboutActivity.this,
                                                                    appBean.getDownloadURL());
                                                        }
                                                    })
                                            .setNegativeButton("取消",null)
                                            .show();
                                }

                                @Override
                                public void onNoUpdateAvailable() {
                                    Toast.makeText(getApplicationContext(), "已经是最新版本",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
                case R.id.feedback:
                    //FeedbackActivity.setBarImmersive(true);
                    //PgyFeedback.getInstance().showDialog(AboutActivity.this);
                    Intent data=new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:jeffdeen@qq.com"));
                    data.putExtra(Intent.EXTRA_SUBJECT, "锤子指南针反馈");
                    data.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(data);
                    //PgyFeedback.getInstance().showActivity(AboutActivity.this);
                    break;
                case R.id.aboutDev:
                    Intent intent = new Intent(AboutActivity.this, AboutDev.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.in_from_right, R.animator.out_to_left);
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
