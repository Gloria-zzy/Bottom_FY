package com.example.administrator.bottom.atys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
