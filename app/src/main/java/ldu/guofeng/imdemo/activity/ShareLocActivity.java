package ldu.guofeng.imdemo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapView;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.MyLocationListener;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

/**
 * 分享位置
 */
public class ShareLocActivity extends CustomReturnToolbar {

    public LocationClient mLocationClient = null;
    //LocationClient类是定位SDK的核心类
    public BDLocationListener myListener = new MyLocationListener();
    //BDLocationListener为结果监听接口，异步获取定位结果
    private MapView mMapView = null;//地图

    private MapMsgReceiver mapReceiver = null;//异步定位，广播接收
    private Button send_loc;//发送位置
    private TextView tv_address;//显示定位地址
    private boolean isOk = false;//定位成功
    private String form = null;//我
    private String to = null;//对方
    private String locInfo = null;//位置消息串

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_share_loc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initReceiver();
        initLocation();
    }

    private void init() {
        form = PreferencesUtils.getInstance().getString("username");
        //接收数据
        Bundle bundle = this.getIntent().getExtras();
        to = bundle.getString("to_user");
        isOk = false;
        send_loc = (Button) findViewById(R.id.send_loc);
        send_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOk) {
                    sendLocation();
                    finish();
                }
            }
        });
        tv_address = (TextView) findViewById(R.id.tv_address);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数
    }

    /**
     * 点击发送位置消息串
     */
    private void sendLocation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String message = form + Constant.SPLIT + to + Constant.SPLIT
                        + Constant.MSG_TYPE_LOC + Constant.SPLIT
                        + locInfo;
                SmackUtils.getInstance().sendMessage(message, to);
            }
        }).start();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter("GET_LOCATION");
        mapReceiver = new MapMsgReceiver();
        registerReceiver(mapReceiver, filter);
    }

    /**
     * 定位信息广播接收器
     */
    public class MapMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mLocationClient.stop();//收到成功或者失败都停止
            //接收定位的部分信息
            locInfo = String.valueOf(intent.getDoubleExtra("lat", 0)) + "@"
                    + String.valueOf(intent.getDoubleExtra("lon", 0)) + "@"
                    + intent.getStringExtra("describe") + "@"
                    + intent.getStringExtra("address");
            if (intent.getStringExtra("type").equals("OK")) {
                isOk = true;
                send_loc.setText("定位成功，点击发送");
                tv_address.setText(intent.getStringExtra("address"));
            } else {
                isOk = false;
                send_loc.setText("定位失败");
                tv_address.setText(intent.getStringExtra("type"));
            }
        }
    }

    /**
     * 配置定位SDK
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //LocationClientOption类，该类用来设置定位SDK的定位方式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置定位模式，默认高精度。高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("位置分享");
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mapReceiver);
        mMapView.onDestroy();
    }
}
