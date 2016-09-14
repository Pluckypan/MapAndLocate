package com.fanglin.fenhong.mapandlocate.baiduloc;

import android.location.Location;

import com.google.gson.Gson;

/**
 * Created by Plucky on 2015/8/14.
 * Desc:
 */
public class FHLocation {

    public String id;
    public static final double NEARBY = 500;//500米范围内认为是附近
    public String member_id;
    public String country;
    public String province;
    public String city;
    public String street;
    public Double latitude;
    public Double longitude;
    public String address;
    public boolean isSuccess;
    public int LocType;
    public float Radius;
    public long pos_time;//定位时间
    public String district;//行政区域


    /**
     * 判断另一个地点是不是就在附近
     */
    public boolean isNearby(FHLocation fhLocation) {
        if (fhLocation != null) {
            float[] res = new float[1];
            Location.distanceBetween(latitude, longitude, fhLocation.latitude, fhLocation.longitude, res);
            return res[0] < NEARBY;
        } else {
            return false;
        }

    }


    public String getAreaInfo() {
        return province + " " + city + " " + getDistrict();
    }

    public String getAddrDesc() {
        return city + " " + getDistrict();
    }

    public String getDistrict() {
        return district == null ? "" : district;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
