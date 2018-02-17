package com.example.administrator.bottom.atys;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.aliyuncs.exceptions.ClientException;
import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.MainActivity;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.alipush.PushMessage;
import com.example.administrator.bottom.net.UploadOrder;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.net.DownloadHXFriends;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private LinearLayout ll_trustfriend;
    private TextView tv_trustfriend;
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

    private final String TAG = "AtyFetch";

    //UI组件初始化（绑定）
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
        ll_trustfriend = (LinearLayout) findViewById(R.id.ll_atyFetch_trustfriend);
        tv_trustfriend = (TextView) findViewById(R.id.tv_atyFetch_trustfriend);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_fetch2);
        getSupportActionBar().hide();

        bindView();

        // 绑定回退按钮事件
        findViewById(R.id.iv_atyDetails_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.switch_still, R.transition.switch_slide_out_right);
            }
        });

        //---------------------状态栏透明 begin----------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

        // pick_point快递点数据
        data_list = new ArrayList<String>();
        data_list.add("北门盘锦花园新生活");
        data_list.add("北门盘锦花园内右拐第七家");
        data_list.add("小东门外菜鸟驿站");
        data_list.add("中苑老食堂菜鸟驿站");

        // 适配器 android.R.layout.simple_spinner_item
        arr_adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, data_list);
        // 设置样式 android.R.layout.simple_spinner_dropdown_item
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        // 加载适配器到pick_point（填充数据）
        sp_pickPoint.setAdapter(arr_adapter);
        // 设置sp_pickPoint的点击事件
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
        // 新建data_list存放数据
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
        ll_trustfriend.setVisibility(View.GONE);

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

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String orderTime = sDateFormat.format(new java.util.Date());

                // 获得phoneNum
                note = et_note.getText().toString();
                if (note.equals("")) {
                    note = "none";
                }

//                RadioButton rb_pickPattern = (RadioButton)findViewById(rg_pickPattern.getCheckedRadioButtonId());
//                String pickPattern = rb_pickPattern.getText().toString();

//                if (rg_pickPattern.getCheckedRadioButtonId() == R.id.rb_atyFetch_pickPattern_friend) {
//                    trustFriend = "CHARLES";
//                } else trustFriend = "none";

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
                if (rg_orderPattern.getCheckedRadioButtonId() == R.id.rb_atyFetch_orderPattern_temp) {
                    pickNumber = et_pickNumber.getText().toString();
                } else {
                    pickNumber = "none";
                }

                amount = et_amount.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
                String phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");

                if (rg_orderPattern.getCheckedRadioButtonId() == R.id.rb_atyFetch_orderPattern_temp && (pickNumber.equals("") || pickNumber == null)) {
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

        rg_pickPattern.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_atyFetch_pickPattern_self:
                        trustFriend = "none";
                        ll_trustfriend.setVisibility(View.GONE);
                        break;
                    case R.id.rb_atyFetch_pickPattern_friend:
                        ll_trustfriend.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        tv_trustfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadHXFriends(Config.getCachedPhoneNum(AtyFetch.this), new DownloadHXFriends.SuccessCallback() {
                    @Override
                    public void onSuccess(ArrayList<String> friendsName) {
                        Log.i(TAG, "DownloadHXFriends onSuccess");
                        Map<String, EaseUser> arrContacts = new HashMap();
                        String items[][] = new String[1][friendsName.size()];
                        items[0] = new String[friendsName.size()];
                        for (int i = 0; i < friendsName.size(); i++) {
                            EaseUser user = new EaseUser(friendsName.get(i));
                            arrContacts.put(user.getUsername(), user);
                            Log.i(TAG, "write arrContacts");
                            String fname = arrContacts.get(user.getUsername()).getUsername();
                            Log.i(TAG, "friend name is " + fname);
                            items[0][i] = new String(fname);
                        }
                        dialogChoice(items); // 单选
                    }
                }, new DownloadHXFriends.FailCallback() {
                    @Override
                    public void onFail() {
                        Toast.makeText(AtyFetch.this, "获取好友列表失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void dialogChoice(final String[][] items) {
        Log.i("AtyFetch", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1111");
        final String[] trustfriend = new String[1];

        //获取当前用户的好友列表，放在items中


        Log.i(TAG, "outside DownloadHXFriends");

        //单选对话窗口
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 6);
        Log.i(TAG, "after AlterDialog");

        //定义标题样式
        TextView title = new TextView(this);
        title.setText("好友列表");
        title.setPadding(40, 10, 10, 10);
        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setTextColor(getResources().getColor(R.color.text_clo));
        title.setTextSize(20);
        Log.i(TAG, "after set title");

        //设置图片
        Drawable drawable = getResources().getDrawable(R.drawable.item_trustfriend);
        drawable.setBounds(10, 10, drawable.getMinimumWidth(), drawable.getMinimumHeight());//这句一定要加
        title.setCompoundDrawables(drawable, null, null, null);//setCompoundDrawables用来设置图片显示在文本的哪一端
        title.setCompoundDrawablePadding(30);//设置文字和图片间距
        Log.i(TAG, "after set Image");

        if (items[0].length == 0)
        {
            items[0] = new String[10];
            for (int i = 0; i < 10; i++) {
                items[0][i] = new String("" + i);
            }
        } else {
            for (int i = 0; i < items[0].length; i++) {
                Log.i(TAG,"items:" + items[0][i]);
            }
        }
        //使用自定义title
        builder.setCustomTitle(title);
        try {
            // item[0] 是一个一维字符串数组，里面的元素都必须全部初始化，若有一个及以上元素为null，会抛出NullPointException异常
            builder.setSingleChoiceItems(items[0], -1,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                        Toast.makeText(AtyFetch.this, items[which],
//                                Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "inside setSingleChoiceItems");
                            if (items[0][which].length() > 0) {
                                trustfriend[0] = items[0][which];
                            } else {
                                trustfriend[0] = "请选择信任好友";
                            }
                        }
                    });
            builder.setPositiveButton("信任TA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
//                    Toast.makeText(AtyFetch.this, trustfriend[0], Toast.LENGTH_SHORT)
//                            .show();
                    tv_trustfriend.setText(trustfriend[0]);
                    trustFriend = trustfriend[0];
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "after code set friends");

        builder.create().show();
    }

}
