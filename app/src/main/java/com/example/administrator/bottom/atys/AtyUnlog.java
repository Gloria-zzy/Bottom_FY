package com.example.administrator.bottom.atys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.example.administrator.bottom.R;

public class AtyUnlog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_unlog);
        getSupportActionBar().hide();

        findViewById(R.id.to_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AtyUnlog.this, AtyLogin.class);
                startActivity(intent);
                overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
            }
        });

        findViewById(R.id.back_to_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.switch_still,R.transition.switch_slide_out_right);
            }
        });
    }
}
