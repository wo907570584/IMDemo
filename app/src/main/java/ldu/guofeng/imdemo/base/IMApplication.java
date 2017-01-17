package ldu.guofeng.imdemo.base;

import android.app.Application;
import android.content.Context;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Application
 *
 * @author GUOFENG
 */
public class IMApplication extends Application {

    private static Context myAppContext = null;
    public static XMPPTCPConnection connection = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppContext = getApplicationContext();
    }

    public static Context getMyAppContext() {
        return myAppContext;
    }

}
