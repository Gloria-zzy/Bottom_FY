package com.example.administrator.bottom.atys;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.aliyuncs.exceptions.ClientException;
import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.alipush.PushMessage;
import com.example.administrator.bottom.net.CompleteOrder;
import com.example.administrator.zxinglibrary.android.CaptureActivity;
import com.example.administrator.zxinglibrary.bean.ZxingConfig;
import com.example.administrator.zxinglibrary.common.Constant;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;
//        手机号   phone
//        信任好友 trust_friend
//        收货地点 arrive_address
//        收货时间 arrive_time
public class AtyStaffOnly extends AppCompatActivity {

    private ImageView scanner;
    private int REQUEST_CODE_SCAN = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_staff_only);

        getSupportActionBar().hide();

        //---------------------状态栏透明 begin----------------------------------------
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyStaffOnly.this.getWindow();
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

        findViewById(R.id.iv_staff_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.switch_still,R.transition.switch_slide_out_right);
            }
        });

        // 绑定按钮到扫描二维码
        //scanner
        scanner = (ImageView)findViewById(R.id.iv_staff_scanner);
//        result = view.findViewById(R.id.result_tv);
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(AtyStaffOnly.this)
                        .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {

                        Intent intent = new Intent(AtyStaffOnly.this, CaptureActivity.class);

                                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                                * 也可以不传这个参数
                                * 不传的话  默认都为默认不震动  其他都为true
                                * */

                        ZxingConfig config = new ZxingConfig();
                        config.setPlayBeep(false);
                        config.setShake(true);
                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);

                        startActivityForResult(intent, REQUEST_CODE_SCAN);

                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        Uri packageURI = Uri.parse("package:" + AtyStaffOnly.this.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        Toast.makeText(AtyStaffOnly.this, "没有权限无法扫描", Toast.LENGTH_LONG).show();
                    }

                }).start();

            }

        });

    }

    // 处理startActivityForResult的返回值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imgReturnIntent) {
        super.onActivityResult(requestCode, resultCode, imgReturnIntent);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (imgReturnIntent != null) {
                //-------------------下单成功 给自己发一条推送-----------------------

//                Runnable networkTask = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO
//                        // 在这里进行 http request.网络请求相关操作
//                        PushMessage pushMessage = new PushMessage();
//                        try {
//                            pushMessage.PushToSelf(Config.getCachedDeviceID(AtyStaffOnly.this), "下单成功！", "UDers正在努力派送中…");
//                        } catch (ClientException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                Thread thread = new Thread(networkTask);
//                thread.start();
                //---------------------------推送结束-----------------------------

                String content = imgReturnIntent.getStringExtra(Constant.CODED_CONTENT);
//                result.setText("扫描结果为：" + content);
                new CompleteOrder(content, new CompleteOrder.SuccessCallback() {

                    @Override
                    public void onSuccess() {

                        Toast.makeText(AtyStaffOnly.this, "完成订单！", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AtyStaffOnly.this, AtyMainFrame.class);
                        i.putExtra("page", "me");
                        startActivity(i);
                    }
                }, new CompleteOrder.FailCallback() {

                    @Override
                    public void onFail() {
                        Toast.makeText(AtyStaffOnly.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        super.onActivityResult(requestCode, resultCode, imgReturnIntent);
    }

}
