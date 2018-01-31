package com.example.administrator.bottom.atys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.administrator.bottom.R;
import com.example.administrator.zxinglibrary.encode.CodeCreator;
import com.google.zxing.WriterException;

public class AtyGenCode extends AppCompatActivity {

    private ImageView code_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_gen_code);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        setIntent(intent);
        String code = intent.getStringExtra("code");

        //---------------------状态栏透明 begin----------------------------------------
        Window window = AtyGenCode.this.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.TRANSPARENT);

        ViewGroup mContentView = (ViewGroup) AtyGenCode.this.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
        //---------------------状态栏透明 end----------------------------------------

        findViewById(R.id.code_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AtyGenCode.this, AtyMainFrame.class);
                i.putExtra("page","order");
                startActivity(i);
                finish();
                overridePendingTransition(R.transition.switch_still,R.transition.switch_slide_out_right);
            }
        });

        code_view=(ImageView)findViewById(R.id.code);
        String contentEtString = code;
        if (TextUtils.isEmpty(contentEtString)) {
//            Toast.makeText(this, "contentEtString²»ÄÜÎª¿Õ", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = null;
        try {
            bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            code_view.setImageBitmap(bitmap);
        }
    }
}
