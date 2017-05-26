package com.xaqb.dongdong;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xaqb.dongdong.impl.OnOkDataFinishedListener;
import com.xaqb.dongdong.util.AppUtils;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;
import com.xaqb.dongdong.util.SPUtil;

import java.util.Map;


public class LoginActivity extends BaseActivity implements OnOkDataFinishedListener{
    private Button mBtLogin;
    private TextView mTvNew;
    private TextView mTvFind;
    private LoginActivity instance;
    private EditText mEtPhone;
    private EditText mEtPsw;
    private String mPhone;
    private String mPsw;
    @Override
    public void initTitle() {
        setTitleB("登录");
        setBackVisibleB(View.INVISIBLE);
    }
    @Override
    public void initView() {


        if ((Boolean)SPUtil.get(this,"FirstIn",true)==true){
            SPUtil.put(this,"FirstIn",false);
            setContentView(R.layout.activity_login);
            instance = this;
        }else{
            instance = this;
            setContentView(R.layout.activity_login);
            intentB(instance,MainActivity.class);
            finish();
        }
        mBtLogin = (Button) findViewById(R.id.bt_login_login);
        mTvNew = (TextView) findViewById(R.id.tv_new_login);
        mTvFind = (TextView) findViewById(R.id.tv_find_login);
        mEtPsw = (EditText) findViewById(R.id.et_psw_login);
        mEtPhone = (EditText) findViewById(R.id.et_phone_login);

    }

    @Override
    public void initAvailable() {}

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SPUtil.get(instance,"password","").toString().equals("")){
            mEtPhone.setText(SPUtil.get(instance,"loginNum","").toString());
            mEtPsw.setText(SPUtil.get(instance,"password","").toString());
        }else if (SPUtil.get(instance,"password","").toString().equals("")){//找回密码或者新注册时
            mEtPhone.setText(SPUtil.get(instance,"loginNum","").toString());
            mEtPsw.setText("");
        }
    }

    @Override
    public void addEvent() {
        mBtLogin.setOnClickListener(instance);
        mTvNew.setOnClickListener(instance);
        mTvFind.setOnClickListener(instance);
        setOnDataFinishedListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login_login://登录按钮
                login();
                break;
            case R.id.tv_new_login://注册
                intentB(instance,RegisterActivity.class);
                finish();
                break;
            case R.id.tv_find_login://找回密码
                intentB(instance,FindActivity.class);
                break;
        }
    }

    /**
     * 登录
     */
    public void login(){
        mPhone = mEtPhone.getText().toString().trim();
        mPsw = mEtPsw.getText().toString().trim();
        if (mPhone.equals("")){
            showToastB("请输入账号");
            return;
        }else if (mPsw.equals("")){
            showToastB("请输入密码");
            return;
        }else if (AppUtils.getInfo(instance).equals("")){
            showToastB("无法获取当前设备号");
            return;
        }

        //进行POST网络请求
        mLoadingDialog.show("正在登陆");
        okPostConnectionB(HttpUrlUtils.getHttpUrl().userLogin(),"username",mPhone,"password",mPsw,"device",AppUtils.getInfo(instance));
    }


    @Override
    public void okDataFinishedListener(String s) {
        mLoadingDialog.dismiss();
        LogUtil.i(s.toString());
        Map<String,Object> map = GsonUtil.GsonToMaps(s);
        if (map.get("state").toString().equals("1.0")){
            showToastB(map.get("mess").toString());
            return;
        }
            Map<String,Object> data = GsonUtil.JsonToMap(GsonUtil.GsonString(map.get("table")));
            LogUtil.i(data.toString());

       /*{tags=用户, userpwd=24e615029dd3cd7f06ea0618f12536c2,
         usertags=01, usersalt=WkOS, userid=77.0,
         userdevice=866322024015134, usermp=18392395598,
         userisreal=2.0, username=18392395598,
         userinvitecode=pbaaab, usernickname=付乐,
         userlogincount=188.0, userqq=, userlogintime=1.49369871E9,
         usermoney=0.00, userheadpic=http://www.qbdongdong.com/uploads/20170309/d9448f452b3a54e7e87d22c8654ebb92.jpg,
         access_token=acd7523a6e33ca2e2c9bdb9e5f4a9926, userloginip=113.135.249.108}*/

        SPUtil.put(instance,"tags",data.get("tags").toString());//标签： 快递员 用户
        SPUtil.put(instance,"usermp",data.get("usermp").toString());//账号
        SPUtil.put(instance,"userid",data.get("userid").toString());//用户id
        SPUtil.put(instance,"userisreal",data.get("userisreal").toString());//是否实名认证
        SPUtil.put(instance,"username",data.get("username").toString());//用户名
        SPUtil.put(instance,"userinvitecode",data.get("userinvitecode").toString());//邀请码
        SPUtil.put(instance,"usernickname",data.get("usernickname").toString());//用户昵称
        SPUtil.put(instance,"access_token",data.get("access_token").toString());//access_token值
        SPUtil.put(instance,"usermoney",data.get("usermoney").toString());//零钱
        SPUtil.put(instance,"userheadpic",data.get("userheadpic").toString());//用户照片
        SPUtil.put(instance,"loginNum",mPhone);
        SPUtil.put(instance,"password",mPsw);
        showToastB("登录成功");
        intentB(instance,MainActivity.class);
        finish();
    }
}