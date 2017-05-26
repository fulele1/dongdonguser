package com.xaqb.dongdong;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.xaqb.dongdong.impl.OnOkDataFinishedListener;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;

import java.util.Map;

public class OrderDetailActivity extends BaseActivity implements OnOkDataFinishedListener{

    private OrderDetailActivity instance;
    private String mEoId;
    private TextView mTvOrderNum;
    private TextView mTvPostName;
    private TextView mTvPostPhone;
    private TextView mTvPostPlace;
    private TextView mTvReceiveName;
    private TextView mTvReceivePhone;
    private TextView mTvReceivePlace;
    private TextView mTvVariety;
    private TextView mTvWeight;
    private ImageView mIvOne;
    private ImageView mIvTwo;
    private ImageView mIvThree;
    private TextView mIvNote;
    private Button mBtCanael;

    @Override
    public void initTitle() {
        setTitleB("订单详情");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_order_detial);
        instance = this;
        mTvOrderNum = (TextView) findViewById(R.id.tv_order_num_o_det);
        mTvPostName = (TextView) findViewById(R.id.tv_post_name_o_det);
        mTvPostPhone = (TextView) findViewById(R.id.tv_post_phone_o_det);
        mTvPostPlace = (TextView) findViewById(R.id.tv_post_place_o_det);
        mTvReceiveName = (TextView) findViewById(R.id.tv_receive_name_o_det);
        mTvReceivePhone = (TextView) findViewById(R.id.tv_receive_phone_o_det);
        mTvReceivePlace = (TextView) findViewById(R.id.tv_receive_place_o_det);
        mTvVariety = (TextView) findViewById(R.id.tv_variety_order_del);
        mTvWeight = (TextView) findViewById(R.id.tv_weight_order_del);
        mIvOne = (ImageView) findViewById(R.id.iv_pic_one_order_del);
        mIvTwo = (ImageView) findViewById(R.id.iv_pic_two_order_del);
        mIvThree = (ImageView) findViewById(R.id.iv_pic_three_order_del);
        mIvNote = (TextView) findViewById(R.id.tv_note_order_del);
        mBtCanael = (Button) findViewById(R.id.bt_cancel_order_del);
    }

    @Override
    public void initAvailable() {
        Intent intent = getIntent();
        mEoId = intent.getStringExtra("eoid");
        setOnDataFinishedListener(instance);
        TAG = 0;
        mLoadingDialog.show("正在加载");
        okGetConnectionB(HttpUrlUtils.getHttpUrl().getOrderDetail()+mEoId+".json");
    }

    @Override
    public void initData() {

    }

    private int TAG;
    @Override
    public void addEvent() {
        mBtCanael.setOnClickListener(instance);
    }

    @Override
    public void okDataFinishedListener(String s) {

        /*{"state":0,"mess":"success","table":
            {"eoid":134,"eoorderno":"2017011815523914847259599092","eoexpressno":"","eodestname":"黑哥",
                    "eoprice":"0.00","eostuffimg":"http:\/\/www.qbdongdong.com\/uploads\/20170118\/a57ffdbac0a843b8e060a1b392aa9b9e.jpg",
                    "eostatus":"01","eocreatetime":1484725959,
                    "eocategory":"03","eoaddress":"","eoremark":"","eoweight":"2.00","eomp":"89898989",
                    "eodestaddress":"噢噢噢哦哦","eosendaddress":"恶魔那",
                    "eoname":"王晓雯","eodestprovince":"海南省","eodestcity":"海口市","eodestdistrict":"美兰区","eosendprovince":"广东省",
                    "eosendcity":"佛山市","eosenddistrict":"枫溪区","eodestmp":"33333333"
            }
        }*/

        Map<String,Object> map = GsonUtil.JsonToMap(s);
             if (map.get("state").toString().equals("1")){
            showToastB("此请求无法进行");
            return;
        }else if (map.get("state").toString().equals("0")) {
                 if (TAG == 0){
                     mTvOrderNum.setText(map.get("eoorderno").toString());//订单编号
                     mTvPostName.setText(map.get("eoname").toString());//寄件人姓名
                     mTvPostPhone.setText(map.get("eomp").toString());//寄件人电话
                     mTvPostPlace.setText(map.get("eosendprovince").toString()+
                             map.get("eosendcity").toString()+map.get("eosenddistrict").toString()+
                             map.get("eosendaddress").toString());//寄件人地址
                     mTvReceiveName.setText(map.get("eodestname").toString());//收件人姓名
                     mTvReceivePhone.setText(map.get("eodestmp").toString());//收件人电话
                     mTvReceivePlace.setText(map.get("eodestprovince").toString()+
                             map.get("eodestcity").toString()+map.get("eodestdistrict").toString()+
                             map.get("eodestaddress").toString());//收件人地址
                     mTvVariety.setText(map.get("eocategory").toString());//类型
                     mTvWeight.setText(map.get("eoweight").toString());//重量
                     //mTvOrderNum.setText(data.get("eostuffimg").toString());//物品照片
                     map.get("eostatus").toString();//订单状态
                     map.get("eoexpressno").toString();//快递单号
                     map.get("eoprice").toString();//金额
                     map.get("eocreatetime").toString();//订单创建时间
                     map.get("eoaddress").toString();//取货地址
                 } else if (TAG == 1){
                     showToastB(map.get("mess").toString());

                 }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancel_order_del:
                TAG = 1;
                mLoadingDialog.show("正在取消");
                okGetConnectionB(HttpUrlUtils.getHttpUrl().cancalOrder()+mEoId+".json");
                break;
        }
    }
}
