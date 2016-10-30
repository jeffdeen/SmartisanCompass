package com.example.jeff.compass;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by jeffdeen on 2016/10/27.
 */

public class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
