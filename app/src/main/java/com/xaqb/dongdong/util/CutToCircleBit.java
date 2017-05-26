package com.xaqb.dongdong.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;

/**
 * 绘制圆形图像
 * 流程：
 *     绘制白格子
 *     绘制圆形
 *     取其交集
 *     绘制头像
 */
public class CutToCircleBit {

    public static Bitmap getCircleBit(Bitmap resource){


        int width =resource.getWidth();
        int height = resource.getHeight();
        int min = Math.min(width, height);

        Bitmap bitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);//绘制白的格子
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawCircle(min/2,min/2,min/2,paint);//绘制圆形
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));//取其交集
        canvas.drawBitmap(resource,0,0,paint);//绘制图片
        return bitmap;
    }



}
