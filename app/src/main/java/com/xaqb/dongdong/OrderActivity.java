package com.xaqb.dongdong;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xaqb.dongdong.Adapter.OrderFragmentAdapter;
import com.xaqb.dongdong.fragment.GotFragment;
import com.xaqb.dongdong.fragment.PostingFragment;
import com.xaqb.dongdong.fragment.PublishedFragment;
import com.xaqb.dongdong.fragment.WaitingFragment;
import com.xaqb.dongdong.fragment.payingFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends FragmentActivity {
    private ViewPager mVpg;
    private List<Fragment> mFrags;
    private FragmentManager mFragmentManager;
    private RadioGroup mRgp;
    private RadioButton mRbWaiting;
    private RadioButton mRbPaying;
    private RadioButton mRbPosting;
    private RadioButton mRbGot;
    private RadioButton mRbPublished;
    private TextView mTvBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order);
        initView();
        initData();
        initEvent();
        initAvaliable();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mVpg.setAdapter(new OrderFragmentAdapter(mFragmentManager,mFrags));
        mRgp.setOnCheckedChangeListener(new CheckedChange());
        mVpg.setOnPageChangeListener(new pageChange());
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderActivity.this.finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mFrags = new ArrayList<>();
        mFrags.add(new PublishedFragment());
        mFrags.add(new WaitingFragment());
        mFrags.add(new payingFragment());
        mFrags.add(new PostingFragment());
        mFrags.add(new GotFragment());
    }

    /**
     * 初始化view
     */
    private void initView() {
        mFragmentManager = this.getSupportFragmentManager();
        mVpg = (ViewPager) findViewById(R.id.vpg_order);
        mRgp = (RadioGroup) findViewById(R.id.rp_order);
        mRbWaiting = (RadioButton) findViewById(R.id.rb_waiting);
        mRbPaying = (RadioButton) findViewById(R.id.rb_paying);
        mRbPosting = (RadioButton) findViewById(R.id.rb_posting);
        mRbPublished = (RadioButton) findViewById(R.id.rb_published);
        mRbGot = (RadioButton) findViewById(R.id.rb_got);
        mTvBack = (TextView) findViewById(R.id.tv_back_order);
    }

    /**
     * 获取Intent携带的数据
     */
    private void initAvaliable(){
        String sign = getIntent().getStringExtra("intentData");
        switch (sign){
            case "0":
                mRbPublished.setChecked(true);
                mRbPublished.setTextColor(Color.BLUE);
                break;
            case "1":
                mRbWaiting.setChecked(true);
                mRbWaiting.setTextColor(Color.BLUE);
                break;
            case "2":
                mRbPaying.setChecked(true);
                mRbPaying.setTextColor(Color.BLUE);
                break;
            case "3":
                mRbPosting.setChecked(true);
                mRbPosting.setTextColor(Color.BLUE);
                break;
            case "4":
                mRbGot.setChecked(true);
                mRbGot.setTextColor(Color.BLUE);
                break;
        }
    }

    /**
     * 页面滑动后设置当前为点击
     */
    class pageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    mRbPublished.setChecked(true);
                    mRbPublished.setTextColor(Color.BLUE);
                    mRbPosting.setTextColor(Color.BLACK);
                    mRbPaying.setTextColor(Color.BLACK);
                    mRbGot.setTextColor(Color.BLACK);
                    mRbWaiting.setTextColor(Color.BLACK);
                    break;
                case 1:
                    mRbWaiting.setChecked(true);
                    mRbWaiting.setTextColor(Color.BLUE);
                    mRbPosting.setTextColor(Color.BLACK);
                    mRbPaying.setTextColor(Color.BLACK);
                    mRbGot.setTextColor(Color.BLACK);
                    mRbPublished.setTextColor(Color.BLACK);
                    break;
                case 2:
                    mRbPaying.setChecked(true);
                    mRbPaying.setTextColor(Color.BLUE);
                    mRbPosting.setTextColor(Color.BLACK);
                    mRbGot.setTextColor(Color.BLACK);
                    mRbPublished.setTextColor(Color.BLACK);
                    mRbWaiting.setTextColor(Color.BLACK);

                    break;
                case 3:
                    mRbPosting.setChecked(true);
                    mRbWaiting.setTextColor(Color.BLACK);
                    mRbPosting.setTextColor(Color.BLUE);
                    mRbGot.setTextColor(Color.BLACK);
                    mRbPublished.setTextColor(Color.BLACK);
                    mRbPaying.setTextColor(Color.BLACK);
                    break;
                case 4:
                    mRbGot.setChecked(true);
                    mRbWaiting.setTextColor(Color.BLACK);
                    mRbPosting.setTextColor(Color.BLACK);
                    mRbPublished.setTextColor(Color.BLACK);
                    mRbGot.setTextColor(Color.BLUE);
                    mRbPaying.setTextColor(Color.BLACK);
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    /**
     * 点击改变页面的监听事件
     */
    class CheckedChange implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_published:
                    mVpg.setCurrentItem(0);
                    break;
                case R.id.rb_waiting:
                    mVpg.setCurrentItem(1);
                    break;
                case R.id.rb_paying:
                    mVpg.setCurrentItem(2);
                    break;
                case R.id.rb_posting:
                    mVpg.setCurrentItem(3);
                    break;
                case R.id.rb_got:
                    mVpg.setCurrentItem(4);
                    break;
            }
        }
    }


}
