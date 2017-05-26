package com.xaqb.dongdong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xaqb.dongdong.zxing.activity.CaptureActivity;

public class QueryActivity extends BaseActivity {

    private EditText mEtCode;
    private ImageView mIvQR;
    private Spinner mSpChoose;
    private Button mBtQuery;
    private QueryActivity instance;
    private String mCode;


    @Override
    public void initTitle() {

    }
    @Override
    public void initView() {
        setContentView(R.layout.activity_query);
        instance = this;
        mEtCode = (EditText) findViewById(R.id.et_code_query);
        mIvQR = (ImageView) findViewById(R.id.iv_qr_query);
        mSpChoose = (Spinner) findViewById(R.id.tv_choose_query);
        mBtQuery = (Button) findViewById(R.id.bt_query_query);
    }

    @Override
    public void initAvailable() {
    }

    @Override
    public void initData() {
    }

    private String mBrand;
    @Override
    public void addEvent() {
        mIvQR.setOnClickListener(instance);
        mBtQuery.setOnClickListener(instance);
        mSpChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                mBrand = tv.getText().toString();
                if (mBrand.equals("请选择")){
                    mBrand = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_qr_query://扫描
                qr();
                break;
            case R.id.bt_query_query://查询
                query();
                break;
        }
    }


    public void qr(){
        mCode = mEtCode.getText().toString().trim();
        if ("".equals(mCode)||null==mCode){
            //跳转到扫描条形码界面
            intentB(instance,CaptureActivity.class,5);
        }
    }


    public void query(){
        String code = mEtCode.getText().toString().trim();
        if (code.equals("")){
            showToastB("请输入订单号");
            return;
        }else if (mBrand.equals("")){
            showToastB("请选择快递公司");
            return;
        }
        Intent intent = new Intent(instance,WebviewActivity.class);
        intent.putExtra("url","https://m.kuaidi100.com/index_all.html?type=" + mBrand + "&postid=" + code);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5&& resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                String code = bundle.getString("result");
                mEtCode.setText(code);
            }
        }
    }
}
