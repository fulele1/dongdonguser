package com.xaqb.dongdong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
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
 * Created by fl on 2017/4/20.
 */

public class WaitingFragment extends Fragment implements AdapterView.OnItemClickListener{
    private Context instance;
    private View view;
    private TextView tv;
    private ListView mList;
    private List<Order> mOrders;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_waiting,null);
        instance = WaitingFragment.this.getActivity();
        getHttpData();

        tv = (TextView) view.findViewById(R.id.tv_waiting);
        mList = (ListView) view.findViewById(R.id.list_waiting);
        mList.setOnItemClickListener(WaitingFragment.this);
        return view;
    }
    
    /**
     * 获取代取件的数据
     */
    public void getHttpData(){
        OkHttpUtils
                .get()
                .url(HttpUrlUtils.getHttpUrl().getOrder()+"uid="+ SPUtil.get(instance,"userid","")+"&status="+"02")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(instance,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        LogUtil.i("待取件"+s);
                        //{"state":1,"mess":"数据为空"}
                        /*{"state":0,"mess":1,"table":[{"eoid":138,"eoorderno":"2017011917143914848172792169",
                             "eotitle":"","eostatus":"02",
                             "eostuffimg":"http:\/\/www.qbdongdong.com\/uploads\/20170119\/795174e6e33ef1b7b051e01614090045.jpg",
                             "eoremark":"","eodestprovince":"}]*/

                        Map<String,Object> map = GsonUtil.JsonToMap(s);
                        if (map.get("state").toString().equals("1")){
                            tv.setText("您还没有相关的订单");
                            tv.setVisibility(View.VISIBLE);
                            return;
                        }else if (map.get("state").toString().equals("0")){
                            List<Map<String,Object>> data = GsonUtil.GsonToListMaps(GsonUtil.GsonString(map.get("table")));
                            mOrders = new ArrayList<Order>();
                            for (int j = 0;j<data.size();j++){
                                Order order = new Order();
                                order.setEoid(data.get(j).get("eoid").toString());
                                order.setEoorderno(data.get(j).get("eoorderno").toString());
                                order.setEotitle(data.get(j).get("eotitle").toString());
                                order.setEostatus(getStatus(data.get(j).get("eostatus").toString()));
                                order.setEostuffimg(data.get(j).get("eostuffimg").toString());
                                order.setEoremark(data.get(j).get("eoremark").toString());
                                order.setEodestprovince(data.get(j).get("eodestprovince").toString());
                                order.setEodestcity(data.get(j).get("eodestcity").toString());
                                order.setEodestdistrict(data.get(j).get("eodestdistrict").toString());
                                order.setEodestaddress(data.get(j).get("eodestaddress").toString());
                                order.setEoexpressstaff(data.get(j).get("eoexpressstaff").toString());
                                //order.setStafftel(data.get(j).get("stafftel").toString());
                                order.setEodestname(data.get(j).get("eodestname").toString());
                                order.setEodestmp(data.get(j).get("eodestmp").toString());
                                order.setEocreatetime(data.get(j).get("eocreatetime").toString());
                                mOrders.add(order);
                            }
                            mList.setAdapter(new OrderAdapter(instance,mOrders));
                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(instance, OrderDetailActivity.class);
        intent.putExtra("eoid",mOrders.get(position).getEoid());
        startActivity(intent);
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

}
