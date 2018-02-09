package com.example.administrator.bottom.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.bottom.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;

public class LoginActivity extends FragmentActivity {
    private EditText usernameView;
    private EditText pwdView;

    protected InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        // should be in launcher activity, but all app use this can avoid the problem
        if(!isTaskRoot()){
            Intent intent = getIntent();
            String action = intent.getAction();
            if(intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)){
                finish();
                return;
            }
        }
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        /*
        *  以上用于避免extends EaseBaseActivity，已经将EaseBaseActivity中的有价值内容复制过来了
        * */

        if(EMClient.getInstance().isLoggedInBefore()){
            //enter to main activity directly if you logged in before.
            startActivity(new Intent(this, ChatMainActivity.class));
            finish();
        }
        
        setContentView(R.layout.aty_chat_login);
        usernameView = (EditText) findViewById(R.id.et_username);
        pwdView = (EditText) findViewById(R.id.et_password);
        Button loginBtn = (Button) findViewById(R.id.btn_login);
        
        loginBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                //login
                EMClient.getInstance().login(usernameView.getText().toString(), pwdView.getText().toString(), new EMCallBack() {
                    
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(LoginActivity.this, ChatMainActivity.class));
                        finish();
                    }
                    
                    @Override
                    public void onProgress(int progress, String status) {
                        
                    }
                    
                    @Override
                    public void onError(int code, String error) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        // cancel the notification
        EaseUI.getInstance().getNotifier().reset();
    }
}
