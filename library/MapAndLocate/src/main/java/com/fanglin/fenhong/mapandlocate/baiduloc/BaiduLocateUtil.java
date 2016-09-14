package com.fanglin.fenhong.mapandlocate.baiduloc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fanglin.fenhong.mapandlocate.somap.SOSOLocationActivity;
import com.tencent.mapsdk.raster.model.LatLng;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaiduLocateUtil {

    private Context mContext;
    private static BaiduLocateUtil minstance;
    private LocationClient mLocationClient;

    LocationClientOption.LocationMode locateMode = LocationClientOption.LocationMode.Hight_Accuracy;// 设置定位模式--高精度
    String locateCoor = "gcj02";//
    boolean showGeolocation = true;// 返回详细描述地址

    private BaiduLocateUtil(Context c) {
        this.mContext = c;
        mLocationClient = new LocationClient(mContext);     //声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(locateMode);
        option.setCoorType(locateCoor);
        option.setIsNeedAddress(showGeolocation);
        option.setOpenGps(true);
        option.setTimeOut(500);
        option.setEnableSimulateGps(false);
        option.setScanSpan(3000);
        option.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(option);

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                    Log.d("Plucky", "msg:" + location.getLocTypeDescription());
                    FHLocation fl = new FHLocation();
                    fl.country = location.getCountry() != null ? location.getCountry() : "中国";
                    fl.province = location.getProvince() != null ? location.getProvince() : "山东";
                    fl.street = location.getStreet();
                    fl.city = location.getCity() != null ? location.getCity() : "青岛";
                    fl.LocType = location.getLocType();
                    fl.latitude = location.getLatitude();
                    fl.longitude = location.getLongitude();
                    fl.district = location.getDistrict();
                    fl.Radius = location.getRadius();
                    fl.pos_time = System.currentTimeMillis() / 1000;
                    if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                        fl.isSuccess = true;
                        fl.address = location.getLocationDescribe();
                    } else {
                        fl.isSuccess = false;
                    }

                    if (mcb != null) {
                        mcb.onChange(fl);
                    }
                } else {
                    if (mcb != null) {
                        mcb.onFailure();
                    }
                }
            }
        });
    }

    public void start() {
        mLocationClient.start();
        if (mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }

    public void stop() {
        mLocationClient.stop();
    }

    private BaiduLocateUtil() {

    }

    public static BaiduLocateUtil getinstance(Context c) {
        if (minstance == null) {
            minstance = new BaiduLocateUtil(c);
        }
        Log.d("BaiduMap", minstance.mLocationClient.getAccessKey());
        return minstance;
    }


    private LocationCallBack mcb;

    public void setCallBack(LocationCallBack cb) {
        this.mcb = cb;
    }

    public interface LocationCallBack {
        void onChange(FHLocation location);

        void onFailure();
    }

    /** 以上代码均为百度定位相关--下面的代码是对于地图的操作*/

    /**
     * 显示某一位置在地图的显示
     */
    public void ShowMapLocation(LocMsg msg) {
        Intent intent = new Intent(mContext, SOSOLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("location", msg);
        mContext.startActivity(intent);
    }

    public interface MapLocCallBack {
        void onEnd(LocMsg msg);
    }

    private MapLocCallBack mapcb;

    public MapLocCallBack getMapCallBack() {
        return mapcb;
    }

    /**
     * 获取地图某一点的位置
     */
    public void getMapLocation(MapLocCallBack cb) {
        mapcb = cb;
        Intent intent = new Intent(mContext, SOSOLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);//SOSO地图
    }


    public static String a(Context var0) {
        String var1 = var0.getPackageName();
        String var3;

        try {
            ApplicationInfo var2 = var0.getPackageManager().getApplicationInfo(var1, PackageManager.GET_META_DATA);
            var3 = var2.metaData.getString("TencentMapSDK");
            if (!TextUtils.isEmpty(var3)) {
                String var4 = "[&=]";
                Pattern var5 = Pattern.compile(var4);
                Matcher var6 = var5.matcher(var3);
                var3 = var6.replaceAll("");
            }
        } catch (PackageManager.NameNotFoundException var7) {
            var3 = "GRYBZ-ACPWV-UIGPO-U2SRZ-KRYB6-7VFTU";
        }

        return var3;
    }

    public void getAddrByLatLng(LatLng ll, final AddrCallBack cb) {
        String url = "http://apis.map.qq.com/ws/geocoder/v1/?location=" + ll.getLatitude() + "," + ll.getLongitude() + "&key=" + a(mContext) + "&get_poi=0";
        new GETUtil().setCallBack(new GETUtil.GETCallBack() {
            @Override
            public void onStart() {
                if (cb != null) cb.onStart();
            }

            @Override
            public void onEnd(boolean isSuccess, String res) {
                if (isSuccess) {
                    try {
                        JSONObject json = new JSONObject(res);
                        String addr = json.getJSONObject("result").getJSONObject("formatted_addresses").getString("recommend");
                        if (cb != null) cb.onEnd(addr);
                    } catch (Exception e) {
                        if (cb != null) cb.onEnd(null);
                    }
                } else {
                    if (cb != null) cb.onEnd(null);
                }
            }
        }).get(url);
    }

    public interface AddrCallBack {
        void onStart();

        void onEnd(String res);
    }

}
