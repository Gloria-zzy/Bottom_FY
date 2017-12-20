package com.example.administrator.bottom.frag;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.atys.AtyFetch;
import com.example.administrator.bottom.atys.AtyMainFrame;
import com.example.administrator.bottom.atys.AtyTakenOrders;
import com.example.administrator.bottom.atys.AtyUnlog;
import com.example.administrator.bottom.custom.OrderView;
import com.example.administrator.bottom.custom.TakeView;
import com.example.administrator.bottom.net.DownloadWaitingOrders;
import com.example.administrator.bottom.net.Order;
import com.example.administrator.bottom.net.UpdateOrder;

import java.util.ArrayList;

import static com.example.administrator.bottom.Config.APP_ID;

/**
 * Created by Administrator on 2017/10/29.
 */

public class FragHome extends Fragment {

    private Button get_btn;
    private LinearLayout linearLayout;
    private String phone;

    public FragHome() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        get_btn = (Button) view.findViewById(R.id.get_btn);
        linearLayout = (LinearLayout) view.findViewById(R.id.take_orders);
//        fresh();
        get_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Config.loginStatus == 0) {
                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                } else {
                    startActivityForResult(new Intent(getActivity(), AtyFetch.class), Activity.RESULT_FIRST_USER);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            }
        });
        return view;
    }

    public void fresh() {

        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        new DownloadWaitingOrders(new DownloadWaitingOrders.SuccessCallback() {

            @Override
            public void onSuccess(ArrayList<Order> orders) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
                phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");
                for (Order o : orders) {
                    if (o.getStatus().equals("2") && (!o.getPhone().equals(phone))) {
                        String number = o.getOrderNum();
                        String point = o.getPoint();
                        String takenum = o.getTakenum();
                        String loc = o.getLocation();
                        String note = o.getNote();
                        String status = o.getStatus();
                        String date = o.getDate();
                        String selfphone = o.getPhone();
                        final OrderView newov = new OrderView(getActivity());
                        newov.setOrder_intro("小件快递");
                        newov.setOrder_num(number);
                        newov.setOrder_point(point);
                        newov.setOrder_takenum(takenum);
                        newov.setOrder_loc(loc);
                        newov.setNum(number);
                        newov.setTime(date);
                        newov.setSelfphone(selfphone);
                        if (note.equals("none")) {
                            note = "无";
                        }
                        newov.setOrder_note(note);
                        if (status.equals("0")) {
                            newov.setOrder_status("已结束");
                        } else if (status.equals("1")) {
                            newov.setOrder_status("正在送货");
                        } else if (status.equals("2")) {
                            newov.setOrder_status("待接单");
                        } else if (status.equals("3")) {
                            newov.setOrder_status("订单异常");
                        }

                        final TakeView newtv = new TakeView(getActivity());
                        newtv.setOrderView(newov);
                        newtv.setOrder_taker(phone);
                        newtv.getBtn_ask_to_take().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (Config.loginStatus == 1) {

                                    new UpdateOrder(newtv.getPhone(),newtv.getOrder_taker(),newtv.getOrder_num(),newtv.getPoint(),newtv.getTakenum(),newtv.getLocation(), newtv.getNote(),newtv.getDate(),"1", new UpdateOrder.SuccessCallback() {

                                        @Override
                                        public void onSuccess() {

                                            fresh();
                                            Toast.makeText(getActivity(), "抢单成功！", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(getActivity(), AtyTakenOrders.class);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);

                                        }
                                    }, new UpdateOrder.FailCallback() {

                                        @Override
                                        public void onFail() {
                                            Toast.makeText(getActivity(), R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                                        }
                                    });

                                } else {
                                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                                }
//                                Toast.makeText(getActivity(), newtv.getOrder_num(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        linearLayout.addView(newtv);
                    }
                }
            }
        }, new DownloadWaitingOrders.FailCallback() {

            @Override
            public void onFail() {
                Toast.makeText(getActivity(), R.string.fail_to_commit, Toast.LENGTH_LONG).show();
            }
        });

    }
}


