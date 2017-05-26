package com.xaqb.dongdong.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xaqb.dongdong.R;

/**
 * Created by fl on 2017/4/26.
 */

public class LoadingDialog {
    private Context mContext;

    public LoadingDialog(Context context) {
        mContext = context;
    }

    private TextView mTvLoading;
    private Dialog mDialog;
    public Dialog show(String message){
        View view  = LayoutInflater.from(mContext).inflate(R.layout.loading_layout,null);
        mTvLoading = (TextView) view.findViewById(R.id.tv_loading);
        mTvLoading.setText(message);
        if (mDialog == null){
            mDialog = new Dialog(mContext,R.style.transparentFrameWindowStyle);
            mDialog.setContentView(view);
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();
        return mDialog;
    }

    public void dismiss(){
        if (mDialog != null){
            mDialog.dismiss();
        }
    }

}
