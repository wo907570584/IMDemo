package ldu.guofeng.imdemo.util;

import android.content.Intent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import ldu.guofeng.imdemo.base.IMApplication;

public class MyLocationListener implements BDLocationListener {
    private String type = null;
    private double lat;
    private double lon;
    private String describe = null;
    private String address = null;

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location.getLocType() == BDLocation.TypeServerError) {
            type = "服务端网络定位失败";
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            type = "请检查网络是否通畅";
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            type = "请检查设备是否处于飞行模式";
        } else {
            type = "OK";
            lat = location.getLatitude();
            lon = location.getLongitude();
            describe = location.getLocationDescribe();// 位置语义化信息
            address = location.getAddrStr();
        }
        Intent intent = new Intent();
        intent.setAction("GET_LOCATION");
        intent.putExtra("type", type);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("describe", describe);
        intent.putExtra("address", address);
        IMApplication.getMyAppContext().sendBroadcast(intent);
    }
}
