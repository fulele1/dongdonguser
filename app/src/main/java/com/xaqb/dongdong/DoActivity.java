package com.xaqb.dongdong;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.xaqb.dongdong.util.GsonUtil;
import com.xaqb.dongdong.util.HttpUrlUtils;
import com.xaqb.dongdong.util.LogUtil;
import com.xaqb.dongdong.util.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

public class DoActivity extends BaseActivity {
    private DoActivity instance;
    private TextView mTvPost;
    private ImageView mIvPostAdd;
    private ImageView mIvReceiveAdd;
    private TextView mTvReceive;
    private TextView mTvVariety;
    private TextView mTvWeight;
    private ImageView mIvPicOne;
    private ImageView mIvPictwo;
    private ImageView mIvPicThree;
    private EditText mEtGetTime;
    private EditText mEtNote;
    private Button mBtFinished;
    private Spinner mSpinner;
    private String flag;
    private RadioGroup mRp;
    private float otherCheck;
    private boolean oneHave;
    private boolean twoHave;
    private boolean threeHave;

    @Override
    public void initTitle() {
        setTitleB("下单");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_do);
        instance = this;
        mTvPost = (TextView) findViewById(R.id.tv_post_do);
        mIvPostAdd = (ImageView) findViewById(R.id.iv_post_choose_do);
        mIvReceiveAdd = (ImageView) findViewById(R.id.iv_receive_choose_do);
        mTvReceive = (TextView) findViewById(R.id.tv_receive_do);
        mTvVariety = (TextView) findViewById(R.id.tv_variety_do);
        mTvWeight = (TextView) findViewById(R.id.tv_weight_do);
        mIvPicOne = (ImageView) findViewById(R.id.iv_pic_one_do);
        mIvPictwo = (ImageView) findViewById(R.id.iv_pic_two_do);
        mIvPicThree = (ImageView) findViewById(R.id.iv_pic_three_do);
        mEtGetTime = (EditText) findViewById(R.id.et_get_time_do);
        mEtNote = (EditText) findViewById(R.id.et_note_do);
        mBtFinished = (Button) findViewById(R.id.bt_finished_do);
        mSpinner = (Spinner) findViewById(R.id.spinner_do);
        mRp = (RadioGroup) findViewById(R.id.rp_do);
    }

    @Override
    public void initAvailable() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void addEvent() {
        mIvPostAdd.setOnClickListener(instance);
        mIvReceiveAdd.setOnClickListener(instance);
        mIvPicOne.setOnClickListener(instance);
        mIvPictwo.setOnClickListener(instance);
        mIvPicThree.setOnClickListener(instance);
        mBtFinished.setOnClickListener(instance);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String str = tv.getText().toString();
                switch (str) {
                    case "食品":
                        mTvVariety.setText("03");
                        break;
                    case "生活用品":
                        mTvVariety.setText("05");
                        break;
                    case "服饰":
                        mTvVariety.setText("02");
                        break;
                    case "文件":
                        mTvVariety.setText("01");
                        break;
                    case "数码用品":
                        mTvVariety.setText("04");
                        break;
                    case "其他":
                        mTvVariety.setText("06");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mRp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_yes_do://可以代签
                        otherCheck = 2;
                        break;
                    case R.id.rb_no_do://不可代签
                        otherCheck = 1;
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_post_choose_do://收件人地址
                intentB(instance,AddressActivity.class,0);
                break;
            case R.id.iv_receive_choose_do://寄件人地址
                intentB(instance,AddressActivity.class,1);
                break;
            case R.id.iv_pic_one_do://第一张图片
                 flag = "ONE";
                showDialogB(instance,new String[]{"拍照","相册"});
                break;
            case R.id.iv_pic_two_do://第二张图片
                flag = "TWO";
                showDialogB(instance,new String[]{"拍照","相册"});
                break;
            case R.id.iv_pic_three_do://第三张图片
                flag = "THREE";
                showDialogB(instance,new String[]{"拍照","相册"});
                break;
            case R.id.bt_finished_do://下单
                doOrder();
                break;
        }
    }


    /**
     * 下单
     */
    public void doOrder(){
        String post = mTvPost.getText().toString().trim();
        String receive = mTvReceive.getText().toString().trim();
        String variety = mTvVariety.getText().toString().trim();
        String weight = mTvWeight.getText().toString().trim();
        String getTime = mEtGetTime.getText().toString().trim();
        String note = mEtNote.getText().toString().trim();
        if ("请编辑寄件人地址".equals(post)){
            showToastB("请填写寄件人地址");
            return;
        }else if ("请编辑收件人地址".equals(receive)){
            showToastB("请填写收件人地址");
            return;
        }else if ("".equals(variety)){
            showToastB("请选择物品类型");
            return;
        }

        mLoadingDialog.show("正在下单");
        OkHttpUtils
                .post()
                .url(HttpUrlUtils.getHttpUrl().doOrder())
                .addParams("uid", SPUtil.get(instance,"userid","").toString())
                .addParams("senduaid",post)
                .addParams("recuaid",receive)
                .addParams("category",variety)
                .addParams("weight",weight)
                .addParams("stuffimg",getTime)
                .addParams("remark",note)
                .addParams("signin ",otherCheck+"")//1本人签收，2他人代收    签收人
                .addParams("source",2+"")//1:web 2:Android 3:ios   下单来源
                .addParams("lon","")
                .addParams("lat","")
                .addParams("iscoupons","")//0不用，1使用     是否使用优惠券
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        mLoadingDialog.dismiss();
                        showToastB(e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        mLoadingDialog.dismiss();
                        Map<String,Object > map = GsonUtil.GsonToMaps(s);
                        if (map.get("state").toString().equals("0.0")&&map.get("mess").toString().equals("success")){
                            showToastB("上传成功");
                        }
                    }
                });
    }

    @Override
    public void dialogItemEventB(int witch) {
        switch (witch){
            case 0:
                intentB(2);//相册
                break;
            case 1:
                intentB(3,"");//相机
                break;
        }
    }


    /**
     * 跳转界面返回的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0&&resultCode == RESULT_OK){//收件人地址
            result(data,0);
        }else if (requestCode == 1&&resultCode == RESULT_OK){//寄件人地址
            result(data,1);
        }else if (requestCode == 2&&resultCode == RESULT_OK){//相机
            result(data,2);
        }else if (requestCode == 3&&resultCode == RESULT_OK){//相册
            result(data,3);
        }
    }


    /**
     * 获取返回来的地址
     * @param data
     * @param requestCode
     */
    public void result(Intent data,int requestCode){

        Bundle bundle = data.getExtras();
        if (bundle !=null){
            String  add = bundle.getString("add");
            if (requestCode == 0){//收件人地址
            mTvPost.setText(add);
            }else if (requestCode == 1){//寄件人地址
            mTvReceive.setText(add);
            }else if (requestCode == 2){//相机
                Bitmap bitmap = (Bitmap) bundle.get("data");
                switch (flag) {
                    case "ONE":
                        mIvPicOne.setImageBitmap(bitmap);
                        oneHave = true;
                        break;
                    case "TWO":
                        mIvPictwo.setImageBitmap(bitmap);
                        twoHave = true;
                        break;
                    case "THREE":
                        mIvPicThree.setImageBitmap(bitmap);
                        threeHave = true;
                        break;
                }
            }else if (requestCode == 3){//图库
                Uri uri = data.getData();
                switch (flag){
                    case "ONE":
                        mIvPicOne.setImageURI(uri);
                        oneHave = true;
                        break;
                    case "TWO":
                        mIvPictwo.setImageURI(uri);
                        twoHave = true;
                        break;
                    case "THREE":
                        mIvPicThree.setImageURI(uri);
                        threeHave = true;
                        break;
                }
            }
        }
    }
}