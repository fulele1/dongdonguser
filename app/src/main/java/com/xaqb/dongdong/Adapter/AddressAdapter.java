package com.xaqb.dongdong.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xaqb.dongdong.AddressActivity;
import com.xaqb.dongdong.EditAddressActivity;
import com.xaqb.dongdong.R;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by fl on 2017/5/3.
 */

public class AddressAdapter extends BaseAdapter {
    private Context mContext;
    private boolean isDefault;
    private List<com.xaqb.dongdong.entity.Address> mAddresses;

    public AddressAdapter(Context context, List<com.xaqb.dongdong.entity.Address> addresses) {
        mContext = context;
        mAddresses = addresses;
    }

    @Override
    public int getCount() {
        return mAddresses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_address,null);
            holder.tvUser = (TextView) convertView.findViewById(R.id.tv_user_add_list);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone_add_list);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_add_add_list);
            holder.tvCheck = (TextView) convertView.findViewById(R.id.tv_check_add_list);
            holder.tvEdit = (TextView) convertView.findViewById(R.id.tv_edit_add_list);
            holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete_add_list);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvUser.setText(mAddresses.get(position).getUser());
        holder.tvPhone.setText(mAddresses.get(position).getPhone());
        holder.tvAddress.setText(mAddresses.get(position).getAddr());
        holder.tvCheck.setOnClickListener(new View.OnClickListener() {//默认按钮
            @Override
            public void onClick(View v) {//设为默认
                if (!isDefault){
                    isDefault = true;
                    holder.tvCheck.setTextColor(Color.RED);
                    Drawable dwLeft = mContext.getResources().getDrawable(R.mipmap.cr32);
                    dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
                    holder.tvCheck.setCompoundDrawables(dwLeft,null,null,null);
                }else{
                    isDefault = false;
                    holder.tvCheck.setTextColor(Color.GRAY);
                    Drawable dwLeft = mContext.getResources().getDrawable(R.mipmap.c32);
                    dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
                    holder.tvCheck.setCompoundDrawables(dwLeft,null,null,null);
                }
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//编辑按钮
                Intent intent = new Intent(mContext,EditAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("add",mAddresses.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//删除按钮
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定要删除此地址吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //调用删除地址接口
                        OkHttpUtils
                                .delete()
                                .url(HttpUrlUtils.getHttpUrl().deleteAddress()+mAddresses.get(position).getId()+".json?access_token="+ SPUtil.get(mContext,"access_token",""))
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int i) {
                                        LogUtil.i("失败");
                                    }

                                    @Override
                                    public void onResponse(String s, int i) {
                                        LogUtil.i("成功"+s);
                                        Intent intent = new Intent(mContext, AddressActivity.class);
                                        mContext.startActivity(intent);
                                    }
                                });
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });
        return convertView;
    }

//    /**
//     * 设置Listview自条目不可点击
//     * @param position
//     * @return
//     */
//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }
}

class ViewHolder{
    TextView tvUser;
    TextView tvPhone;
    TextView tvAddress;
    TextView tvCheck;
    TextView tvEdit;
    TextView tvDelete;
}
