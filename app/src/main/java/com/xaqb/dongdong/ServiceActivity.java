package com.xaqb.dongdong;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ServiceActivity extends BaseActivity {

    @Override
    public void initTitle() {
        setTitleB("关于咚咚");
    }

    private TextView mTvVersion;
    private Button mBtCall;
    private ServiceActivity instance;

    @Override
    public void initView() {
        setContentView(R.layout.activity_service);
        instance = this;
        mTvVersion = (TextView) findViewById(R.id.tv_version_service);
        mBtCall = (Button) findViewById(R.id.bt_call_service);
        mTvVersion.setText("v" + getVersionName());
    }

    @Override
    public void initAvailable() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void addEvent() {
        mBtCall.setOnClickListener(instance);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_call_service:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:029-87888612"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    showToastB("请添加“CALL_PHONE”权限");
                    return;
                }
                startActivity(intent);
                break;
        }
    }
}