package com.example.administrator.bottom.atys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.net.UploadOrder;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import static com.example.administrator.bottom.Config.APP_ID;

public class AtyFetch extends AppCompatActivity {

    private Spinner point_spinner;
    private Spinner loc_spinner;
    private EditText note_edittext;
    private EditText takenum_edittext;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private String point;
    private String loc;
    private String note;
    private String takenum;

    //UI组件初始化
    private void bindView() {
        point_spinner = (Spinner) findViewById(R.id.point_spinner);
        loc_spinner = (Spinner) findViewById(R.id.loc_spinner);
        note_edittext = (EditText) findViewById(R.id.fetch_note);
        takenum_edittext = (EditText) findViewById(R.id.tv_order_takenum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_fetch2);
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
        data_list.add("北门盘锦花园新生活");
        data_list.add("北门盘锦花园内右拐第七家");
        data_list.add("小东门外菜鸟驿站");
        data_list.add("中苑老食堂菜鸟驿站");

        //适配器 android.R.layout.simple_spinner_item
        arr_adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, data_list);
        //设置样式 android.R.layout.simple_spinner_dropdown_item
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        //加载适配器
        point_spinner.setAdapter(arr_adapter);

        //数据
        data_list = new ArrayList<String>();
        SharedPreferences sharedPreferences = getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        String abr = sharedPreferences.getString(Config.ADDRESS, "");
        data_list.add(abr);
        data_list.add("明德楼");
        data_list.add("文德楼");
        data_list.add("信息中心");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        //加载适配器
        loc_spinner.setAdapter(arr_adapter);

        point_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                point = (String) point_spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loc = (String) loc_spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        findViewById(R.id.fetch_summit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = sDateFormat.format(new java.util.Date());

                // 获得phoneNum
                note = note_edittext.getText().toString();
                if (note.equals("")) {
                    note = "none";
                }
                takenum = takenum_edittext.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
                String phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");

                if (takenum.equals("") || takenum == null) {
                    Toast.makeText(AtyFetch.this, "取货号不能为空！", Toast.LENGTH_LONG).show();
                } else {
                    new UploadOrder(phone, point, takenum, loc, note, date, new UploadOrder.SuccessCallback() {

                        @Override
                        public void onSuccess() {


                            Toast.makeText(AtyFetch.this, "提交成功！", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(AtyFetch.this, AtyMainFrame.class);
                            i.putExtra("page", "order");
                            startActivity(i);
                            finish();

                        }
                    }, new UploadOrder.FailCallback() {

                        @Override
                        public void onFail() {
                            Toast.makeText(AtyFetch.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
