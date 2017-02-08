package ldu.guofeng.imdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

/**
 * 定位、显示、发送
 */
public class ShareLocActivity extends CustomReturnToolbar {

    public LocationClient mLocationClient = null;
    //LocationClient类是定位SDK的核心类
    public BDLocationListener myListener = new MyLocationListener();
    //BDLocationListener为结果监听接口，异步获取定位结果
    private MapView mMapView = null;//地图
    private BaiduMap mBaiduMap = null;

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
                    Intent intent = new Intent();
                    intent.putExtra("my_location", locInfo);
                    setResult(200, intent);
                    sendLocation();
                    finish();
                }
            }
        });
        tv_address = (TextView) findViewById(R.id.tv_address);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
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

    /**
     * 异步获取定位结果
     */
    public class MyLocationListener implements BDLocationListener {
        private String type = null;
        private double radius;//精度
        private double latitude;//纬度
        private double longitude;//经度
        private String describe = null;//描述
        private String address = null;//地址

        @Override
        public void onReceiveLocation(BDLocation location) {
            mLocationClient.stop();//收到成功或者失败都停止
            if (location == null || mMapView == null || mBaiduMap == null)
                return;

            //获取定位信息
            if (location.getLocType() == BDLocation.TypeServerError) {
                type = "服务端网络定位失败";
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                type = "请检查网络是否通畅";
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                type = "请检查设备是否处于飞行模式";
            } else {
                type = "OK";
                radius = location.getRadius();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                describe = location.getLocationDescribe();// 位置语义化信息
                address = location.getAddrStr();
            }

            //成功与否更改提示信息
            if (type.equals("OK")) {
                isOk = true;
                send_loc.setText("定位成功，点击发送");
                tv_address.setText(address);
            } else {
                isOk = false;
                send_loc.setText("定位失败");
                tv_address.setText(type);
            }
            //接收定位的部分信息
            locInfo = radius + "@"
                    + String.valueOf(latitude) + "@"
                    + String.valueOf(longitude) + "@"
                    + describe + "@"
                    + address;

            //显示定位
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())//设置定位数据的精度信息
                    .direction(100)//设置定位数据的方向信息,顺时针0-360
                    .latitude(location.getLatitude())//设置定位数据的纬度
                    .longitude(location.getLongitude())//设置定位数据的经度
                    .build();
            mBaiduMap.setMyLocationData(locData);
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 17);
            mBaiduMap.animateMapStatus(msu);
        }
    }


    /**
     * 配置定位SDK
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //LocationClientOption类，该类用来设置定位SDK的定位方式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置定位模式，默认高精度。分为高精度，低功耗，仅设备
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
        //可选，默认false，设置是否需要位置语义化结果
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("分享位置");
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
        mMapView.onDestroy();
    }
}
