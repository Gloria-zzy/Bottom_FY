package com.example.administrator.bottom.atys;

import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.administrator.bottom.R;

import java.util.ArrayList;
import java.util.List;

public class AtyMail extends AppCompatActivity {

    private Spinner time_spinner;
    private Spinner loc_spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    //UI组件初始化
    private void bindView() {
        time_spinner=(Spinner)findViewById(R.id.point_spinner);
        loc_spinner=(Spinner)findViewById(R.id.loc_spinner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_fetch);
        getSupportActionBar().hide();

        //---------------------状态栏透明 begin----------------------------------------
        Window window = AtyMail.this.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.TRANSPARENT);

        ViewGroup mContentView = (ViewGroup) AtyMail.this.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
        //---------------------状态栏透明 end----------------------------------------

        bindView();
        findViewById(R.id.Fetch_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //数据
        data_list = new ArrayList<String>();
        data_list.add("12：00~13：30");
        data_list.add("15：10~15：40");
        data_list.add("17：20~18：30");
        data_list.add("20：10~21：40");

        //适配器
        arr_adapter= new ArrayAdapter<String>(this, R.layout.item_spinner, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        //加载适配器
        time_spinner.setAdapter(arr_adapter);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("默认地址");
        data_list.add("明德楼");
        data_list.add("文德楼");
        data_list.add("信息中心");

        //适配器
        arr_adapter= new ArrayAdapter<String>(this, R.layout.item_spinner, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        //加载适配器
        loc_spinner.setAdapter(arr_adapter);
    }
}
