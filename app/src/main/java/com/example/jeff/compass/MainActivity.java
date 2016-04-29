package com.example.jeff.compass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.List;


public class MainActivity extends Activity {

    private final float MAX_ROTATE_DEGREE = 1.0f;// 最多旋转一周，即360°
    private float mDirection;// 当前浮点方向
    private float mTargetDirection;// 目标浮点方向
    private AccelerateInterpolator mInterpolator;// 动画从开始到结束，变化率是一个加速的过程,就是一个动画速率
    protected final Handler mHandler = new Handler();
    protected final Handler mHandler2 = new Handler();
    private boolean mStopDrawing;// 是否停止指南针旋转的标志位
    CompassView mPointer;// 指南针view
    private SensorManager sensorManager;
    private Button aboutbutton;
    private double degree;
    private int rotateDegree;
    private TextView compassDirection;
    private TextView compassDegree;
    private LocationManager locationManager;
    private String provider;
    private TextView latitudeText;
    private TextView longitudeText;
    private ToggleButton lightButton;
    float distance;
    LevelView show;
    // 获取与Y轴的夹角
    float yAngle;
    float toyAngle;
    // 获取与Z轴的夹角
    float zAngle=0f;
    float tozAngle=0f;
    // 定义水平仪能处理的最大倾斜角，超过该角度，气泡将直接在位于边界。
    int MAX_ANGLE = 90;
    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
           // Log.d("direction",mDirection+"");
            if (mPointer != null && !mStopDrawing) {
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
                                    : 0.2f));// 用了一个加速动画去旋转图片，很细致
                    mPointer.updateDirection(mDirection);// 更新指南针旋转


                }

                if (show != null) {
                    toyAngle=toyAngle+((yAngle - toyAngle) * mInterpolator.getInterpolation(0.2f));// 用了一个加速动画去旋转图片，很细致
                    tozAngle=tozAngle+((zAngle - tozAngle) * mInterpolator.getInterpolation(0.2f));// 用了一个加速动画去旋转图片，很细致
                    //Log.d("distance",distance+"");
                    updateLevelView();
                    }
                toyAngle=yAngle;
                tozAngle=zAngle;

                mHandler.postDelayed(mCompassViewUpdater,20);// 20毫秒后重新执行自己，比定时器好
            }
        }
    };
    protected Runnable directionUpdater = new Runnable() {
        @Override
        public void run() {
            updateDirection();// 更新方向值
            mHandler2.postDelayed(directionUpdater,150);// 20毫秒后重新执行自己，比定时器好
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor magneticSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
       //mStopDrawing = true;
        //Log.d("direction", 0 + "");
        compassDegree=(TextView)findViewById(R.id.compassDegree);
        compassDirection=(TextView)findViewById(R.id.compassDirection);
        show = (LevelView) findViewById(R.id.show);
       // mHandler.post(mCompassViewUpdater);
        mPointer = (CompassView) findViewById(R.id.degree);
        mPointer.setRatio(1.0f);
        ButtonListener buttonListener=new ButtonListener();
        aboutbutton=(Button)findViewById(R.id.button);
        aboutbutton.setOnClickListener(buttonListener);
        longitudeText=(TextView)findViewById(R.id.longitudeText);
        latitudeText=(TextView)findViewById(R.id.latitudeText);
        lightButton=(ToggleButton)findViewById(R.id.lightButton);
        lightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lightButton.setBackgroundResource(R.drawable.light_on);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Toast.makeText(getApplicationContext(),"您开启了屏幕常亮",Toast.LENGTH_SHORT).show();
                }
                if(!isChecked){
                    lightButton.setBackgroundResource(R.drawable.light_off);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Toast.makeText(getApplicationContext(),"您关闭了屏幕常亮",Toast.LENGTH_SHORT).show();
                }
            }
        });
        initial();
    }

    class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            switch (view.getId()){
                //case R.id.lightButton:

                   // break;
                case R.id.button:
                    Intent intent=new Intent(MainActivity.this,AboutActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.anim, R.animator.anim2);
                    break;
                default:
                    break;
            }
        }
    }

    private SensorEventListener listener=new SensorEventListener() {
        float[] accelerometerValues=new float[3];

        float[] magneticValues=new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            //判断当前是加速度传感器还是地磁传感器
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                //赋值时要调用clone方法
                accelerometerValues=event.values.clone();
            }else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                magneticValues=event.values.clone();
            }
            float[] values=new float[3];
            float[] R=new float[9];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
            SensorManager.getOrientation(R, values);
            degree = Math.toDegrees(values[0]);// 赋值给全局变量，让指南针旋转
            mTargetDirection=(float)degree*-1.0f;
            yAngle=(int)Math.toDegrees(values[1]);
            zAngle=(int)Math.toDegrees(values[2]);
            //Log.d("direction",zAngle+"");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(sensorManager!=null){
            sensorManager.unregisterListener(listener);
        }
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mStopDrawing=false;
        mHandler.postDelayed(mCompassViewUpdater, 20);
        mHandler2.postDelayed(directionUpdater, 150);
    }
    @Override
    protected void onStop(){
        super.onStop();
        mStopDrawing=true;
    }

    private void updateDirection(){
        if(Math.abs(distance)<1){
            return;
        }
        rotateDegree=(int)degree;
        //Log.d("nan",""+rotateDegree);
        if(rotateDegree==0){
            compassDirection.setText("正北");
            compassDegree.setText(" ");
        }
        else if(rotateDegree>0&&rotateDegree<=45){
            compassDirection.setText("北偏东");
            compassDegree.setText(" "+rotateDegree+"°");
        }
        else if(rotateDegree>45&&rotateDegree<90){
            compassDirection.setText("东偏北");
            compassDegree.setText(" "+Math.abs(rotateDegree-90)+"°");
        }
        else if(rotateDegree==90){
            compassDirection.setText("正东");
            compassDegree.setText(" ");
        }
        else if(rotateDegree>90&&rotateDegree<=135){
            compassDirection.setText("东偏南");
            compassDegree.setText(" "+(rotateDegree-90)+"°");
        }
        else if(rotateDegree>135&&rotateDegree<179){
            compassDirection.setText("南偏东");
            compassDegree.setText(" "+Math.abs(rotateDegree - 179)+"°");
        }
        else if(rotateDegree==179||rotateDegree==-179){
            compassDirection.setText("正南");
            compassDegree.setText(" ");
        }
        else if(rotateDegree<-135&&rotateDegree>-179){
            compassDirection.setText("南偏西");
            compassDegree.setText(" "+Math.abs(rotateDegree+179)+"°");
        }
        else if(rotateDegree>=-135&&rotateDegree<-90){
            compassDirection.setText("西偏南");
            compassDegree.setText(" "+Math.abs(rotateDegree+90)+"°");
        }
        else if(rotateDegree==-90){
            compassDirection.setText("正西");
            compassDegree.setText(" ");
        }
        else if(rotateDegree>-90&&rotateDegree<=-45){
            compassDirection.setText("西偏北");
            compassDegree.setText(" "+Math.abs(rotateDegree+90)+"°");
        }
        else if(rotateDegree<0&&rotateDegree>-45){
            compassDirection.setText("北偏西");
            compassDegree.setText(" "+Math.abs(rotateDegree)+"°");
        }
    }
    private void initial()
    {

        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList=locationManager.getProviders(true);
            if(providerList.contains(LocationManager.GPS_PROVIDER)){
                provider=LocationManager.GPS_PROVIDER;
            }else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
                provider=LocationManager.NETWORK_PROVIDER;
            }else
            {
                Toast.makeText(getApplicationContext(),"GPS未打开，无法获取当前位置",Toast.LENGTH_SHORT).show();
            }
        try{
            Location location=locationManager.getLastKnownLocation(provider);
            if(location!=null){
                showLocation(location);
            }
            locationManager.requestLocationUpdates(provider,1000,5,locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
            //Log.d("ddd",""+changenumber(location.getLongitude()));
            //Log.d("ddd", "" + changenumber(location.getLatitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //改变经度纬度
    private void showLocation(Location location){
        if(location!=null){
            double LONG=changenumber(location.getLongitude());
            double LAT=changenumber(location.getLatitude());
            if(LONG>0){
                longitudeText.setText("东经"+" "+LONG+"°");
            }
            if(LONG<0){
                longitudeText.setText("西经"+" "+Math.abs(LONG)+"°");
            }
            if(LAT>0){
                latitudeText.setText(" 北纬"+" "+LAT+"°");
            }
            if(LAT<0){
                latitudeText.setText(" 南纬"+" "+Math.abs(LAT)+"°");
            }
        }
    }

    private double changenumber(double number){
        return ((int)(number*100))/100.00;
    }

    private void updateLevelView(){

        // 气泡位于中间时（水平仪完全水平），气泡的X、Y座标
        int x = (show.length- show.bubble.getWidth()) / 2;
        int y = (show.length- show.bubble.getHeight()) / 2;
        // 如果与Z轴的倾斜角还在最大角度之内
        if (Math.abs(zAngle) <= MAX_ANGLE) {
            // 根据与Z轴的倾斜角度计算X座标的变化值（倾斜角度越大，X座标变化越大）
            int deltaX = (int) ((show.length- show.bubble
                    .getWidth()) / 2 * zAngle / MAX_ANGLE);
            x -= deltaX;
        }
        // 如果与Z轴的倾斜角已经大于MAX_ANGLE，气泡应到最左边
        else if (zAngle > MAX_ANGLE) {
            x = show.length - show.bubble.getWidth();
        }
        // 如果与Z轴的倾斜角已经小于负的MAX_ANGLE，气泡应到最右边
        else {
            x = 0;
        }
        // 如果与Y轴的倾斜角还在最大角度之内
        if (Math.abs(yAngle) <= MAX_ANGLE) {
            // 根据与Y轴的倾斜角度计算Y座标的变化值（倾斜角度越大，Y座标变化越大）
            int deltaY = (int) ((show.length- show.bubble
                    .getHeight()) / 2 * yAngle / MAX_ANGLE);
            y += deltaY;
        }
        // 如果与Y轴的倾斜角已经大于MAX_ANGLE，气泡应到最下边
        else if (yAngle > MAX_ANGLE) {
            y = show.length - show.bubble.getHeight();
        }
        // 如果与Y轴的倾斜角已经小于负的MAX_ANGLE，气泡应到最右边
        else {
            y = 0;
        }
        // 如果计算出来的X、Y座标还位于水平仪的仪表盘内，更新水平仪的气泡座标
        if (isContain(x, y)) {
            show.bubbleX = x;
            show.bubbleY = y;
        }
        // 通知系统重回MyView组件
        show.postInvalidate();
    }

    // 计算x、y点的气泡是否处于水平仪的仪表盘内
    private boolean isContain(int x, int y) {
        // 计算气泡的圆心座标X、Y
        int bubbleCx = x + show.bubble.getWidth() / 2;
        int bubbleCy = y + show.bubble.getWidth() / 2;
        // 计算水平仪仪表盘的圆心座标X、Y
        int backCx = show.length/ 2;
        int backCy = show.length/ 2;
        // 计算气泡的圆心与水平仪仪表盘的圆心之间的距离。
        double distance = Math.sqrt((bubbleCx - backCx) * (bubbleCx - backCx)
                + (bubbleCy - backCy) * (bubbleCy - backCy));
        // 若两个圆心的距离小于它们的半径差，即可认为处于该点的气泡依然位于仪表盘内
        if (distance < (show.length - show.bubble.getWidth()) / 2) {
            return true;
        } else {
            return false;
        }
    }
}
