package com.example.jeff.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by jeffdeen on 2016/5/1.
 */
public class compassSensorClass implements SensorEventListener{
    SensorManager sm;
    MainActivity mainActivity;
    Sensor magneticSensor;
    Sensor accelerometerSensor;
    float[] accelerometerValues=new float[3];
    float[] magneticValues=new float[3];
    float[] values=new float[3];
    float[] returnValues=new float[3];
    public compassSensorClass(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        sm=(SensorManager)mainActivity.getSystemService(Context.SENSOR_SERVICE);
        magneticSensor=sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //判断当前是加速度传感器还是地磁传感器
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            //赋值时要调用clone方法
            accelerometerValues=event.values.clone();
        }else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            magneticValues=event.values.clone();
        }
        values=new float[3];
        float[] R=new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
        SensorManager.getOrientation(R, values);

    }

    protected float getvalues(int num){
        returnValues[0]=(float)Math.toDegrees(values[0]);
        returnValues[1]=(float)Math.toDegrees(values[1]);
        returnValues[2]=(float)Math.toDegrees(values[2]);
        return returnValues[num];
    }
    public void onstop()
    {
        if (this.sm != null)
            this.sm.unregisterListener(this);
    }

    public void resume()
    {
        if (this.sm != null) {
            sm.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sm.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
