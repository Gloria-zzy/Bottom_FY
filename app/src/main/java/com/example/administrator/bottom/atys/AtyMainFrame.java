package com.example.administrator.bottom.atys;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.application.MainApplication;
import com.example.administrator.bottom.frag.FragCommunity;
import com.example.administrator.bottom.frag.FragHome;
import com.example.administrator.bottom.frag.FragMe;
import com.example.administrator.bottom.frag.FragOrder;
import com.example.administrator.zxinglibrary.android.CaptureActivity;
import com.example.administrator.zxinglibrary.bean.ZxingConfig;
import com.example.administrator.zxinglibrary.common.Constant;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import static com.example.administrator.bottom.Config.REQUEST_READ_PHONE_STATE;

/**
 * Created by Administrator on 2017/10/31.
 */

public class AtyMainFrame extends Activity implements View.OnClickListener {

    private LinearLayout tabHome;
    private LinearLayout tabOrder;
    private LinearLayout tabCommunity;
    private LinearLayout tabMe;

    private FrameLayout ly_content;

    private Fragment[] fragments = new Fragment[4];
    private FragHome fragHome;
    private FragOrder fragOrder;
    private FragCommunity fragCommunity;
    private FragMe fragMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        MainApplication.setMainActivity(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bindView();

        //---------------------状态栏透明 begin----------------------------------------
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyMainFrame.this.getWindow();
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

//        getSupportActionBar().hide();

        //      load page!!!
//        showFragHome();
        Intent intent = getIntent();
        setIntent(intent);
        String page = intent.getStringExtra("page");
        bindView();

//        Toast.makeText(AtyMainFrame.this, page, Toast.LENGTH_LONG).show();
        if (page != null) {
            if (page.equals("home")) {
                showFragHome();
            } else if (page.equals("order")) {
                showFragOrder();
            } else if (page.equals("community")) {
                showFragCommunity();
            } else if (page.equals("me")) {
                showFragMe();
            } else {
                System.out.println("hellooooooooo:" + page + "1111");
                showFragHome();
            }
        }

        // 申请 读取手机状态 权限

        AndPermission.with(this)
                .permission(Manifest.permission.READ_PHONE_STATE).callback(new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {

                String permission = Manifest.permission.READ_PHONE_STATE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int i = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
                    if (i != PackageManager.PERMISSION_GRANTED) {

                    } else {
                        CloudPushService pushService = PushServiceFactory.getCloudPushService();
                        String deviceId = pushService.getDeviceId();
                        Config.cacheDeviceID(AtyMainFrame.this, deviceId);
//                        Toast.makeText(AtyMainFrame.this, deviceId, Toast.LENGTH_LONG).show();
                        System.out.println("DEVICE_ID + !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(deviceId);
                    }
                }
            }

            @Override
            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                System.out.println("-------------------------------no permission--------------------------------");
            }

        }).start();


//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String DEVICE_ID = tm.getDeviceId();
//        System.out.println("DEVICE_ID + !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(DEVICE_ID);
    }

    //UI组件初始化与事件绑定
    private void bindView() {
//        topBar = (TextView)this.findViewById(R.id.txt_top);
        tabHome = (LinearLayout) this.findViewById(R.id.txt_home);
        tabOrder = (LinearLayout) this.findViewById(R.id.txt_get);
        tabCommunity = (LinearLayout) this.findViewById(R.id.txt_community);
        tabMe = (LinearLayout) this.findViewById(R.id.txt_me);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        tabHome.setOnClickListener(this);
        tabOrder.setOnClickListener(this);
        tabCommunity.setOnClickListener(this);
        tabMe.setOnClickListener(this);

    }

    //重置所有文本的选中状态
    public void selected() {
        tabHome.setSelected(false);
        tabOrder.setSelected(false);
        tabCommunity.setSelected(false);
        tabMe.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (fragHome != null) {
            transaction.hide(fragHome);
        }
        if (fragOrder != null) {
            transaction.hide(fragOrder);
        }
        if (fragCommunity != null) {
            transaction.hide(fragCommunity);
        }
        if (fragMe != null) {
            transaction.hide(fragMe);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.txt_home:
                selected();
                tabHome.setSelected(true);
                if (fragHome == null) {
                    fragHome = new FragHome();
//                    fragHome.fresh();
                    transaction.add(R.id.fragment_container, fragHome);
                } else {
//                    fragHome.fresh();
                    transaction.show(fragHome);
                }
                break;

            case R.id.txt_get:
                selected();
                tabOrder.setSelected(true);
                if (fragOrder == null) {
                    fragOrder = new FragOrder();
//                    fragOrder.fresh();
                    transaction.add(R.id.fragment_container, fragOrder);
                } else {
//                    fragOrder.fresh();
                    transaction.show(fragOrder);

                }
                break;

            case R.id.txt_community:
                selected();
                tabCommunity.setSelected(true);
                if (fragCommunity == null) {
                    fragCommunity = new FragCommunity();
//                    fragCommunity.fresh();
                    transaction.add(R.id.fragment_container, fragCommunity);
                } else {
//                    fragCommunity.fresh();
                    transaction.show(fragCommunity);

                }
                break;

            case R.id.txt_me:
                selected();
                tabMe.setSelected(true);
                if (fragMe == null) {
                    fragMe = new FragMe();
                    transaction.add(R.id.fragment_container, fragMe);
                } else {
                    transaction.show(fragMe);
                }
                break;
        }

        transaction.commit();
    }

    public static void close() {

    }

    public void showFragHome() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();
        tabHome.setSelected(true);
        fragHome = new FragHome();
        transaction.add(R.id.fragment_container, fragHome);
        transaction.show(fragHome);
        transaction.commit();
        fragHome.fresh();
    }

    public void showFragOrder() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();
        tabOrder.setSelected(true);
        fragOrder = new FragOrder();
        transaction.add(R.id.fragment_container, fragOrder);
        transaction.show(fragOrder);
        transaction.commit();
    }

    public void showFragCommunity() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();
        tabCommunity.setSelected(true);
        fragCommunity = new FragCommunity();
        transaction.add(R.id.fragment_container, fragCommunity);
        transaction.show(fragCommunity);
        transaction.commit();
    }

    public void showFragMe() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();
        tabMe.setSelected(true);
        fragMe = new FragMe();
        transaction.add(R.id.fragment_container, fragMe);
        transaction.show(fragMe);
        transaction.commit();
    }

    public void appendConsoleText(String text) {
        Log.i("MainFrame", text);
        Toast.makeText(AtyMainFrame.this, text, Toast.LENGTH_LONG).show();
    }
}
