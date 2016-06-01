package com.example.jeff.compass;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jeffdeen on 2016/5/1.
 */
public class locationSensor {
    private LocationManager locationManager;
    private String provider;
    MainActivity mainActivity;
    List<String> providerList;
    public locationSensor(MainActivity mainActivity) {
        this.mainActivity= mainActivity;
        locationManager=(LocationManager)mainActivity.getSystemService(Context.LOCATION_SERVICE);
        providerList=locationManager.getProviders(true);
        getProvider();
        getLocation();
    }

    protected void getProvider(){
        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
        }else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;
        }else
        {
            Toast.makeText(mainActivity,"GPS未打开，无法获取当前位置",Toast.LENGTH_SHORT).show();
        }
    }

    protected void getLocation(){
        try{
            Location location=locationManager.getLastKnownLocation(provider);
            if(location!=null){
                mainActivity.showLocation(location);
            }
            locationManager.requestLocationUpdates(provider,1000,5,locationListener);
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mainActivity.showLocation(location);
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
    protected void onStop(){
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
