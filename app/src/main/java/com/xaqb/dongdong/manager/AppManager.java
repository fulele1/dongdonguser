package com.xaqb.dongdong.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by lenovo on 2017/4/17.
 */

public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager instance;

    public AppManager() {
        mActivityStack = new Stack<>();
    }

    /**
     * 单例模式 饿汉式
     * @return
     */
    public static AppManager getAppManager(){
        if (instance == null){
            instance = new AppManager();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        mActivityStack.add(activity);
    }

}
