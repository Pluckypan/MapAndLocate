package engineer.echo.maplocdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanglin.fenhong.mapandlocate.baiduloc.BaiduLocateUtil;
import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.mapandlocate.baiduloc.LocMsg;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLoc, btnShow;
    TextView tvMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMemo = (TextView) findViewById(R.id.tvMemo);
        btnLoc = (Button) findViewById(R.id.btnLoc);
        btnShow = (Button) findViewById(R.id.btnShow);

        btnLoc.setOnClickListener(this);
        btnShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoc:
                handleLocation();
                break;
            case R.id.btnShow:
                showMap();
                break;
        }
    }

    private void handleLocation() {
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
    }

    private void showMap() {
        LocMsg lmsg = new LocMsg();
        lmsg.mLat = 36.081600;
        lmsg.mLng = 120.422760;
        lmsg.mPoi = "青岛大学宁夏路231";

        BaiduLocateUtil.getinstance(getApplicationContext()).ShowMapLocation(lmsg);
    }
}
