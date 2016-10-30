package com.example.jeff.compass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.pgyersdk.crash.PgyCrashManager;


public class MainActivity extends BaseActivity {

    private final float MAX_ROTATE_DEGREE = 1.0f;// 最多旋转一周，即360°
    private float mDirection;// 当前浮点方向
    private float mTargetDirection;// 目标浮点方向
    private AccelerateInterpolator mInterpolator;// 动画从开始到结束，变化率是一个加速的过程,就是一个动画速率
    protected final Handler mHandler = new Handler();
    protected final Handler mHandler2 = new Handler();
    private boolean mStopDrawing;// 是否停止指南针旋转的标志位
    compassView mPointer;// 指南针view
    private Button aboutbutton;
    private float degree;
    private int rotateDegree;
    private TextView compassDirection;
    private TextView compassDegree;
    private TextView latitudeText;
    private TextView longitudeText;
    private ToggleButton lightButton;
    float distance;
    //locationSensor locationSensor;
    compassSensorClass sensorClass;
    float yAngle;
    float zAngle = 0f;



    //SensorManager sensorManager;
    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
            if (mPointer != null && !mStopDrawing) {
                degree = sensorClass.getvalues(0);// 赋值给全局变量，让指南针旋转
                mTargetDirection = degree * -1.0f;
                yAngle = sensorClass.getvalues(1);
                zAngle = sensorClass.getvalues(2);
                if (mDirection != mTargetDirection) {
                    // calculate the short routine
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }
                    // limit the max speed to MAX_ROTATE_DEGREE
                    distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROTATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROTATE_DEGREE
                                : (-1.0f * MAX_ROTATE_DEGREE);
                    }
                    // need to slow down if the distance is short
                    mDirection = mDirection
                            + ((to - mDirection) * mInterpolator
                            .getInterpolation(Math.abs(distance) > MAX_ROTATE_DEGREE ? 0.3f
                                    : 0.2f));
                    mPointer.updateDirection(mDirection);// 更新指南针旋转

                }

                mHandler.postDelayed(mCompassViewUpdater, 10);// 20毫秒后重新执行自己，比定时器好
            }
        }
    };
    protected Runnable directionUpdater = new Runnable() {
        @Override
        public void run() {
            updateDirection();// 更新方向值
            mHandler2.postDelayed(directionUpdater, 150);// 20毫秒后重新执行自己，比定时器好
        }
    };
    public AMapLocationClient mLocationClient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //蒲公英捕捉crash
        PgyCrashManager.register(this);
        sensorClass = new compassSensorClass(this);
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
        compassDegree = (TextView) findViewById(R.id.compassDegree);
        compassDirection = (TextView) findViewById(R.id.compassDirection);
        mPointer = (compassView) findViewById(R.id.degree);
        mPointer.setRatio(1.0f);


        ButtonListener buttonListener = new ButtonListener();
        aboutbutton = (Button) findViewById(R.id.button);
        aboutbutton.setOnClickListener(buttonListener);
        longitudeText = (TextView) findViewById(R.id.longitudeText);
        latitudeText = (TextView) findViewById(R.id.latitudeText);
        lightButton = (ToggleButton) findViewById(R.id.lightButton);
        lightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lightButton.setBackgroundResource(R.drawable.light_on);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Toast.makeText(getApplicationContext(), "您开启了屏幕常亮", Toast.LENGTH_SHORT).show();
                }
                if (!isChecked) {
                    lightButton.setBackgroundResource(R.drawable.light_off);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Toast.makeText(getApplicationContext(), "您关闭了屏幕常亮", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        showLocation(amapLocation);
                        //Toast.makeText(getApplicationContext(),amapLocation.getLatitude()+" "+
                        //amapLocation.getLongitude(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();
        //initial();
        //updateLevelView();
    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button:
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.anim, R.animator.anim2);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyCrashManager.unregister();
        sensorClass.onstop();
        //locationSensor.onStop();
        //compassView.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ActivityCollector.finishAll();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater, 10);
        mHandler2.postDelayed(directionUpdater, 150);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStopDrawing = true;
    }

    @Override
    public void onBackPressed() {// 覆盖返回
        ActivityCollector.finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void updateDirection() {
        if (Math.abs(distance) < 1) {
            return;
        }
        rotateDegree = (int) sensorClass.getvalues(0);
        //Log.d("nan",""+rotateDegree);
        if (rotateDegree == 0) {
            compassDirection.setText("正北");
            compassDegree.setText(" ");
        } else if (rotateDegree > 0 && rotateDegree <= 45) {
            compassDirection.setText("北偏东");
            compassDegree.setText(" " + rotateDegree + "°");
        } else if (rotateDegree > 45 && rotateDegree < 90) {
            compassDirection.setText("东偏北");
            compassDegree.setText(" " + Math.abs(rotateDegree - 90) + "°");
        } else if (rotateDegree == 90) {
            compassDirection.setText("正东");
            compassDegree.setText(" ");
        } else if (rotateDegree > 90 && rotateDegree <= 135) {
            compassDirection.setText("东偏南");
            compassDegree.setText(" " + (rotateDegree - 90) + "°");
        } else if (rotateDegree > 135 && rotateDegree < 179) {
            compassDirection.setText("南偏东");
            compassDegree.setText(" " + Math.abs(rotateDegree - 179) + "°");
        } else if (rotateDegree == 179 || rotateDegree == -179) {
            compassDirection.setText("正南");
            compassDegree.setText(" ");
        } else if (rotateDegree < -135 && rotateDegree > -179) {
            compassDirection.setText("南偏西");
            compassDegree.setText(" " + Math.abs(rotateDegree + 179) + "°");
        } else if (rotateDegree >= -135 && rotateDegree < -90) {
            compassDirection.setText("西偏南");
            compassDegree.setText(" " + Math.abs(rotateDegree + 90) + "°");
        } else if (rotateDegree == -90) {
            compassDirection.setText("正西");
            compassDegree.setText(" ");
        } else if (rotateDegree > -90 && rotateDegree <= -45) {
            compassDirection.setText("西偏北");
            compassDegree.setText(" " + Math.abs(rotateDegree + 90) + "°");
        } else if (rotateDegree < 0 && rotateDegree > -45) {
            compassDirection.setText("北偏西");
            compassDegree.setText(" " + Math.abs(rotateDegree) + "°");
        }
    }

    //改变经度纬度
    protected void showLocation(AMapLocation location) {
        if (location != null) {
            double LONG = keepTwoDecimal(location.getLongitude());
            double LAT = keepTwoDecimal(location.getLatitude());
            if (LONG > 0) {
                longitudeText.setText("东经" + " " + LONG + "°");
            }
            if (LONG < 0) {
                longitudeText.setText("西经" + " " + Math.abs(LONG) + "°");
            }
            if (LAT > 0) {
                latitudeText.setText(" 北纬" + " " + LAT + "°");
            }
            if (LAT < 0) {
                latitudeText.setText(" 南纬" + " " + Math.abs(LAT) + "°");
            }
        }
    }

    //保留两位小数
    private double keepTwoDecimal(double number){
        return ((int)(number*100))/100.00;
    }
}
