package com.xaqb.dongdong;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xaqb.dongdong.entity.Address;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditAddressActivity extends BaseActivity {

    private EditAddressActivity instance;
    private Address mAddress;
    private EditText mEtName;
    private EditText mEtPhone;
    private EditText mEtCity;
    private EditText mEtHome;
    private Button mBtFinished;

    @Override
    public void initTitle() {
        setTitleB("编辑收货地址");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_edit_address);
        instance = this;
        mEtName = (EditText) findViewById(R.id.et_name_edit_add);
        mEtPhone = (EditText) findViewById(R.id.et_phone_edit_add);
        mEtCity = (EditText) findViewById(R.id.et_city_edit_add);
        mEtHome = (EditText) findViewById(R.id.et_home_edit_add);
        mBtFinished = (Button) findViewById(R.id.bt_finished_edit_add);
    }
    @Override
    public void initAvailable() {
        Intent intent = getIntent();
        mAddress = (Address) intent.getSerializableExtra("add");
        LogUtil.i(mAddress.getPhone()+mAddress.getAddr()+mAddress.getProvince()+mAddress.getUser());
    }

    @Override
    public void initData() {
        mEtName.setText(mAddress.getUser());
        mEtPhone.setText(mAddress.getPhone());
        mEtCity.setText(mAddress.getProvince());
        mEtHome.setText(mAddress.getAddr());

    }

    @Override
    public void addEvent() {
        mBtFinished.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_finished_edit_add:
                changeAddress();
                break;
        }
    }

    /**
     * 修改地址
     */
    public void changeAddress(){
        Map<String, String> map = new HashMap<>();
        map.put("access_token", SPUtil.get(instance,"access_token","").toString());
        map.put("name", mEtName.getText().toString().trim());
        map.put("mp", mEtPhone.getText().toString().trim());
        map.put("province", mEtCity.getText().toString().trim());
        map.put("city", mEtHome.getText().toString().trim());
        map.put("district", "");
        map.put("address", "");
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtil.GsonString(map));

        OkHttpUtils
                .put()
                .url(HttpUrlUtils.getHttpUrl().editAddress()+ mAddress.getId()+".json?")
                .requestBody(body)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        showToastB("请求失败");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Map<String, Object> map = GsonUtil.GsonToMaps(s);
                        if (map.get("state").toString().equals("1.0")) {
                            showToastB(map.get("mess").toString());
                            return;
                        }
                        showToastB("修改成功");
                        finish();
                    }
                });
    }
}