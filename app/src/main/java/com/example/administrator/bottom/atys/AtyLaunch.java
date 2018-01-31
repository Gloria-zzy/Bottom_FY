package com.example.administrator.bottom.atys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.net.UploadToken;

import static com.example.administrator.bottom.Config.APP_ID;
import static com.example.administrator.bottom.Config.REQUEST_READ_PHONE_STATE;

public class AtyLaunch extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //加载启动图片
        setContentView(R.layout.aty_launch);
        Integer time = 3000;    //设置等待时间，单位为毫秒

        //---------------------状态栏透明 begin----------------------------------------
        Window window = AtyLaunch.this.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.TRANSPARENT);

        ViewGroup mContentView = (ViewGroup) AtyLaunch.this.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
        //---------------------状态栏透明 end----------------------------------------

        // 判断该用户是否已经登录
        {
            // 获得token
            SharedPreferences sharedPreferences = getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
            String token = sharedPreferences.getString(Config.KEY_TOKEN, "");

            // 用UploadToken类上传token，并处理返回值
            new UploadToken(token, new UploadToken.SuccessCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AtyLaunch.this, R.string.login_already, Toast.LENGTH_LONG).show();
                    Config.loginStatus = 1;
                }
            }, new UploadToken.FailCallback() {
                @Override
                public void onFail() {
                    Toast.makeText(AtyLaunch.this, R.string.login_notyet, Toast.LENGTH_LONG).show();
                    Config.loginStatus = 0;
                }
            });
        }


        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(AtyLaunch.this, AtyMainFrame.class);
                intent.putExtra("page", "home");
                startActivity(intent);
                AtyLaunch.this.finish();
            }
        }, time);
    }
}
