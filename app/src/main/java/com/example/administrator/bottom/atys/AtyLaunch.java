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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyLaunch.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
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
//                    Toast.makeText(AtyLaunch.this, R.string.login_already, Toast.LENGTH_LONG).show();
                    Config.loginStatus = 1;
                }
            }, new UploadToken.FailCallback() {
                @Override
                public void onFail() {
//                    Toast.makeText(AtyLaunch.this, R.string.login_notyet, Toast.LENGTH_LONG).show();
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
