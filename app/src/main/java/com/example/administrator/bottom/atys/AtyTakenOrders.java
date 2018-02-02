package com.example.administrator.bottom.atys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.custom.OrderView;
import com.example.administrator.bottom.custom.QQRefreshHeader;
import com.example.administrator.bottom.custom.RefreshLayout;
import com.example.administrator.bottom.net.DownloadTakenOrders;
import com.example.administrator.bottom.net.Order;
import com.example.administrator.bottom.net.UpdateOrder;

import java.util.ArrayList;

import static com.example.administrator.bottom.Config.APP_ID;

public class AtyTakenOrders extends AppCompatActivity {

    private String phone;
    private LinearLayout sv;
    private String number;
    private String point;
    private String takenum;
    private String loc;
    private String note;
    private String status;
    private String date;
    private String order_num;
    private String taker;
    private String publisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_taken_orders);
        getSupportActionBar().hide();

        //---------------------状态栏透明 begin----------------------------------------
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyTakenOrders.this.getWindow();
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

        findViewById(R.id.iv_taken_orders_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.switch_still, R.transition.switch_slide_out_right);
            }
        });

        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout_taken_orders);
        if (refreshLayout != null) {
            // 刷新状态的回调
            refreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // 延迟3秒后刷新成功
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.refreshComplete();
                            //-----------------BEGIN-----------------


//                            sv = (LinearLayout) findViewById(R.id.sv_taken_orders);
//                            SharedPreferences sharedPreferences = AtyTakenOrders.this.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
//                            phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");
//                            if (sv != null) {
//                                sv.removeAllViews();
//                            }
//                            new DownloadTakenOrders(phone, new DownloadTakenOrders.SuccessCallback() {
//
//                                @Override
//                                public void onSuccess(ArrayList<Order> orders) {
//
//                                    for (Order o : orders) {
//                                        number = o.getOrderNumber();
//                                        point = o.getPickPoint();
//                                        takenum = o.getPickNumber();
//                                        loc = o.getArriveAddress();
//                                        note = o.getNote();
//                                        status = o.getOrderStatus();
//                                        date = o.getOrderTime();
//                                        order_num = o.getOrderNumber();
//                                        taker = o.getTaker();
//                                        publisher = o.getPhone();
//                                        final OrderView newov = new OrderView(AtyTakenOrders.this);
//
//                                        newov.setTv_size("小件快递");
//                                        newov.setTv_order_number(number);
//                                        newov.setOrder_point(point);
//                                        newov.setOrder_takenum(takenum);
//                                        newov.setTv_arriveAddress(loc);
//                                        newov.setOrderNumber(number);
//                                        newov.setTv_orderTime(date);
//                                        if (note.equals("none")) {
//                                            note = "无";
//                                        }
//                                        newov.setTv_note(note);
//                                        newov.getOrder_delete().setVisibility(View.GONE);
//                                        newov.getOrder_change().setVisibility(View.GONE);
//                                        newov.getOrder_code().setVisibility(View.GONE);
//                                        newov.getOrder_cancel().setVisibility(View.GONE);
//                                        if (status.equals("0")) {
//                                            newov.setOrder_status("已结束");
//                                            sv.addView(newov);
//                                        } else if (status.equals("1")) {
//                                            newov.setOrder_status("正在送货");
//                                            sv.addView(newov);
//                                        } else if (status.equals("2")) {
//                                            newov.setOrder_status("待接单");
//                                            sv.addView(newov);
//                                        } else if (status.equals("3")) {
//                                            newov.setOrder_status("订单异常");
//                                            sv.addView(newov);
//                                        }
//                                        newov.getDischarge_order().setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                new UpdateOrder(publisher, taker, order_num, point, takenum, loc, note, date, "2", new UpdateOrder.SuccessCallback() {
//
//                                                    @Override
//                                                    public void onSuccess() {
//
//
//                                                        Toast.makeText(AtyTakenOrders.this, "已放弃订单", Toast.LENGTH_LONG).show();
//                                                        Intent i = new Intent(AtyTakenOrders.this, AtyTakenOrders.class);
//                                                        startActivity(i);
//                                                        finish();
//
////                                    AtyTakenOrders.this.overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
//
//                                                    }
//                                                }, new UpdateOrder.FailCallback() {
//
//                                                    @Override
//                                                    public void onFail() {
//                                                        Toast.makeText(AtyTakenOrders.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//                                            }
//                                        });
//                                    }
//                                }
//                            }, new DownloadTakenOrders.FailCallback() {
//
//                                @Override
//                                public void onFail() {
//                                    Toast.makeText(AtyTakenOrders.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
//                                }
//                            });

                            //-----------------END-----------------
                        }
                    }, Config.DELAYMILLIS);
                }
            });
        }
        QQRefreshHeader header = new QQRefreshHeader(this);
        refreshLayout.setRefreshHeader(header);
        refreshLayout.autoRefresh();
    }
}
