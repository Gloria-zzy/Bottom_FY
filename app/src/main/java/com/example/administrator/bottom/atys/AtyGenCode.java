package com.example.administrator.bottom.atys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
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
