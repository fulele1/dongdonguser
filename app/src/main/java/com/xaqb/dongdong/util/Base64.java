/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.xaqb.dongdong.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Base64 工具�?
 */
public final class Base64 {

    public static String encode(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return android.util.Base64.encodeToString(appicon, android.util.Base64.DEFAULT);
    }
}
