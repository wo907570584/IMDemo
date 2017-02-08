package ldu.guofeng.imdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

/**
 * 位置详情页
 */
public class MapLocActivity extends CustomReturnToolbar {
    private MapView mMapView = null;//地图
    private BaiduMap mBaiduMap = null;
    private TextView tv_address;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_map_loc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String loc_info = intent.getStringExtra("loc_info");
        String[] arr = loc_info.split("@");
        double latitude = Double.parseDouble(arr[1]);
        double longitude = Double.parseDouble(arr[2]);
        String describe = arr[3];//描述
        String address = arr[4];//地址

        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_address.setText(address + describe);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        LatLng point = new LatLng(latitude, longitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 17);
        mBaiduMap.animateMapStatus(msu);

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_loc);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("查看位置详情");
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
