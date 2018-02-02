package com.example.administrator.bottom.atys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.aliyuncs.exceptions.ClientException;
import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.alipush.PushMessage;
import com.example.administrator.bottom.net.UploadOrder;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import static com.example.administrator.bottom.Config.APP_ID;

public class AtyFetch extends AppCompatActivity {

    private Spinner sp_pickPoint;
    private Spinner sp_arriveAddress;
    private Spinner sp_arriveTime;
    private EditText et_note;
    private EditText et_pickNumber;
    private EditText et_amount;
    private RadioGroup rg_orderPattern;
    private RadioGroup rg_pickPattern;
    private RadioGroup rg_size;
    private LinearLayout ll_orderPattern_temp;
    private LinearLayout ll_amount;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private String pickPoint;
    private String size;
    private String amount;
    private String arriveTime;
    private String arriveAddress;
    private String trustFriend;
    private String note;
    private String pickNumber;

    //UI组件初始化
    private void bindView() {
        sp_pickPoint = (Spinner) findViewById(R.id.sp_atyFetch_pickPoint);
        sp_arriveAddress = (Spinner) findViewById(R.id.sp_atyFetch_arriveAddress);
        sp_arriveTime = (Spinner) findViewById(R.id.sp_atyFetch_arriveTime);
        et_note = (EditText) findViewById(R.id.et_atyFetch_note);
        et_pickNumber = (EditText) findViewById(R.id.tv_atyFetch_pickNumber);
        et_amount = (EditText) findViewById(R.id.tv_atyFetch_amount);
        rg_orderPattern = (RadioGroup) findViewById(R.id.rg_atyFetch_orderPattern);
        rg_pickPattern = (RadioGroup) findViewById(R.id.rg_atyFetch_pickPattern);
        rg_size = (RadioGroup) findViewById(R.id.rg_atyFetch_size);
        ll_orderPattern_temp = (LinearLayout) findViewById(R.id.ll_atyFetch_orderPattern_temp);
        ll_amount = (LinearLayout) findViewById(R.id.ll_atyFetch_amount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_fetch2);
        getSupportActionBar().hide();

        bindView();
        findViewById(R.id.iv_atyFetch_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //---------------------状态栏透明 begin----------------------------------------
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyFetch.this.getWindow();
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


        //----------------------------快递点 begin---------------------------------
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
        sp_pickPoint.setAdapter(arr_adapter);
        sp_pickPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pickPoint = (String) sp_pickPoint.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //----------------------------快递点 end---------------------------------

        //----------------------------收货地点 begin---------------------------------
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
        sp_arriveAddress.setAdapter(arr_adapter);
        sp_arriveAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arriveAddress = (String) sp_arriveAddress.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //----------------------------收货地点 end---------------------------------

        //----------------------------收货时间 begin---------------------------------
        //数据
        data_list = new ArrayList<String>();
        data_list.add("18：30~20：30");
        data_list.add("20：30~22：00");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        //加载适配器
        sp_arriveTime.setAdapter(arr_adapter);
        sp_arriveTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arriveTime = (String) sp_arriveTime.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //----------------------------收货时间 end---------------------------------

        ll_orderPattern_temp.setVisibility(View.GONE);

        findViewById(R.id.btn_atyFetch_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                下单时间 order_time
//                信任好友 trust_friend
//                快递体积 size(L M S)
//                快递数量 amount(int)
//                收货地点 arrive_address
//                收货时间 arrive_time
//                快递点   pick_point
//                取货号   pick_number
//                备注     note

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String orderTime = sDateFormat.format(new java.util.Date());

                // 获得phoneNum
                note = et_note.getText().toString();
                if (note.equals("")) {
                    note = "none";
                }
                if(rg_pickPattern.getCheckedRadioButtonId() == R.id.rb_atyFetch_pickPattern_friend){
                    trustFriend = "CHARLES";
                }else trustFriend = "none";

                switch (rg_size.getCheckedRadioButtonId()) {
                    case R.id.rb_atyFetch_size_small:
                        size = "S";
                        break;
                    case R.id.rb_atyFetch_size_medium:
                        size = "M";
                        break;
                    case R.id.rb_atyFetch_size_large:
                        size = "L";
                        break;
                }
                amount = et_amount.getText().toString();
                pickNumber = et_pickNumber.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
                String phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");

                if (rg_orderPattern.getCheckedRadioButtonId()== R.id.rb_atyFetch_orderPattern_temp && (pickNumber.equals("") || pickNumber == null)) {
                    Toast.makeText(AtyFetch.this, "取货号不能为空！", Toast.LENGTH_LONG).show();
                } else {
                    new UploadOrder(phone, orderTime, trustFriend, size, amount, arriveAddress, arriveTime, pickPoint, pickNumber, note, new UploadOrder.SuccessCallback() {

                        @Override
                        public void onSuccess() {

                            //-------------------下单成功 给自己发一条推送-----------------------
                            SharedPreferences sharedPreferences = AtyFetch.this.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
                            final String deviceId = sharedPreferences.getString(Config.DEVICEID, "");

                            Runnable networkTask = new Runnable() {

                                @Override
                                public void run() {
                                    // TODO
                                    // 在这里进行 http request.网络请求相关操作
                                    PushMessage pushMessage = new PushMessage();
                                    try {
                                        pushMessage.PushToSelf(deviceId, "下单成功！", "UDers正在努力派送中…");
                                    } catch (ClientException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            Thread thread = new Thread(networkTask);
                            thread.start();
                            //---------------------------推送结束-----------------------------

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


        rg_orderPattern.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_atyFetch_orderPattern_old:
                        ll_orderPattern_temp.setVisibility(View.GONE);
                        ll_amount.setVisibility(View.VISIBLE);
                        pickNumber = "none";
                        pickPoint = "none";
                        break;
                    case R.id.rb_atyFetch_orderPattern_temp:
                        ll_orderPattern_temp.setVisibility(View.VISIBLE);
                        ll_amount.setVisibility(View.GONE);
                        break;
                }
            }
        });


    }
}
