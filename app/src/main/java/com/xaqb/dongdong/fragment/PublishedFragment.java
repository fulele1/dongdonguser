package com.xaqb.dongdong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xaqb.dongdong.Adapter.OrderAdapter;
import com.xaqb.dongdong.OrderDetailActivity;
import com.xaqb.dongdong.R;
import com.xaqb.dongdong.entity.Order;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by fl on 2017/5/2.
 */

public class PublishedFragment extends Fragment implements AdapterView.OnItemClickListener{
    private View view;
    private TextView mTv;
    private ListView mList;
    private List<Order> mOrders;
    private Context instance;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_published,null);
        instance = PublishedFragment.this.getActivity();
        getHttpData();
        mTv = (TextView) view.findViewById(R.id.tv_published);
        mList = (ListView) view.findViewById(R.id.list_published);
        mList.setOnItemClickListener(PublishedFragment.this);
        return view;
    }


    /**
     * 获取已发布的数据
     */
    public void getHttpData(){
        OkHttpUtils
                .get()
                .url(HttpUrlUtils.getHttpUrl().getOrder()+"uid="+ SPUtil.get(this.getActivity(),"userid","")+"&status="+"01")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(instance,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        LogUtil.i("已发布"+s);
                        //{"state":1,"mess":"数据为空"}
                        /*published{"state":0,"mess":3,
                                    "table":[
                                                {"eoid":134,"eoorderno":"2017011815523914847259599092","eotitle":"","eostatus":"01",
		                                            "eostuffimg":"http:\/\/www.qbdongdong.com\/uploads\/20170118\/a57ffdbac0a843b8e060a1b392aa9b9e.jpg",
		                                            "eoremark":"","eodestprovince":"海南省","eodestcity":"海口市",
		                                            "eodestdistrict":"美兰区","eodestaddress":"噢噢噢哦哦","eoexpressstaff":0,
		                                            "stafftel":null,"eodestname":"黑哥","eodestmp":"33333333","eocreatetime":1484725959},
                                            ]*/
                        Map <String,Object> map = GsonUtil.JsonToMap(s);
                        if (map.get("state").toString().equals("1")){
                            mTv.setText("您还没有相关的订单");
                            mTv.setVisibility(View.VISIBLE);
                            return;
                        }else if (map.get("state").toString().equals("0")){
                            List<Map <String,Object>> data = GsonUtil.GsonToListMaps(GsonUtil.GsonString(map.get("table")));
                            mOrders = new ArrayList<Order>();
                            for (int j = 0;j<data.size();j++){
                                Order order = new Order();
                                order.setEoid(data.get(j).get("eoid").toString());//订单id
                                order.setEoorderno(data.get(j).get("eoorderno").toString());//订单号
                                order.setEotitle(data.get(j).get("eotitle").toString());//订单名称
                                order.setEostatus(getStatus(data.get(j).get("eostatus").toString()));//订单状态
                                order.setEostuffimg(data.get(j).get("eostuffimg").toString());//物品照片
                                order.setEoremark(data.get(j).get("eoremark").toString());//描述
                                order.setEodestprovince(data.get(j).get("eodestprovince").toString());//收件人省份
                                order.setEodestcity(data.get(j).get("eodestcity").toString());//收件人城市
                                order.setEodestdistrict(data.get(j).get("eodestdistrict").toString());//收件人区
                                order.setEodestaddress(data.get(j).get("eodestaddress").toString());//收件人地址
                                order.setEoexpressstaff(data.get(j).get("eoexpressstaff").toString());//指定的快递员id
                                //order.setStafftel(data.get(j).get("stafftel").toString());
                                order.setEodestname(data.get(j).get("eodestname").toString());//寄件人姓名
                                order.setEodestmp(data.get(j).get("eodestmp").toString());//寄件人电话
                                order.setEocreatetime(data.get(j).get("eocreatetime").toString());//订单创建的时间
                                mOrders.add(order);
                            }
                            mList.setAdapter(new OrderAdapter(instance,mOrders));
                        }
                    }
                });
    }


    public String getStatus(String status){
        switch (status){
            case "01":
                return "已发布";
            case "02":
                return "待收取";
            case "03":
                return "代付款";
            case "04":
                return "运送中";
            case "05":
                return "已发送";
        }

        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(instance, OrderDetailActivity.class);
        intent.putExtra("eoid",mOrders.get(position).getEoid());
        startActivity(intent);
    }

}