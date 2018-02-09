package com.example.administrator.bottom.atys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.bottom.R;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by Administrator on 2018/2/8 0008.
 */

public class AtyChat extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_fetch);

        EaseChatFragment chatFragment = new EaseChatFragment();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
//        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }
}

