package com.xaqb.dongdong;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xaqb.dongdong.util.AppUtils;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

public class RegisterActivity extends BaseActivity {

    @Override
    public void initTitle() {
        setTitleB("注册");
    }

    private RegisterActivity instance;
    private EditText mEtPhone;
    private EditText mEtCode;
    private EditText mEtPsw;
    private EditText mEtConPsw;
    private ImageView mBtCode;
    private ImageView mBtAgree;
    private Button mBtReg;
    private TextView mTvAgain;
    private TimeCount mTime;
    private String mPhone;
    private boolean canClick;
    private boolean isAgree;
    private String mCode;
    private String mCodeKey;
    private String mPsw;
    private String mConPsw;

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        instance = this;
        mEtPhone = (EditText) findViewById(R.id.et_phone_reg);
        mEtCode = (EditText) findViewById(R.id.et_code_reg);
        mEtPsw = (EditText) findViewById(R.id.et_psw_reg);
        mEtConPsw = (EditText) findViewById(R.id.et_con_psw_reg);
        mBtReg = (Button) findViewById(R.id.bt_reg_reg);
        mBtCode = (ImageView) findViewById(R.id.bt_code_reg);
        mBtAgree = (ImageView) findViewById(R.id.iv_agree_reg);
        mTvAgain = (TextView) findViewById(R.id.tv_again_reg);
        mTime = new TimeCount(60000,1000);
    }

    @Override
    public void initAvailable() {}

    @Override
    public void initData() {}

    @Override
    public void addEvent() {
        mBtCode.setOnClickListener(instance);
        mBtReg.setOnClickListener(instance);
        mBtAgree.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_code_reg://手机验证码
                if (!canClick){
                    getCode();
                }
                break;
            case R.id.iv_agree_reg://同意协议
                if (!isAgree){
                    mBtAgree.setImageResource(R.mipmap.checkc32);
                    isAgree = true;
                }else{
                    mBtAgree.setImageResource(R.mipmap.checkg32);
                    isAgree = false;
                }
                break;
            case R.id.bt_reg_reg://注册按钮
                register();
                break;
        }
    }


    /**
     * 获取手机验证码
     */
    public void getCode(){
        mPhone = mEtPhone.getText().toString().trim();
        if (mPhone.equals("")){
            showToastB("请输入手机号码");
            return;
        }
        OkHttpUtils
                .post()
                .url(HttpUrlUtils.getHttpUrl().phoneCode())
                .addParams("tel",mPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        showToastB("网络请求失败");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Map<String, Object> map = GsonUtil.GsonToMaps(s);
                        mCodeKey = map.get("table").toString();
                        mBtCode.setImageResource(R.mipmap.new48);
                        showToastB("验证码已发送至您的手机");
                        mTime.start();
                    }
                });
    }


    public void register(){
        mCode = mEtCode.getText().toString().trim();
        mPsw = mEtPsw.getText().toString().trim();
        mConPsw = mEtConPsw.getText().toString().trim();
         if (mPhone.equals("")){
            showToastB("请输入手机号码");
            return;
        }else if (mCode.equals("")){
            showToastB("请输入验证码");
            return;
        }else if (mPsw.equals("")){
            showToastB("请输入密码");
            return;
        }else if (mConPsw.equals("")){
            showToastB("请输入确认密码");
            return;
        }else if (!mConPsw.equals(mPsw)){
            showToastB("两次密码不一致");
            return;
        }else if (!isAgree){
            showToastB("请同意咚咚协议");
            return;
        }
        OkHttpUtils
                .post()
                .url(HttpUrlUtils.getHttpUrl().userRegister())
                .addParams("username",mPhone)
                .addParams("password",mPsw)
                .addParams("code",mCode)
                .addParams("device", AppUtils.getInfo(instance))
                .addParams("codekey",mCodeKey)
                .addParams("invitecode",SPUtil.get(instance,"userinvitecode","").toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        showToastB("网络连接异常");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        SPUtil.put(instance,"loginNum",mPhone);//保存账号到偏好设置
                        SPUtil.put(instance,"password","");//保存密码到偏好设置
                        showToastB("注册成功");
                        SPUtil.put(instance,"FirstIn",true);//改变登录界面的是否首次进入的偏好设置，防止跳转到登录界面时直接进入主界面
                        intentB(instance,LoginActivity.class);
                        finish();
                    }
                });
    }

    /**
     * 倒计时计时器
     */
    class TimeCount extends CountDownTimer{

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvAgain.setText(millisUntilFinished/1000+"s后重新获取");
            canClick = true;
        }

        @Override
        public void onFinish() {
            mBtCode.setImageResource(R.mipmap.newg48);
            mTvAgain.setText("重新发送");
            canClick = false;
        }
    }
}