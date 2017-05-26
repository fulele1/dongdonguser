package com.xaqb.dongdong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TypeActivity extends Activity implements View.OnClickListener{

    private TextView mBtIde;
    private TextView mBtJz;
    private TextView mBtHkb;
    private TextView mBtJgz;
    private TextView mBtSbz;
    private TextView mBtJiz;
    private TextView mBtGnhz;
    private TextView mBtGazxz;
    private TextView mBtOther;
    private TypeActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_layout);
        instance = this;
        mBtIde = (TextView) findViewById(R.id.bt_ide_win);
        mBtJz = (TextView) findViewById(R.id.bt_jz_win);
        mBtHkb = (TextView) findViewById(R.id.bt_hkb_win);
        mBtJgz = (TextView) findViewById(R.id.bt_jgz_win);
        mBtSbz = (TextView) findViewById(R.id.bt_sbz_win);
        mBtJiz = (TextView) findViewById(R.id.bt_jiz_win);
        mBtGnhz = (TextView) findViewById(R.id.bt_gnhz_win);
        mBtGazxz = (TextView) findViewById(R.id.bt_gazxz_win);
        mBtOther = (TextView) findViewById(R.id.bt_other_win);
        mBtIde.setOnClickListener(instance);
        mBtJz.setOnClickListener(instance);
        mBtHkb.setOnClickListener(instance);
        mBtJgz.setOnClickListener(instance);
        mBtSbz.setOnClickListener(instance);
        mBtJiz.setOnClickListener(instance);
        mBtGnhz.setOnClickListener(instance);
        mBtGazxz.setOnClickListener(instance);
        mBtOther.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ide_win:
                intent(mBtIde.getText()+"");
                break;
            case R.id.bt_jz_win:
                intent(mBtJz.getText()+"");
                break;
            case R.id.bt_hkb_win:
                intent(mBtHkb.getText()+"");
                break;
            case R.id.bt_jgz_win:
                intent(mBtJgz.getText()+"");
                break;
            case R.id.bt_sbz_win:
                intent(mBtSbz.getText()+"");
                break;
            case R.id.bt_jiz_win:
                intent(mBtJiz.getText()+"");
                break;
            case R.id.bt_gnhz_win:
                intent(mBtGnhz.getText()+"");
                break;
            case R.id.bt_gazxz_win:
                intent(mBtGazxz.getText()+"");
                break;
            case R.id.bt_other_win:
                intent(mBtOther.getText()+"");
                break;
        }

    }

    public void intent(String s){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("type",s);
        intent.putExtras(bundle);
        instance.setResult(RESULT_OK,intent);
        finish();
    }
}
