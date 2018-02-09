package com.example.administrator.bottom.atys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.net.DownloadAddress;
import com.example.administrator.bottom.net.GetCode;
import com.example.administrator.bottom.net.Login;
import com.example.administrator.bottom.tools.MD5Tool;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AtyLogin extends Activity {

    private EditText etPhone = null;
    private EditText etCode = null;

    private Button getcodeBtn, loginBtn, registerBtn;

    //UI组件初始化
    private void bindView() {
        etPhone = (EditText) findViewById(R.id.etPhoneNum);
        etCode = (EditText) findViewById(R.id.etCode);
        getcodeBtn = (Button) findViewById(R.id.btnGetCode);
        loginBtn = (Button) findViewById(R.id.btnLogin);
//        registerBtn = (Button) findViewById(R.id.btnRegister);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login);
        bindView();

        //---------------------状态栏透明 begin----------------------------------------
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyLogin.this.getWindow();
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

        findViewById(R.id.back_to_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.switch_still,R.transition.switch_slide_out_right);
            }
        });
        getcodeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (TextUtils.isEmpty(etPhone.getText())) {
                    Toast.makeText(AtyLogin.this, R.string.phone_num_cannot_be_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog pd = ProgressDialog.show(AtyLogin.this, getResources().getString(R.string.connecting), getResources().getString(R.string.connecting_to_server));

                new GetCode(etPhone.getText().toString(), new GetCode.SuccessCallback() {

                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        Toast.makeText(AtyLogin.this, R.string.suc_to_get_code, Toast.LENGTH_LONG).show();
                    }
                }, new GetCode.FailCallback() {

                    @Override
                    public void onFail() {
                        pd.dismiss();
                        Toast.makeText(AtyLogin.this, R.string.fail_to_get_code, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (TextUtils.isEmpty(etPhone.getText())) {
                    Toast.makeText(AtyLogin.this, R.string.phone_num_cannot_be_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(etCode.getText())) {
                    Toast.makeText(AtyLogin.this, R.string.code_cannot_be_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                new Login(MD5Tool.md5(etPhone.getText().toString()), etCode.getText().toString(), etPhone.getText().toString(),new Login.SuccessCallback() {

                    @Override
                    public void onSuccess(String token,int isvalid) {

                        Config.cacheToken(AtyLogin.this, token);
                        Config.cachePhoneNum(AtyLogin.this, etPhone.getText().toString());
                        //注册失败会抛出HyphenateException
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    EMClient.getInstance().createAccount(Config.getCachedToken(AtyLogin.this), Config.getCachedToken(AtyLogin.this));//同步方法
                                    Log.i("AtyLogin", "EMClient register succ");
                                } catch (HyphenateException e) {
                                    Log.i("AtyLogin", "EMClient register exception");
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                        Config.loginStatus = Config.RESULT_STATUS_SUCCESS;

                        if(isvalid==Config.RESULT_STATUS_SUCCESS){

                            new DownloadAddress(etPhone.getText().toString(), new DownloadAddress.SuccessCallback() {

                                @Override
                                public void onSuccess(String school, String area, String building, String room) {
                                    Config.cacheAddress(AtyLogin.this, area + building + room);
                                }
                            }, new DownloadAddress.FailCallback() {

                                @Override
                                public void onFail() {
                                    Toast.makeText(AtyLogin.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                                }
                            });

                            Toast.makeText(AtyLogin.this, "您已注册", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(AtyLogin.this, AtyMainFrame.class);
                            i.putExtra("page","me");
                            startActivity(i);
                            finish();
                        }else{
                            Intent i = new Intent(AtyLogin.this, AtyAddress.class);
                            startActivity(i);
                            finish();
                        }


                    }
                }, new Login.FailCallback() {

                    @Override
                    public void onFail() {
                        Toast.makeText(AtyLogin.this, R.string.fail_to_login, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
