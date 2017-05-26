package com.xaqb.dongdong;


import android.widget.TextView;

import com.xaqb.dongdong.impl.OnOkDataFinishedListener;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.SPUtil;

import java.util.List;
import java.util.Map;

public class MoneyActivity extends BaseActivity implements OnOkDataFinishedListener{

    private TextView mTvMoney;
    private TextView mTvGrade;
    private MoneyActivity instance;
    private double sum;//积分的总和


    @Override
    public void initTitle() {
        setTitleB("我的钱包");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_money);
        instance = this;
        mTvMoney = (TextView) findViewById(R.id.tv_money_money);
        mTvGrade = (TextView) findViewById(R.id.tv_grade_money);
    }

    @Override
    public void initAvailable() {

    }

    @Override
    public void initData() {
        mTvMoney.setText(SPUtil.get(instance,"usermoney","").toString());
    }

    @Override
    public void addEvent() {
        setOnDataFinishedListener(instance);
        okGetConnectionB(HttpUrlUtils.getHttpUrl().getGrade()+"uid="+SPUtil.get(instance,"userid",""));
    }

    @Override
    public void okDataFinishedListener(String s) {
        //{"state":0,"mess":1,"table":[{"iid":24,"iintegral":10,"icreatetime":1487906393,"isname":"注册赠送"},
        // {"iid":33,"iintegral":10,"icreatetime":1493954089,"isname":"邀请他人"}]}
        Map<String,Object> map = GsonUtil.GsonToMaps(s);
        if (map.get("state").toString().equals("1.0")){
            showToastB(map.get("mess").toString());
            return;
        }
        if (map.get("state").toString().equals("0.0")&&map.get("mess").toString().equals("0.0")){
            showToastB("暂无数据");
            return;
        }

        List<Map<String,Object>> data = GsonUtil.GsonToListMaps(GsonUtil.GsonString(map.get("table")));//参数[{},{}]

        for (int i =0;i<data.size();i++){
            sum += (double) data.get(i).get("iintegral");
        }
        mTvGrade.setText(sum+"");


    }
}
