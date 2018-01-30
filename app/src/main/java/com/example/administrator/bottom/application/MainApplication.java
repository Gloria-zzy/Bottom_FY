package com.example.administrator.bottom.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.example.administrator.bottom.atys.AtyMainFrame;

public class MainApplication extends Application {
    private static final String TAG = "Init";
    private static AtyMainFrame mainActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        initCloudChannel(this);
    }
    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    public static void setMainActivity(AtyMainFrame activity) {
        mainActivity = activity;
    }

    public static void setConsoleText(String text) {
        if (mainActivity != null && text != null) {
            mainActivity.appendConsoleText(text);
        }
    }
}