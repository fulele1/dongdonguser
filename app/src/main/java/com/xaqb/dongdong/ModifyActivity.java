package com.xaqb.dongdong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xaqb.dongdong.util.CutToCircleBit;
import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyActivity extends BaseActivity {

    private TextView mTvPhone;
    private EditText mEdNickname;
    private ImageView mIvUserPic;
    private ImageView mIvPrompt;
    private ImageView mIvMPsw;
    private Button mBtFnished;
    private ModifyActivity instance;
    private Bitmap mBitmap;
    private boolean isOpen;
    private static final int SUCCESS = 1;
    private static final int FALL = 2;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //加载网络成功进行UI的更新,处理得到的图片资源
                case SUCCESS:
                    //通过message，拿到字节数组
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    //通过imageview，设置图片
                    mIvUserPic.setImageBitmap(CutToCircleBit.getCircleBit(bitmap));
                    break;
                //当加载网络失败执行的逻辑代码
                case FALL:
                    showToastB("网络出现了问题");
                    break;
            }
        }
    };


    @Override
    public void initTitle() {
        setTitleB("修改设置");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_modify);
        instance = this;
        mTvPhone = (TextView) findViewById(R.id.tv_phone_modify);
        mEdNickname = (EditText) findViewById(R.id.tv_nickname_modify);
        mIvUserPic = (ImageView) findViewById(R.id.iv_pic_modify);
        mIvPrompt = (ImageView) findViewById(R.id.iv_prompt_modify);
        mIvMPsw = (ImageView) findViewById(R.id.iv_mpsw_modify);
        mBtFnished = (Button) findViewById(R.id.bt_finished_modify_set);
    }

    @Override
    public void initAvailable() {
    }

    @Override
    public void initData() {
        mTvPhone.setText(SPUtil.get(instance,"usermp","").toString());
        mEdNickname.setText(SPUtil.get(instance,"usernickname","").toString());
        getUserPic();
    }

    @Override
    public void addEvent() {
        mIvUserPic.setOnClickListener(instance);
        mIvPrompt.setOnClickListener(instance);
        mIvMPsw.setOnClickListener(instance);
        mBtFnished.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_pic_modify://头像切换
                showDialogB(instance,new String[]{"拍照","相册"});
                break;
            case R.id.iv_prompt_modify://提示音效
                if (!isOpen){
                    isOpen = true;
                    mIvPrompt.setImageResource(R.mipmap.on64);
                }else{
                    isOpen = false;
                    mIvPrompt.setImageResource(R.mipmap.off64);
                }
                break;
            case R.id.iv_mpsw_modify://更改密码
                    intentB(instance,ModifyPSWActivity.class);
                break;

            case R.id.bt_finished_modify_set://完成修改
                finishedModify();
                break;
        }
    }


    /**
     * 更改信息
     */
    public void finishedModify(){
        Map<String, String> map = new HashMap<>();
        map.put("access_token", SPUtil.get(instance,"access_token","").toString());
        map.put("usernickname", mEdNickname.getText().toString().trim());//昵称
        map.put("userqq", "");//qq
        map.put("usermp", "");//电话
        map.put("userheadpic", "");//头像
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtil.GsonString(map));

        OkHttpUtils
                .put()
                .url(HttpUrlUtils.getHttpUrl().modifyMess()+ SPUtil.get(instance,"userid","")+".json?")
                .requestBody(body)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        showToastB("请求失败");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        LogUtil.i(s);
                        Map<String,Object> map = GsonUtil.GsonToMaps(s);
                        LogUtil.i(map.toString());
                        if (map.get("state").toString().equals("1.0")) {
                            showToastB(map.get("mess").toString());
                            return;
                        }
                        showToastB("修改成功");
                        finish();
                    }
                });
    }

    @Override
    public void dialogItemEventB(int witch) {
        switch (witch){
            case 0://相机
                intentB(2);
                break;
            case 1://相册
                intentB(3,"");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 2&& data != null){//相机
            Bundle bundle = data.getExtras();
            mBitmap = (Bitmap) bundle.get("data");
            mIvUserPic.setImageBitmap(CutToCircleBit.getCircleBit(mBitmap));

        }else if(requestCode == 3&& data != null){//图库
            Uri uri = data.getData();

            try {
                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(instance.getContentResolver(), uri);
                mIvUserPic.setImageBitmap(CutToCircleBit.getCircleBit(mBitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取用户的头像Bitmap
     */
    public void getUserPic() {
        String url;
        if (SPUtil.get(instance,"userheadpic","").toString().equals("")){
            url = "http://www.qbdongdong.com/uploads/20170309/d9448f452b3a54e7e87d22c8654ebb92.jpg";
        }else{
            url = SPUtil.get(instance,"userheadpic","").toString();
        }

        //1.创建一个okhttpclient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
        Request request = new Request.Builder()
                .url(url)
                .build();
        //3.创建一个Call对象，参数是request对象，发送请求
        Call call = okHttpClient.newCall(request);
        //4.异步请求，请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到从网上获取资源，转换成我们想要的类型
                byte[] Picture_bt = response.body().bytes();
                //通过handler更新UI
                Message message = handler.obtainMessage();
                message.obj = Picture_bt;
                message.what = SUCCESS;
                handler.sendMessage(message);
            }
        });
    }
}