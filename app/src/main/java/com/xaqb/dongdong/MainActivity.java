package com.xaqb.dongdong;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.xaqb.dongdong.Adapter.GragAdapter;
import com.xaqb.dongdong.util.CutToCircleBit;
import com.xaqb.dongdong.util.SPUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    private ImageView mIvMsg;
    private ImageView mIvUser;
    private ImageView mIvUserPic;
    private ImageView mIvZxing;
    private TextView mTvQuery;
    private TextView mTvWait;
    private TextView mTvPosting;
    private TextView mTvPosted;
    private TextView mTvKeFu;
    private TextView mTvShare;
    private TextView mTvDo;
    private TextView mTvUser;
    private Button mBtFnished;
    private MainActivity instance;
    private ConvenientBanner mCb;
    private List<Integer> mImageList;
    private ListView mList;
    private DrawerLayout mDraLayout;
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
                    mIvUser.setImageBitmap(CutToCircleBit.getCircleBit(bitmap));
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
        setTitleBarVisibleB(View.GONE);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_drag);
        instance = this;
        mIvMsg = (ImageView) findViewById(R.id.iv_msg_main);
        mIvUserPic = (ImageView) findViewById(R.id.iv_user_drag);
        mIvUser = (ImageView) findViewById(R.id.iv_user_main);
        mIvZxing = (ImageView) findViewById(R.id.iv_zxing_drag);
        mTvQuery = (TextView) findViewById(R.id.tv_query_main);
        mTvWait = (TextView) findViewById(R.id.tv_wait_main);
        mTvPosting = (TextView) findViewById(R.id.tv_posting_main);
        mTvPosted = (TextView) findViewById(R.id.tv_posted_main);
        mTvKeFu = (TextView) findViewById(R.id.tv_kefu_main);
        mTvShare = (TextView) findViewById(R.id.tv_share_main);
        mTvUser = (TextView) findViewById(R.id.tv_user_drag);
        mTvDo = (TextView) findViewById(R.id.tv_do_main);
        mBtFnished = (Button) findViewById(R.id.bt_finish_drag);
        mCb = (ConvenientBanner) findViewById(R.id.cb_main);
        mList = (ListView) findViewById(R.id.list_drag);
        mList.setDivider(new ColorDrawable(Color.GRAY));
        mList.setDividerHeight(1);
        mDraLayout = (DrawerLayout) findViewById(R.id.activity_drag);
    }

    @Override
    public void initAvailable() {
    }

    @Override
    public void initData() {
        mImageList = new ArrayList();
        mImageList.add(R.mipmap.cb1);
        mImageList.add(R.mipmap.cb2);
        mImageList.add(R.mipmap.cb3);
        cbSetPage();
        mCb.startTurning(2000);
        cbItemEvent();
        mTvUser.setText(SPUtil.get(instance,"usernickname","").toString());
        getUserPic();
    }

    @Override
    public void addEvent() {
        mIvMsg.setOnClickListener(instance);
        mIvUser.setOnClickListener(instance);
        mTvQuery.setOnClickListener(instance);
        mTvWait.setOnClickListener(instance);
        mTvPosting.setOnClickListener(instance);
        mTvPosted.setOnClickListener(instance);
        mTvKeFu.setOnClickListener(instance);
        mTvShare.setOnClickListener(instance);
        mTvDo.setOnClickListener(instance);
        mIvZxing.setOnClickListener(instance);
        mBtFnished.setOnClickListener(instance);
        mList.setAdapter(new GragAdapter(instance));
        itemEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_user_main://用户
                mDraLayout.openDrawer(Gravity.LEFT);//打开抽屉
                break;
            case R.id.iv_msg_main://消息
                intentB(instance,MessageActivity.class);
                break;
            case R.id.iv_zxing_drag://生成用户二维码
                if (SPUtil.get(instance,"userisreal","").equals("2.0")){//后期应更改为 已认证：1.0
                    intentB(instance,QRCodeActivity.class);
                }else {
                    showToastB("请进行实名认证");//认证中：2.0  未认证 0.0  认证失败  3.0
                }

                break;
            case R.id.tv_query_main://订单查询
                intentB(instance, QueryActivity.class);
                break;
            case R.id.tv_wait_main://代取件
                intentB(instance, OrderActivity.class,"1");
                break;
            case R.id.tv_posting_main://运送中
                intentB(instance, OrderActivity.class,"2");
                break;
            case R.id.tv_posted_main://已签收
                intentB(instance, OrderActivity.class,"3");
                break;
            case R.id.tv_kefu_main://客服中心
                intentB(instance, ServiceActivity.class);
                break;
            case R.id.tv_share_main://召集伙伴
                intentB(instance, ShareActivity.class);
                break;
            case R.id.tv_do_main://我要下单
                intentB(instance, DoActivity.class);
                break;
            case R.id.bt_finish_drag://退出
                SPUtil.put(instance,"password","");//清除偏好设置中的密码
                SPUtil.put(instance,"FirstIn",true);
                intentB(instance, LoginActivity.class);
                finish();
                break;
        }
    }


    /**
     * 轮播图holder
     */
    public class CbHolder implements Holder<Integer>{

        private ImageView pImg;
        @Override
        public View createView(Context context) {
            pImg = new ImageView(context);
            pImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return pImg;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            pImg.setImageResource(data);
        }
    }


    /**
     * 轮播图设置图片
     */
    public void cbSetPage(){
        mCb.setPages(new CBViewHolderCreator<CbHolder>() {
            @Override
            public CbHolder createHolder() {
                return new CbHolder();
            }
        },mImageList)
         .setPageIndicator(new int[] {R.mipmap.pointn,R.mipmap.pointc})
         .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_LEFT);
    }


    /**
     * 轮播图子条目的点击事件
     */
    public void cbItemEvent(){
        mCb.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showToastB("这是第"+position+"图片");
            }
        });
    }

    /**
     * 子条目的点击事件
     */
   public void itemEvent(){
       mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               switch (position){
                   case 0://我的钱包
                       intentB(instance,MoneyActivity.class);
                       break;
                   case 1://我的订单
                       intentB(instance,OrderActivity.class,"0");
                       break;
                   case 2://管理地址
                       intentB(instance,AddressActivity.class);
                       break;
                   case 3://关于咚咚
                       intentB(instance,ServiceActivity.class);
                       break;
                   case 4://修改设置
                       intentB(instance,ModifyActivity.class);
                       break;
                   case 5://实名认证
                       /*if (SPUtil.get(instance,"userisreal","").equals("2.0")){
                           showToastB("认证中...");
                       }else if (SPUtil.get(instance,"userisreal","").equals("1.0")){
                           showToastB("已认证");
                       }else{
                           intentB(instance,ApproveActivity.class);
                       }*/
                           intentB(instance,ApproveActivity.class);
                       break;
               }
           }
       });
   }


    /**
     * 获取用户的头像Bitmap
     */
    public void getUserPic() {

        String url ;
        //1.创建一个okhttpclient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
        if (SPUtil.get(instance,"userheadpic","").toString().equals("")){
            url = "http://www.qbdongdong.com/uploads/20170309/d9448f452b3a54e7e87d22c8654ebb92.jpg";
        }else{
            url = SPUtil.get(instance,"userheadpic","").toString();
        }
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