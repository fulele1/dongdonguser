package com.xaqb.dongdong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xaqb.dongdong.R;
import com.xaqb.dongdong.entity.Order;

import java.util.List;

/**
 * Created by fl on 2017/5/17.
 */

public class OrderAdapter extends BaseAdapter {
    Context mContext;
    List<Order> mOrders;
    public OrderAdapter(Context context, List<Order> orders) {
        mContext = context;
        mOrders = orders;
    }

    @Override
    public int getCount() {
        return mOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if (convertView==null){
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_order,null);
            holder = new viewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.place = (TextView) convertView.findViewById(R.id.tv_place);
            holder.orderanum = (TextView) convertView.findViewById(R.id.tv_order_num);
            holder.statu = (TextView) convertView.findViewById(R.id.tv_statu);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else{
            holder = (viewHolder) convertView.getTag();
        }
        holder.name.setText(mOrders.get(position).getEodestname());
        holder.num.setText(mOrders.get(position).getStafftel());
        holder.place.setText(mOrders.get(position).getEodestprovince()+mOrders.get(position).getEodestcity()+mOrders.get(position).getEodestdistrict()+mOrders.get(position).getEodestaddress());
        holder.orderanum.setText(mOrders.get(position).getEoorderno());
        holder.statu.setText(mOrders.get(position).getEostatus());
        holder.time.setText(mOrders.get(position).getEocreatetime());
        return convertView;
    }

    class viewHolder{
        TextView name;
        TextView num;
        TextView place;
        TextView orderanum;
        TextView statu;
        TextView time;

    }
}
