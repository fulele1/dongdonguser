package com.xaqb.dongdong;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xaqb.dongdong.impl.OnOkDataFinishedListener;
import com.xaqb.dongdong.manager.AppManager;
import com.xaqb.dongdong.view.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private LinearLayout mLyTitle;//标题布局
    private FrameLayout mLyContent;//内容布局
    private TextView mTvBack;//返回键
    private TextView mTvTitle;//标题内容
    private Toast mToast;//吐司
    private AlertDialog.Builder mDialog;//对话框
    private Intent mIntent;
    public LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppManager.getAppManager().addActivity(this);
        mContext = this;
        mLoadingDialog = new LoadingDialog(mContext);
        setUpViewsB();
        initTitle();
        initView();
        initAvailable();
        initData();
        addEvent();
    }




    /**
     * 初始化标题栏
     */
    public abstract void initTitle();

    /**
     * 初始化view
     */
    public abstract void initView();

    /**
     * 初始化携带跳转的数据
     */
    public abstract void initAvailable();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 添加事件
     */
    public abstract void addEvent();

    /**
     * 初始化标题栏和内容界面
     */
    public void setUpViewsB() {
        super.setContentView(R.layout.activity_base);
        mLyTitle = (LinearLayout) findViewById(R.id.ly_title_bar);
        mLyContent = (FrameLayout) findViewById(R.id.ly_content_base);
        mTvBack = (TextView) findViewById(R.id.tv_back_title);
        mTvTitle = (TextView) findViewById(R.id.tv_title_tilte);
    }

    /**
     * 重新渲染
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mLyContent.removeAllViews();
        View.inflate(this, layoutResID, mLyContent);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        mLyContent.removeAllViews();
        mLyContent.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mLyContent.removeAllViews();
        mLyContent.addView(view, params);
        onContentChanged();
    }

    /**
     * 设置标题栏可见
     *
     * @param visible
     */
    public void setTitleBarVisibleB(int visible) {
        mLyTitle.setVisibility(visible);
    }

    /**
     * 设置标题栏不可见
     *
     * @param gone
     */
    public void setTitleBarGoneB(int gone) {
        mLyTitle.findViewById(gone);
    }

    /**
     * 设置标题栏内容
     *
     * @param title
     */
    public void setTitleB(String title) {
        mTvTitle.setText(title);
    }

    /**
     * 设置返回按钮不可见
     *
     * @param invisible
     */
    public void setBackVisibleB(int invisible) {
        mTvBack.setVisibility(invisible);
    }

    /**
     * 点击返回按钮
     *
     * @param view
     */
    public void onBack(View view) {
        finish();
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
    }

    /**
     * 弹出对话框
     */
    public void showToastB(String str) {
        if (null == mToast) {
            mToast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }

    /**
     * 弹出普通对话框
     *
     * @param tit
     * @param icon
     * @param mes
     * @param ok
     * @param no
     */
    public AlertDialog.Builder showDialogB(Context context, String tit, int icon, String mes, String ok, String no) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setIcon(icon);
        mDialog.setTitle(tit);
        mDialog.setMessage(mes);
        mDialog.setPositiveButton(ok, new DialogEvent());
        mDialog.setNegativeButton(no, new DialogEvent());
        mDialog.show();
        return mDialog;
    }


    public AlertDialog.Builder showDialogB(Context context,String [] item){
        mDialog = new AlertDialog.Builder(context);
        mDialog.setItems(item,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogItemEventB(which);

            }
        });
        mDialog.show();
        return mDialog;
    }

    /**
     * 对话框的子条目的点击事件
     * @param witch
     */
    public void dialogItemEventB(int witch){
    }


    class DialogEvent implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    dialogOkB();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    dialogNoB();
                    break;
            }
        }
    }

    /**
     * 对话框确定键
     */
    public void dialogOkB() {
    }

    /**
     * 对话框取消键
     */
    public void dialogNoB() {
    }

    /**
     * 普通的界面的跳转
     *
     * @param context
     * @param cla
     */
    public void intentB(Context context, Class<?> cla) {
        mIntent = new Intent(context, cla);
        context.startActivity(mIntent);
    }

    /**
     * 携带参数界面的跳转
     *
     * @param context
     * @param cla
     */
    public void intentB(Context context, Class<?> cla, String string) {
        mIntent = new Intent(context, cla);
        mIntent.putExtra("intentData", string);
        context.startActivity(mIntent);
    }



    /**
     * 需要请求结果的界面的跳转
     * @param context
     * @param cla
     * @param code
     */
    public void intentB(Context context,Class<?> cla,int code){
        mIntent = new Intent(context, cla);
        startActivityForResult(mIntent,code);
    }


    /**
     * 携带请求数据跳转界面
     * @param context
     * @param cla
     * @param key
     * @param value
     */
    public void intentB(Context context,Class<?> cla,String key,String value){
        mIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(key,value);
        mIntent.putExtras(bundle);
        setResult(RESULT_OK,mIntent);
        finish();
    }


    /**
     * 相机
     * @param code
     */
    public void intentB(int code){
        mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(mIntent,code);
    }

    /**
     * 图库
     * @param code
     * @param a
     */
    public void intentB(int code,String a){
        mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mIntent,code);
    }


    /**
     * GET进行网络请求
     * @param url
     */
    public void okGetConnectionB(String url){
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int i) {
                        mLoadingDialog.dismiss();
                        showToastB("网络连接失败");
                    }

                    @Override
                    public void onResponse(String string, int i) {
                        mLoadingDialog.dismiss();
                        mOnOkDataFinishedListener.okDataFinishedListener(string);
                    }
                });
    }



    public void okPostConnectionB(String url, String p1,String v1, String p2,String v2, String p3,String v3){
        OkHttpUtils
                .post()
                .url(url)
                .addParams(p1,v1)
                .addParams(p2,v2)
                .addParams(p3,v3)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int i) {
                        //请求出错
                        mLoadingDialog.dismiss();
                        showToastB("网络连接异常");
                    }

                    @Override
                    public void onResponse(String string, int i) {
                        //请求成功
                        mOnOkDataFinishedListener.okDataFinishedListener(string);
                    }
                });

    }


    /**
     * 回调接口中注册监听事件
     * @param onOkDataFinishedListener
     */
    OnOkDataFinishedListener mOnOkDataFinishedListener;
    public void setOnDataFinishedListener(OnOkDataFinishedListener onOkDataFinishedListener){
        mOnOkDataFinishedListener = onOkDataFinishedListener;
    }


    public String getVersionName() {
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);

            // 当前应用的版本名称
            return info.versionName;

        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}