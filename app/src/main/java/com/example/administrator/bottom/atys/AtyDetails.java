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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.custom.OrderView;
import com.example.administrator.bottom.custom.QQRefreshHeader;
import com.example.administrator.bottom.custom.RefreshLayout;
import com.example.administrator.bottom.net.DownloadOneOrder;
import com.example.administrator.bottom.net.DownloadOrders;
import com.example.administrator.bottom.net.Order;

import java.util.ArrayList;

import static com.example.administrator.bottom.Config.APP_ID;
import static com.example.administrator.bottom.Config.cacheAddress;

//        订单号   order_number
//        下单时间 order_time
//        信任好友 trust_friend
//        快递体积 size(L M S)
//        收货地点 arrive_address
//        收货时间 arrive_time
//        快递点   pick_point
//        取货号   pick_number
//        派送员   taker
//        备注     note
//        状态     order_status(int)

public class AtyDetails extends AppCompatActivity {

    private TextView tv_orderNumber;
    private TextView tv_orderTime;
    private TextView tv_trustFriend;
    private TextView tv_size;
    private TextView tv_arriveAddress;
    private TextView tv_arriveTime;
    private TextView tv_pickPoint;
    private TextView tv_pickNumber;
    private TextView tv_taker;
    private TextView tv_note;
    private TextView tv_orderStatus;

    private LinearLayout ll_orderPattern_temp;
    private LinearLayout ll_pickPattern_self;
    private LinearLayout ll_pickPattern_friend;

    private String phone;
    private String orderNumber;
    private String orderTime;
    private String pickPoint;
    private String size;
    private String amount;
    private String arriveTime;
    private String arriveAddress;
    private String trustFriend;
    private String note;
    private String taker;
    private String orderStatus;
    private String pickNumber;

    //UI组件初始化
    private void bindView() {
        tv_orderNumber = (TextView) findViewById(R.id.tv_atyDetails_orderNumber);
        tv_orderTime = (TextView) findViewById(R.id.tv_atyDetails_orderTime);
        tv_trustFriend = (TextView) findViewById(R.id.tv_atyDetails_trustFriend);
        tv_size = (TextView) findViewById(R.id.tv_atyDetails_size);
        tv_arriveAddress = (TextView) findViewById(R.id.tv_atyDetails_arriveAddress);
        tv_arriveTime = (TextView) findViewById(R.id.tv_atyDetails_arriveTime);
        tv_pickPoint = (TextView) findViewById(R.id.tv_atyDetails_pickPoint);
        tv_pickNumber = (TextView) findViewById(R.id.tv_atyDetails_pickNumber);
        tv_taker = (TextView) findViewById(R.id.tv_atyDetails_taker);
        tv_note = (TextView) findViewById(R.id.tv_atyDetails_note);
        tv_orderStatus = (TextView) findViewById(R.id.tv_atyDetails_orderStatus);
        ll_orderPattern_temp = (LinearLayout) findViewById(R.id.ll_atyDetails_orderPattern_temp);
        ll_pickPattern_self = (LinearLayout) findViewById(R.id.ll_atyDetails_pickPattern_self);
        ll_pickPattern_friend = (LinearLayout) findViewById(R.id.ll_atyDetails_pickPattern_friend);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_details);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        setIntent(intent);
        orderNumber = intent.getStringExtra("orderNumber");

        bindView();
        findViewById(R.id.iv_atyDetails_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.switch_still, R.transition.switch_slide_out_right);
            }
        });

        //---------------------状态栏透明 begin----------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyDetails.this.getWindow();
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

//        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout_taken_orders);
//        if (refreshLayout != null) {
//            // 刷新状态的回调
//            refreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    // 延迟3秒后刷新成功
//                    refreshLayout.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            refreshLayout.refreshComplete();
                            //-----------------BEGIN-----------------

//                            SharedPreferences sharedPreferences = AtyDetails.this.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
//                            phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");
                            new DownloadOneOrder(orderNumber, new DownloadOneOrder.SuccessCallback() {

                                @Override
                                public void onSuccess(ArrayList<Order> orders) {

                                    //        订单号   order_number
                                    //        下单时间 order_time
                                    //        信任好友 trust_friend
                                    //        快递体积 size(L M S)
                                    //        收货地点 arrive_address
                                    //        收货时间 arrive_time
                                    //        快递点   pick_point
                                    //        取货号   pick_number
                                    //        派送员   taker
                                    //        备注     note
                                    //        状态     order_status(int)

                                    for (Order o : orders) {
                                        orderNumber = o.getOrderNumber();
                                        orderTime = o.getOrderTime();
                                        trustFriend = o.getTrust_friend();
                                        switch (o.getSize()){
                                            case "S":
                                                size = "小";
                                                break;
                                            case "M":
                                                size = "中";
                                                break;
                                            case "L":
                                                size = "大";
                                                break;
                                        }
                                        arriveAddress = o.getArriveAddress();
                                        arriveTime = o.getArriveTime();
                                        pickPoint = o.getPickPoint();
                                        pickNumber = o.getPickNumber();
                                        if(o.getTaker().equals("0")){
                                            taker = "暂无";
                                        }
                                        note = o.getNote();
                                        if (o.getOrderStatus().equals("0")){
                                            orderStatus = "已结单";
                                        }else{
                                            orderStatus = "派送中";
                                        }
                                        if (note.equals("none")) {
                                            note = "无";
                                        }

                                        tv_orderNumber.setText(orderNumber);
                                        tv_orderTime.setText(orderTime);
                                        tv_trustFriend.setText(trustFriend);
                                        tv_size.setText(size);
                                        tv_arriveAddress.setText(arriveAddress);
                                        tv_arriveTime.setText(arriveTime);
                                        tv_pickPoint.setText(pickPoint);
                                        tv_pickNumber.setText(pickNumber);
                                        tv_taker.setText(taker);
                                        tv_note.setText(note);
                                        tv_orderStatus.setText(orderStatus);

                                        if (pickPoint.equals("") || pickPoint == null) {
                                            //老用户
                                            ll_orderPattern_temp.setVisibility(View.GONE);
                                        } else {
                                            //新用户
                                            ll_orderPattern_temp.setVisibility(View.VISIBLE);
                                        }
                                        if (trustFriend.equals("none")) {
                                            //信任好友代拿
                                            ll_pickPattern_self.setVisibility(View.GONE);
                                            ll_pickPattern_friend.setVisibility(View.VISIBLE);
                                        } else {
                                            //自己拿
                                            ll_pickPattern_self.setVisibility(View.VISIBLE);
                                            ll_pickPattern_friend.setVisibility(View.GONE);
                                        }

                                    }
                                }
                            }, new DownloadOneOrder.FailCallback() {

                                @Override
                                public void onFail() {
                                    Toast.makeText(AtyDetails.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                                }
                            });

                            findViewById(R.id.btn_atyDetails_code).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(AtyDetails.this, AtyGenCode.class);
                                    intent.putExtra("code", orderNumber);
                                    startActivity(intent);
                                    AtyDetails.this.overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                                }
                            });


                            //-----------------END-----------------
//                        }
//                    }, Config.DELAYMILLIS);
//                }
//            });
//        }
//        QQRefreshHeader header = new QQRefreshHeader(this);
//        refreshLayout.setRefreshHeader(header);
//        refreshLayout.autoRefresh();


    }
}
