package com.example.administrator.bottom.atys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.net.UploadAddress;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.bottom.Config.APP_ID;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class AtyAddress extends Activity {
    //    private EditText etUserName;
//    private EditText etPassword_1;
//    private EditText etPassword_2;
//    private EditText etPhoneNumber;
//    private EditText etEmail;
//    private EditText etAddress;
//
//    private TextView tvcode;
    private Spinner area_spinner;
    private Spinner building_spinner;
    private EditText room_edittext;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private String school = "南京信息工程大学";
    private String area = "";
    private String building = "";
    private String room = "";
    private CheckBox agree;

    //UI组件初始化
    private void bindView() {

        agree = (CheckBox) findViewById(R.id.rb_agree);
        agree.setChecked(true);
        area_spinner = (Spinner) findViewById(R.id.area_spinner);
        building_spinner = (Spinner) findViewById(R.id.building_spinner);
        room_edittext = (EditText) findViewById(R.id.room_et);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_address);

        bindView();
        findViewById(R.id.Address_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AtyAddress.this, AtyAgreement.class));
            }
        });

        //数据
        data_list = new ArrayList<String>();
        data_list.add("东苑");
        data_list.add("中苑");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        //加载适配器
        area_spinner.setAdapter(arr_adapter);

        //数据
        data_list = new ArrayList<String>();
//        if(area.equals("东苑")){
            data_list.add("硕园2栋");
            data_list.add("硕园3栋");
            data_list.add("硕园4栋");
            data_list.add("硕园5栋");
            data_list.add("晖园11栋");
            data_list.add("晖园12栋");
            data_list.add("晖园13栋");
            data_list.add("晖园14栋");
//        }
//        if(area.equals("中苑")){
            data_list.add("沁园30栋");
            data_list.add("沁园31栋");
            data_list.add("沁园32栋");
            data_list.add("沁园33栋");
            data_list.add("沁园34栋");
            data_list.add("沁园35栋");
            data_list.add("沁园36栋");
            data_list.add("沁园37栋");
            data_list.add("沁园38栋");
            data_list.add("沁园39栋");
//        }

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        //加载适配器
        building_spinner.setAdapter(arr_adapter);



        area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area = (String) area_spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        building_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                building = (String) building_spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        findViewById(R.id.btnAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(room_edittext.getText().toString().equals("")){
                    Toast.makeText(AtyAddress.this, "宿舍号不能为空！", Toast.LENGTH_LONG).show();
                }else if (agree.isChecked()) {

                    room = room_edittext.getText().toString();
                    // 获得phoneNum
                    SharedPreferences sharedPreferences = getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
                    String phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");
                    String abr = area + building + room;
                    Config.cacheAddress(AtyAddress.this, abr);
                    new UploadAddress(phone, school, area, building, room, new UploadAddress.SuccessCallback() {

                        @Override
                        public void onSuccess() {

                            Intent i = new Intent(AtyAddress.this, AtyMainFrame.class);
                            i.putExtra("page", "me");
                            startActivity(i);

                        }
                    }, new UploadAddress.FailCallback() {

                        @Override
                        public void onFail() {
                            Toast.makeText(AtyAddress.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(AtyAddress.this, R.string.check_agreement, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
