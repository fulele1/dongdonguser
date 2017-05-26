package com.xaqb.dongdong;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.xaqb.dongdong.util.QRCodeUtil;

import java.io.File;


public class QRCodeActivity extends BaseActivity {

    private ImageView mIvQRCode;

    @Override
    public void initTitle() {
        setTitleBarVisibleB(View.GONE);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_qrcode);
        mIvQRCode = (ImageView) findViewById(R.id.iv_qr_qrcode);
    }

    @Override
    public void initAvailable() {

    }

    @Override
    public void initData() {

        Intent intent = getIntent();
        String  user = intent.getStringExtra("");
        String  ide = intent.getStringExtra("");

        final String filePath = getFileRoot(this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";

        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage("付乐", 800, 800, BitmapFactory.decodeResource(getResources(), R.mipmap.icon128), filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIvQRCode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public void addEvent() {

    }


    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

}
