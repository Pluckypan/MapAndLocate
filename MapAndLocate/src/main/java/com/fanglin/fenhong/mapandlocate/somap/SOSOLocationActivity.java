package com.fanglin.fenhong.mapandlocate.somap;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fanglin.fenhong.mapandlocate.R;
import com.fanglin.fenhong.mapandlocate.baiduloc.BaiduLocateUtil;
import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.mapandlocate.baiduloc.LocMsg;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;


/**
 * 搜狗定位地图显示经纬度
 * Created by Plucky on 14/11/21.
 */

@SuppressLint("ClickableViewAccessibility")
public class SOSOLocationActivity extends MapActivity {

    MapView mMapView;
    Button mButton = null;
    LocMsg mMsg;

    private Marker marker;
    private String SOMAPKEY = "";

    @Override
    /**
     *显示地图，启用内置缩放控件，并用MapController控制地图的中心点及Zoom级别
     */ protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soso_map);

        if (getIntent().hasExtra("location")) {
            mMsg = getIntent().getParcelableExtra("location");
        }


        initView();


        //获取当前定位
        BaiduLocateUtil.getinstance(getApplicationContext()).setCallBack(new BaiduLocateUtil.LocationCallBack() {
            @Override
            public void onChange(final FHLocation location) {
                if (!location.isSuccess) {

                    //腾讯 36.0711121570,120.4170806958
                    //百度 36.0767740000,120.4236900000
                    location.latitude = 36.0711121570;
                    location.longitude = 120.4170806958;
                    location.address = "青岛大学";
                }
                mMsg = new LocMsg();
                mMsg.mLat = location.latitude;
                mMsg.mLng = location.longitude;
                mMsg.mPoi = location.address;
                mMsg.mImgUri = getImgUri(location.latitude, location.longitude);

                BaiduLocateUtil.getinstance(getApplicationContext()).stop();

                LatLng ll = new LatLng(location.latitude, location.longitude);
                mMapView.getMap().setCenter(ll);

                mMapView.getMap().clearAllOverlays();
                MarkerOptions mo = new MarkerOptions();
                mo.draggable(true);
                mo.title(location.address);
                mo.position(ll);


                marker = mMapView.getMap().addMarker(mo);
                marker.showInfoWindow();
                mMapView.getMap().setZoom(16);
            }

            @Override
            public void onFailure() {

            }
        });


        if (mMsg == null) {
            mButton.setOnClickListener(l);
            mMapView.getMap().setOnMarkerDraggedListener(mdl);
            BaiduLocateUtil.getinstance(getApplicationContext()).start();
        } else {
            mButton.setVisibility(View.GONE);
            LatLng ll = new LatLng(mMsg.mLat, mMsg.mLng);
            mMapView.getMap().setCenter(ll);
            mMapView.getMap().clearAllOverlays();
            MarkerOptions mo = new MarkerOptions();
            mo.draggable(false);
            mo.title(mMsg.mPoi);
            mo.position(ll);


            marker = mMapView.getMap().addMarker(mo);
            marker.showInfoWindow();
            mMapView.getMap().setZoom(16);
        }

    }


    private void initView() {
        mMapView = (MapView) findViewById(android.R.id.widget_frame);
        mButton = (Button) this.findViewById(android.R.id.button1);
        SOMAPKEY = BaiduLocateUtil.a(getApplicationContext());
    }


    private TencentMap.OnMarkerDraggedListener mdl = new TencentMap.OnMarkerDraggedListener() {
        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(final Marker marker) {
            BaiduLocateUtil.getinstance(getApplicationContext()).getAddrByLatLng(marker.getPosition(), new BaiduLocateUtil.AddrCallBack() {
                @Override
                public void onStart() {
                    mMapView.getMap().setCenter(marker.getPosition());
                    marker.setTitle("");
                    marker.hideInfoWindow();
                    mMsg = new LocMsg();
                    mMsg.mLat = marker.getPosition().getLatitude();
                    mMsg.mLng = marker.getPosition().getLongitude();
                    mMsg.mPoi = marker.getTitle();
                    mMsg.mImgUri = getImgUri(marker.getPosition().getLatitude(), marker.getPosition().getLongitude());

                }

                @Override
                public void onEnd(String res) {
                    if (res != null) {
                        marker.setTitle(res);
                        mMsg.mPoi = marker.getTitle();
                        marker.showInfoWindow();
                    }
                }
            });
        }

        @Override
        public void onMarkerDragStart(Marker marker) {

        }
    };

    private OnClickListener l = new OnClickListener() {
        @Override
        public void onClick(View v) {
            BaiduLocateUtil.MapLocCallBack cb = BaiduLocateUtil.getinstance(getApplicationContext()).getMapCallBack();
            if (mMsg != null) {
                if (cb != null) {
                    cb.onEnd(mMsg);
                }
                finish();
            } else {
                if (cb != null) {
                    cb.onEnd(null);
                }

            }
        }
    };


    private Uri getImgUri(double mlat, double mlng) {
        return Uri.parse("http://apis.map.qq.com/ws/staticmap/v2").buildUpon().appendQueryParameter("size", "240*240").appendQueryParameter("key", SOMAPKEY).appendQueryParameter("zoom", "16").appendQueryParameter("center", mlat + "," + mlng).build();
    }

}
