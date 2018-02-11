package com.example.administrator.bottom.atys;

import android.Manifest;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
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
import com.example.administrator.bottom.ui.FragChatMainActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class AtyMainFrame extends FragmentActivity implements View.OnClickListener {

    private LinearLayout tabHome;
    private LinearLayout tabOrder;
    private LinearLayout tabCommunity;
    private LinearLayout tabMe;

    private FrameLayout ly_content;

    private Fragment[] fragments = new Fragment[4];
    private FragHome fragHome;
    private FragOrder fragOrder;
    private FragChatMainActivity fragCommunity;
    private FragMe fragMe;

    // 用来在log输出中标志这个Activity的信息
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        MainApplication.setMainActivity(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bindView();

        //---------------------状态栏透明 begin----------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

        // 获取携带传入参数的intent实体
        Intent intent = getIntent();
        setIntent(intent);
        bindView();

        // page是传入参数，提示应该显示哪个界面
        String page = intent.getStringExtra("page");
        if (page != null) {
            if (page.equals("home")) {
                Log.i(TAG, "page home");
                showFragHome();
            } else if (page.equals("order")) {
                Log.i(TAG, "page order");
                showFragOrder();
            } else if (page.equals("community")) {
                Log.i(TAG, "page community");
                showFragCommunity();
            } else if (page.equals("me")) {
                Log.i(TAG, "page me");
                showFragMe();
            } else {
                // page不为空但是不符合上述任何选项，那么默认显示Home页面
                Log.i(TAG, "page not null but is empty");
                showFragHome();
            }
        } else {
            // page为空，默认显示Home页面
            Log.i(TAG, "page is null");
            showFragHome();
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
                        Log.i(TAG, "AliPush deviceID:" + deviceId);
                    }
                }
            }

            @Override
            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                Log.i(TAG, "-------------------------------no permission of read_phone_state--------------------------------");
            }

        }).start();

    }

    //UI组件初始化与事件绑定
    private void bindView() {
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
    public void clearSelected() {
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

    // 当点击主界面上的fragment标签时显示相应fragment
    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.txt_home:
                // 清除所有frag的选中状态，全部设置为false（先前选中的frag只可能有一个，但无法预知是哪一个，因此全部清除）
                clearSelected();
                // 设置点击的frag的状态为选中
                tabHome.setSelected(true);
                // 如果选中的frag已经实例化，就跳转（transaction）到这个实例上，如果没有实例化就新建一个实例
                if (fragHome == null) {
                    fragHome = new FragHome();
                    transaction.add(R.id.fragment_container, fragHome);
                } else {
                    transaction.show(fragHome);
                }
                break;

            case R.id.txt_get:
                clearSelected();
                tabOrder.setSelected(true);
                if (fragOrder == null) {
                    fragOrder = new FragOrder();
                    transaction.add(R.id.fragment_container, fragOrder);
                } else {
                    transaction.show(fragOrder);
                }
                break;

            case R.id.txt_community:
                clearSelected();
                tabCommunity.setSelected(true);
                if (fragCommunity == null) {
                    fragCommunity = new FragChatMainActivity();
                    transaction.add(R.id.fragment_container, fragCommunity);
                } else {
                    transaction.show(fragCommunity);
                }
                break;

            case R.id.txt_me:
                clearSelected();
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

    // 用于在本activity生成时指定显示的Fragment，生成时由于没有输入所以无法触发onClick方法，通过page参数指定显示的Fragment
    public void showFragHome() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        clearSelected();
        tabHome.setSelected(true);
        fragHome = new FragHome();
        transaction.add(R.id.fragment_container, fragHome);
        transaction.show(fragHome);
        transaction.commit();
        fragHome.fresh();
    }

    public void showFragOrder() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        clearSelected();
        tabOrder.setSelected(true);
        fragOrder = new FragOrder();
        transaction.add(R.id.fragment_container, fragOrder);
        transaction.show(fragOrder);
        transaction.commit();
    }

    public void showFragCommunity() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        clearSelected();
        tabCommunity.setSelected(true);
        fragCommunity = new FragChatMainActivity();
        transaction.add(R.id.fragment_container, fragCommunity);
        transaction.show(fragCommunity);
        transaction.commit();
    }

    public void showFragMe() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        clearSelected();
        tabMe.setSelected(true);
        fragMe = new FragMe();
        transaction.add(R.id.fragment_container, fragMe);
        transaction.show(fragMe);
        transaction.commit();
    }

    // 用于被MainApplication的setConsoleText调用，MainApplication的setConsoleText被MyMessageIntentService调用，用于MyMessageIntentService在AtyMainFrame中输出信息
    public void appendConsoleText(String text) {
        Log.i(TAG, text);
        Toast.makeText(AtyMainFrame.this, text, Toast.LENGTH_LONG).show();
    }
}
