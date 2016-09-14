# MapAndLocate
几行代码实现定位、以及根据经纬度在地图上具体位置。定位采用的是百度定位、地图显示采用的是搜搜地图。为什么这么做，因为结合着两者：定位准确，体积最小。

## 截图


## useage
### 第一步：在AndroidManifest.xml文件中分别填写 在 [百度定位开放平台申请的API_KEY](http://lbsyun.baidu.com/apiconsole/key) 以及在[腾讯搜搜地图开放平台](http://lbs.qq.com/mykey.html)申请的KEY
```
<!-- 百度定位 -->
<meta-data
    android:name="com.baidu.lbsapi.API_KEY"
    android:value="5n57o4BRzjq1FZGK0PbIvk4HD3iIGy8Q" />

<!-- 腾讯地图 -->
<meta-data
    android:name="TencentMapSDK"
    android:value="FQYBZ-CKSRF-G2LJD-JW6HF-DPQKK-RSFCF" />
```

### 第二步 ：添加依赖 这里以gradle为例，在项目build.gradle文件中添加：
```
dependencies {
    compile 'com.fanglin.fenhong.mapandlocate:MapAndLocate:1.0.0'
}
```

### 第三步：调用
#### 定位
```
BaiduLocateUtil.getinstance(getApplicationContext()).start();
BaiduLocateUtil.getinstance(getApplicationContext()).setCallBack(new BaiduLocateUtil.LocationCallBack() {
    @Override
    public void onChange(FHLocation location) {
        if (location != null) {
            BaiduLocateUtil.getinstance(getApplicationContext()).stop();
            tvMemo.setText(location.toString());
        }
    }

    @Override
    public void onFailure() {

    }
});
```
#### 在地图上显示指定位置
```
LocMsg lmsg = new LocMsg();
lmsg.mLat = 36.081600;
lmsg.mLng = 120.422760;
lmsg.mPoi = "青岛大学宁夏路231";

BaiduLocateUtil.getinstance(getApplicationContext()).ShowMapLocation(lmsg);
```
