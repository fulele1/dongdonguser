package com.xaqb.dongdong;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

public class ModifyPSWActivity extends BaseActivity {
    private EditText mEtOld;
    private EditText mEtNew;
    private EditText mEtCon;
    private Button mBtFinished;
    private String mOld;
    private String mNew;
    private String mCon;
    private ModifyPSWActivity instance;

    @Override
    public void initTitle() {
        setTitleB("修改密码");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_modify_psw);
        instance = this;
        mEtOld = (EditText) findViewById(R.id.et_old_modify);
        mEtNew = (EditText) findViewById(R.id.et_new_modify);
        mEtCon = (EditText) findViewById(R.id.et_con_modify);
        mBtFinished = (Button) findViewById(R.id.bt_finished_modify);
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
            case R.id.bt_finished_modify://完成
                mOld = mEtOld.getText().toString().trim();
                mNew = mEtNew.getText().toString().trim();
                mCon = mEtCon.getText().toString().trim();
                if (mOld.equals("")){
                    showToastB("请输入旧密码");
                    return;
                }else if (mNew.equals("")){
                    showToastB("请输入新密码");
                    return;
                }else if (mCon.equals("")){
                    showToastB("请输入确认密码");
                    return;
                }else if (!mNew.equals(mCon)){
                    showToastB("两次输入的密码不一致");
                    return;
                }
                mLoadingDialog.show("正在修改");
                OkHttpUtils
                        .post()
                        .url(HttpUrlUtils.getHttpUrl().modifyPSW()+ SPUtil.get(instance,"userid","")+".json?")
                        .addParams("old_password",mOld)
                        .addParams("new_password",mNew)
                        .addParams("confirm_password",mCon)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                showToastB("请求异常");
                                mLoadingDialog.dismiss();
                            }
                            @Override
                            public void onResponse(String s, int i) {
                                mLoadingDialog.dismiss();
                                Map<String,Object> map = GsonUtil.GsonToMaps(s);
                                if (map.get("state").toString().equals("1.0")){//{"state":1,"mess":"旧密码输入错误"}
                                    showToastB(map.get("mess").toString());
                                    return;
                                }else if (map.get("state").toString().equals("0.0")&&map.get("mess").toString().equals("success")){//{state=0.0, mess=success}
                                    showToastB("密码修改成功");
                                    finish();
                                }
                            }
                        });
                break;
        }
    }
}
