package com.xaqb.dongdong;


import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

public class FindActivity extends BaseActivity {

    private EditText mEtPhone;
    private EditText mEtCode;
    private EditText mEtPsw;
    private EditText mEtConPsw;
    private ImageView mIvCode;
    private Button mBtFinished;
    private String mPhone;
    private String mCode;
    private String mPsw;
    private String mConPsw;
    private String mCodeKey;
    private FindActivity.TimeCount mTime;
    private TextView mTvAgain;
    private boolean canClick;
    private FindActivity instance;

    @Override
    public void initTitle() {
        setTitleB("找回密码");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_find);
        instance = this;
        mEtPhone = (EditText) findViewById(R.id.et_phone_find);
        mEtCode = (EditText) findViewById(R.id.et_code_find);
        mEtPsw = (EditText) findViewById(R.id.et_psw_find);
        mEtConPsw = (EditText) findViewById(R.id.et_con_psw_find);
        mIvCode = (ImageView) findViewById(R.id.iv_code_find);
        mBtFinished = (Button) findViewById(R.id.bt_finished_find);
        mTvAgain = (TextView) findViewById(R.id.tv_again_find);
        mTime = new TimeCount(60000,1000);
    }

    @Override
    public void initAvailable() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void addEvent() {
        mIvCode.setOnClickListener(instance);
        mBtFinished.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_code_find://手机验证码
                if (!canClick){
                    getCode();
                }
                break;
            case R.id.bt_finished_find://完成
                finished();
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
                        mIvCode.setImageResource(R.mipmap.new48);
                        showToastB("验证码已发送至您的手机");
                        mTime.start();
                    }
                });
    }


    public void finished(){
        mPhone = mEtPhone.getText().toString().trim();
        mCode = mEtCode.getText().toString().trim();
        mPsw = mEtPsw.getText().toString().trim();
        mConPsw = mEtConPsw.getText().toString().trim();
        if (mPhone.equals("")){
            showToastB("请输入手机号码");
            return;
        }else if (mCode.equals("")){
            showToastB("请输入手机验证码");
            return;
        }else if (mPsw.equals("")){
            showToastB("请输入密码");
            return;
        }else if (mConPsw.equals("")){
            showToastB("请输入确认密码");
            return;
        }else if (!mPsw.equals(mConPsw)){
            showToastB("两次输入密码不一致");
            return;
        }
        mLoadingDialog.show("正在修改");
        OkHttpUtils
                .post()
                .url(HttpUrlUtils.getHttpUrl().findPsw())
                .addParams("tel",mPhone)
                .addParams("new_password",mPsw)
                .addParams("confirm_password",mConPsw)
                .addParams("code",mCode)
                .addParams("codekey",mCodeKey)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        mLoadingDialog.dismiss();
                        showToastB("访问异常");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        mLoadingDialog.dismiss();
                        SPUtil.put(instance,"loginNum",mPhone);//保存账号到偏好设置
                        SPUtil.put(instance,"password","");//保存密码到偏好设置
                        SPUtil.put(instance,"FirstIn",true);//改变登录界面的是否首次进入的偏好设置，防止跳转到登录界面时直接进入主界面
                        intentB(instance,LoginActivity.class);
                        finish();
                    }
                });

    }

    /**
     * 倒计时计时器
     */
    class TimeCount extends CountDownTimer {

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
            mIvCode.setImageResource(R.mipmap.newg48);
            mTvAgain.setText("重新发送");
            canClick = false;
        }
    }


}
