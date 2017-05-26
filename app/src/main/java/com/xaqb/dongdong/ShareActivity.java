package com.xaqb.dongdong;


import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.xaqb.dongdong.util.SPUtil;

public class ShareActivity extends BaseActivity {
    private TextView mTvLink;
    private TextView mTvInviteCode;
    private ShareActivity instance;

    @Override
    public void initTitle() {
    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_share);
        instance = this;
        mTvLink = (TextView) findViewById(R.id.tv_link_share);
        mTvInviteCode = (TextView) findViewById(R.id.tv_userinvitecode_share);
    }

    @Override
    public void initAvailable() {
    }

    @Override
    public void initData() {
        mTvInviteCode.setText(SPUtil.get(instance,"userinvitecode","").toString());
    }

    @Override
    public void addEvent() {
        mTvLink.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_link_share:
                Uri uri = Uri.parse("http://www.qbdongdong.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }
}
