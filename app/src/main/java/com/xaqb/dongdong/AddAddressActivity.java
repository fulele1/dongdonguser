package com.xaqb.dongdong;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


public class AddAddressActivity extends BaseActivity {
    private AddAddressActivity instance;
    private EditText mEtName;
    private EditText mEtPhone;
    private EditText mEtProvince;
    private EditText mEtHome;
    private Button mBtFinished;
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private String mName;
    private String mPhone;


    @Override
    public void initTitle() {
        setTitleB("新增地址");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_address);
        instance = this;
        mEtName = (EditText) findViewById(R.id.et_name_add_add);
        mEtPhone = (EditText) findViewById(R.id.et_phone_add_add);
        mEtProvince = (EditText) findViewById(R.id.et_city_add_add);
        mEtHome = (EditText) findViewById(R.id.et_home_add_add);
        mBtFinished = (Button) findViewById(R.id.bt_finished_add_add);
    }

    @Override
    public void initAvailable() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void addEvent() {
        mBtFinished.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_finished_add_add:
                addAddress();
                break;
        }
    }

    /**
     * 增加地址
     */
    public void addAddress(){
        mName = mEtName.getText().toString().trim();
        mPhone = mEtPhone.getText().toString().trim();
        mProvince = mEtProvince.getText().toString().trim();
        mCity = mEtHome.getText().toString().trim();
        mDistrict = mEtHome.getText().toString().trim();

        OkHttpUtils
                .post()
                .url(HttpUrlUtils.getHttpUrl().addAddress())
                .addParams("access_token", SPUtil.get(instance,"access_token","").toString())
                .addParams("province",mProvince)//省
                .addParams("city",mCity)//市
                .addParams("district",mDistrict)//区
                .addParams("uid",SPUtil.get(instance,"userid","").toString())//用户ID
                .addParams("name",mName)//姓名
                .addParams("mp",mPhone)//电话
                .addParams("area","")//行政区划
                .addParams("postcode","")//邮政编码
                .addParams("address","")//详细门牌号
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                LogUtil.i("失败");
            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.i("成功"+s);
                showToastB("添加成功");
                finish();
            }
        });
    }

}
