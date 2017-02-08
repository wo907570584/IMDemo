package ldu.guofeng.imdemo.util;

import android.app.Activity;

import java.util.HashMap;

/**
 * 活动管理
 * Created by GUOFENG on 2017/1/16.
 */

public class ActivityCollector {

    private static HashMap<String, Activity> activities = new HashMap<String, Activity>();

    public static void addActivity(String name, Activity activity) {
        if (!activities.containsKey(name)) {
            activities.put(name, activity);
        }
    }

    public static void removeActivity(String name) {
        if (activities.containsKey(name)) {
            activities.get(name).finish();
        }
    }

}
