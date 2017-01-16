package ldu.guofeng.imdemo.base;

import android.app.Application;
import android.content.Context;

/**
 * Application
 *
 * @author GUOFENG
 */
public class IMApplication extends Application {

    private static Context myAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppContext = getApplicationContext();
    }

    public static Context getMyAppContext() {
        return myAppContext;
    }

}
