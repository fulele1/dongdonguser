package com.xaqb.dongdong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xaqb.dongdong.R;
import com.xaqb.dongdong.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/18.
 */

public class GragAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;
    private List<Integer> mListIcon;
    public GragAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mList.add("我的钱包");
        mList.add("我的订单");
        mList.add("管理地址");
        mList.add("关于咚咚");
        mList.add("修改设置");
        mList.add("实名认证");

        mListIcon = new ArrayList<>();
        mListIcon.add(R.mipmap.money64);
        mListIcon.add(R.mipmap.order64);
        mListIcon.add(R.mipmap.loc64);
        mListIcon.add(R.mipmap.about64);
        mListIcon.add(R.mipmap.set64);
        mListIcon.add(R.mipmap.che64);
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderDrag holderDrag;
        if (null==convertView){
            holderDrag = new HolderDrag();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_drag,null);
            holderDrag.imageView = (ImageView) convertView.findViewById(R.id.iv_list_drag);
            holderDrag.textView = (TextView) convertView.findViewById(R.id.tv_list_drag);
            holderDrag.imageViewref = (ImageView) convertView.findViewById(R.id.iv_ref_drag);
            convertView.setTag(holderDrag);
        }else{
            holderDrag = (HolderDrag) convertView.getTag();
        }

        holderDrag.imageView.setImageResource(mListIcon.get(position));
        holderDrag.textView.setText(mList.get(position));
        if (position == 5){
            holderDrag.imageViewref.setVisibility(View.VISIBLE);
            if (SPUtil.get(mContext,"userisreal","").equals("2.0")){
                holderDrag.imageViewref.setImageResource(R.mipmap.ing32);
            }else if (SPUtil.get(mContext,"userisreal","").equals("1.0")){
                holderDrag.imageViewref.setImageResource(R.mipmap.ed32);
            }else{
                holderDrag.imageViewref.setImageResource(R.mipmap.no32);
            }
        }
        return convertView;
    }


    class HolderDrag{
        ImageView imageView;
        ImageView imageViewref;
        TextView textView;
    }
}
