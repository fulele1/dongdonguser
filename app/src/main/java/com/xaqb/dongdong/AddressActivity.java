package com.xaqb.dongdong;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.xaqb.dongdong.Adapter.AddressAdapter;
import com.xaqb.dongdong.entity.Address;
import com.xaqb.dongdong.impl.OnOkDataFinishedListener;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.SPUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AddressActivity extends BaseActivity implements OnOkDataFinishedListener{

    private ListView mList;
    private Button mBtAddress;
    private AddressActivity instance;
    private List<Address> mAddress;

    @Override
    public void initTitle() {
        setTitleB("地址管理");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_address);
        instance = this;
        mList = (ListView) findViewById(R.id.list_address);
        mList.setDivider(new ColorDrawable(Color.GRAY));
        mList.setDividerHeight(15);
        mBtAddress = (Button) findViewById(R.id.bt_add_address_add);
    }

    @Override
    public void initAvailable() {
    }

    @Override
    public void initData() {
        setOnDataFinishedListener(instance);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("add",mAddress.get(position).getAddr());
                intent.putExtras(bundle);
                instance.setResult(RESULT_OK,intent);
                AddressActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoadingDialog.show("正在连接...");
        okGetConnectionB(HttpUrlUtils.getHttpUrl().getAddress()+SPUtil.get(instance,"userid","").toString()+".json?access_token="+SPUtil.get(instance,"access_token","").toString()+"&id="+SPUtil.get(instance,"userid","").toString()+"p=");

    }

    @Override
    public void addEvent() {
        mBtAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add_address_add://添加地址
                intentB(instance,AddAddressActivity.class);
                break;
        }
    }


/*
    {"state":0,"mess":1,"table":[
        {   "uaid":88,"uaname":"付乐","uaarea":"",
            "uapostcode":"","uaprovince":"安徽省",
            "uacity":"安庆市","uadistrict":"枞阳县",
            "uaaddress":"lll","uamp":"18392395597",
            "uaupdatetime":1489051236
            },
        {"uaid":94,"uaname":"你啊",
        "uaarea":"","uapostcode":"",
        "uaprovince":"北京市","uacity":"北京市",
        "uadistrict":"朝阳区","uaaddress":"xx街道",
        "uamp":"18392395598","uaupdatetime":1493802818
        }
    ]}
*/

    @Override
    public void okDataFinishedListener(String s) {
        Map<String,Object> map = GsonUtil.GsonToMaps(s);//{state=0.0, mess=1.0, table=[{uaid=88.0, uaname=付乐,}{uaid=88.0, uaname=付乐}]}

        if (map.get("state").toString().equals("1.0")){
            showToastB(map.get("mess").toString());
            return;
        }
        if (map.get("state").toString().equals("0.0")&&map.get("mess").toString().equals("0.0")){
            showToastB("暂无数据");
            return;
        }
        List<Map<String,Object>> data = GsonUtil.GsonToListMaps(GsonUtil.GsonString(map.get("table")));//参数[{},{}]
        mAddress = new ArrayList<>();
        for (int i =0;i<data.size();i++){
            Address address = new Address();
            address.setUser(data.get(i).get("uaname").toString());
            address.setPhone(data.get(i).get("uamp").toString());
            String add = data.get(i).get("uacity").toString()+
                    data.get(i).get("uadistrict").toString()+
                    data.get(i).get("uaaddress").toString();
            address.setAddr(add);
            address.setProvince(data.get(i).get("uaprovince").toString());
            address.setId(data.get(i).get("uaid").toString());
            mAddress.add(address);
        }
        mList.setAdapter(new AddressAdapter(instance,mAddress));
    }


}