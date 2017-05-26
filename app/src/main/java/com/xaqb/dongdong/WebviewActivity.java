package com.xaqb.dongdong;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xaqb.dongdong.util.LogUtil;

public class WebviewActivity extends BaseActivity {
    private WebView mWeb;
    private WebviewActivity instance;


    @Override
    public void initTitle() {
        setTitleB("查询");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_webview);
        instance = this;
        mWeb = (WebView) findViewById(R.id.wb_web_web);
        setmWeb();
    }

    public void setmWeb(){
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWeb.getSettings().setSupportZoom(true);
        mWeb.getSettings().setBuiltInZoomControls(true);
        mWeb.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });

        mWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(instance).setTitle("提示").setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).create().show();
                return true;
            }
        });
    }

    @Override
    public void initAvailable() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        LogUtil.i(url);// https://m.kuaidi100.com/index_all.html?type=中通&postid=718821442951
        if (!url.isEmpty()){
            mWeb.loadUrl(url);
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void addEvent() {
    }

    @Override
    public void onClick(View v) {
    }
}
