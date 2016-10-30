package com.example.jeff.compass;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffdeen on 2016/10/27.
 */

public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
