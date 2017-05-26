package com.xaqb.dongdong;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xaqb.dongdong.util.Base64;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;


public class ApproveActivity extends BaseActivity {

    private TextView mTvChoose;
    private ImageView mIvHold;
    private Button mBtPost;
    private EditText mEtName;
    private EditText mEtIde;
    private ApproveActivity instance;
    private Bitmap mBitmap;
    private String mName;
    private String mIde;
    private boolean mHavePic;
    private String mType;

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_approve);
        mTvChoose = (TextView) findViewById(R.id.tv_choose_pro);
        mIvHold = (ImageView) findViewById(R.id.iv_hold_apr);
        mBtPost = (Button) findViewById(R.id.bt_post_pro);
        mEtName = (EditText) findViewById(R.id.et_name_app);
        mEtIde = (EditText) findViewById(R.id.et_ide_app);
        instance = this;
    }

    @Override
    public void initAvailable() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void addEvent() {
        mTvChoose.setOnClickListener(instance);
        mIvHold.setOnClickListener(instance);
        mBtPost.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose_pro://选择证件类型
                intentB(instance,TypeActivity.class,1);
                break;
            case R.id.iv_hold_apr://持证照片
                showDialogB(instance,new String[]{"拍照","相册"});
                break;
            case R.id.bt_post_pro://上传认证
                getApproveData();

                OkHttpUtils
                        .post()
                        .url(HttpUrlUtils.getHttpUrl().realApply() + "access_token=" + SPUtil.get(instance, "access_token", ""))
                        .addParams("uid", SPUtil.get(instance, "userid", "").toString())
                        .addParams("type", "1")
                        .addParams("name", mName)
                        .addParams("certtype", mType)
                        .addParams("certcode", mIde)
                        .addParams("certimg", Base64.encode(mBitmap))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                mLoadingDialog.dismiss();
                                showToastB("网络访问异常");
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                mLoadingDialog.dismiss();
                                Map<String, Object> map = GsonUtil.GsonToMaps(s);
                                LogUtil.i(map.toString());
                                switch (map.get("state").toString()) {
                                    case "0.0":
                                        showToastB("上传成功，请等待审核！");
                                        finish();
                                        break;
                                    case "1.0":
                                        showToastB(map.get("mess").toString());
                                        break;
                                    case "102.0":
                                        showToastB("认证中，请等待审核！");
                                        break;
                                    case "101.0":
                                        showToastB("已经认证成功！");
                                        finish();
                                        break;
                                    default:
                                        showToastB("认证失败，请核对信息！");
                                        break;
                                }
                            }
                        });
                break;
        }
    }

    /**
     * 获取要进行上传认证的数据
     */
    public void getApproveData(){
        mName = mEtName.getText().toString().trim();
        mIde = mEtIde.getText().toString().trim();
        mType = getType(mTvChoose.getText().toString());
        if (mName.equals("")){
            showToastB("请输入姓名");
        }else if (mType.equals("")){
            showToastB("请选择证件类型");
        }else if(mIde.equals("")){
            showToastB("请输入证件号码");
        }else if (!mHavePic){
            showToastB("请拍摄照片");
        }else{
            mLoadingDialog.show("请稍后...");
        }
    }

    public String getType(String type){
        switch (type){
            case "身份证":
                return "11";
            case "驾照":
                return "94";
            case "警官证":
                return "91";
            case "户口本":
                return "13";
            case "士兵证":
                return "92";
            case "国内护照":
                return "93";
            case "军官证":
                return "90";
            case "其他":
                return "90";
        }
        return "";
    }

    @Override
    public void dialogItemEventB(int witch) {
        switch (witch){
            case 0://相机
                intentB(2);
                break;
            case 1://相册
                intentB(3,"");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1&& resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle !=null){//选择证件类型
                String type = bundle.getString("type");
                mTvChoose.setText(type);
                mTvChoose.setTextColor(Color.BLACK);
            }
        }else if(requestCode == 2&& data != null){//相机
            Bundle bundle = data.getExtras();
            mBitmap = (Bitmap) bundle.get("data");
            mIvHold.setImageBitmap(mBitmap);
            mHavePic = true;

        }else if(requestCode == 3&& data != null){//图库
            Uri uri = data.getData();
            mIvHold.setImageURI(uri);
            mBitmap =((BitmapDrawable) ((ImageView) mIvHold).getDrawable()).getBitmap();
            mHavePic = true;
        }
    }

}
