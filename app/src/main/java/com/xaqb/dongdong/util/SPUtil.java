package com.xaqb.dongdong.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fl on 2017/5/2.
 */

public class SPUtil {
    public static final String DB = "USER.db";

    /**
     * 存放到偏好设置
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context,String key,Object value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String){
            editor.putString(key,(String) value);
        }else if (value instanceof Integer){
            editor.putInt(key,(Integer) value);
        }else if (value instanceof Boolean){
            editor.putBoolean(key,(Boolean) value);
        }
        editor.commit();
    }

    /**
     * 获取到偏好设置中保存的值
     * @param context
     * @param key
     * @param object
     * @return
     */
    public static Object get(Context context,String key,Object object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB,Activity.MODE_PRIVATE);
        if (object instanceof String){
            return sharedPreferences.getString(key,(String) object);
        }else if (object instanceof Integer){
            return sharedPreferences.getInt(key,(Integer)object);
        }else if (object instanceof Boolean){
            return sharedPreferences.getBoolean(key,(Boolean)object);
        }
        return null;
    }

    /**
     * 移除指定的键
     * @param context
     * @param key
     */
    public static void remove(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

}
