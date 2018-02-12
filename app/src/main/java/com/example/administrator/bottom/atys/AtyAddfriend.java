package com.example.administrator.bottom.atys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyuncs.exceptions.ClientException;
import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.alipush.PushMessage;
import com.example.administrator.bottom.net.UploadHXFriend;
import com.example.administrator.bottom.net.UploadOrder;
import com.hyphenate.easeui.widget.EaseTitleBar;

public class AtyAddfriend extends AppCompatActivity {

    private EaseTitleBar titleBar;
    protected EditText query;
    protected ImageButton clearSearch;
    protected FrameLayout errorItemContainer;
    protected InputMethodManager inputMethodManager;
    private Button add;
    private TextView tv_phone;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_addfriend);
        getSupportActionBar().hide();

        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        query = (EditText) findViewById(R.id.query);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) findViewById(R.id.fl_error_item);
        tv_phone = (TextView) findViewById(R.id.tv_atyAddfriend_phone);
        add = (Button)  findViewById(R.id.btn_atyAddfriend_add);

        //---------------------状态栏透明 begin----------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyAddfriend.this.getWindow();
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

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(com.hyphenate.easeui.R.transition.switch_still, com.hyphenate.easeui.R.transition.switch_slide_out_right);
            }
        });

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
                if(count == 11){
                    phone = s.toString();
                    //上传手机号

                    hideSoftKeyboard();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UploadHXFriend(phone,phone, new UploadHXFriend.SuccessCallback() {

                    @Override
                    public void onSuccess() {



                    }
                }, new UploadHXFriend.FailCallback() {

                    @Override
                    public void onFail() {
                        Toast.makeText(AtyAddfriend.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
