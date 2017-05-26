package com.xaqb.dongdong;


import android.os.Handler;
import android.view.View;

public class LeaderActivity extends BaseActivity {

    private LeaderActivity instance;

    @Override
    public void initTitle() {
        setTitleBarVisibleB(View.GONE);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_leader);
        instance = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intentB(instance,LoginActivity.class);
                finish();
            }
        },1500);

    }

    @Override
    public void initAvailable() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void addEvent() {

    }
}
