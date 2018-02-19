package com.example.administrator.bottom.frag;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.L;
import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.atys.AtyDetails;
import com.example.administrator.bottom.atys.AtyFetch;
import com.example.administrator.bottom.atys.AtyMainFrame;
import com.example.administrator.bottom.atys.AtyUnlog;
import com.example.administrator.bottom.custom.OrderView;
import com.example.administrator.bottom.net.DownloadOrders;
import com.example.administrator.bottom.net.Order;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.administrator.bottom.Config.APP_ID;

/**
 * Created by Administrator on 2017/10/29.
 */

public class FragHome extends Fragment {

    // 代拿下单按钮
    private Button get_btn;
    private TextView tv_delivering;
    private TextView tv_history;
    private TextView tv_error;
    private LinearLayout ll_delivering;
    private LinearLayout ll_history;
    private LinearLayout ll_error;
    private int delivering_num = 0;
    private int history_num = 0;
    private int error_num = 0;

    //    private LinearLayout linearLayout;
    private String phone;

    // 默认构造函数
    public FragHome() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        get_btn = (Button) view.findViewById(R.id.get_btn);
//        linearLayout = (LinearLayout) view.findViewById(R.id.take_orders);

        // 获得phoneNum
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");
        fresh();

        // 绑定下单按钮的事件
        get_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Config.loginStatus == 0) {
                    // 用户未登录
                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                } else {
                    // 用户已登录

                    // 这个startActivityforResult没有写对应的onActivityResult函数进行处理
                    startActivityForResult(new Intent(getActivity(), AtyFetch.class), Activity.RESULT_FIRST_USER);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            }
        });

        //--------------------------九宫格 begin--------------------------------------------------------
        final String[] name = {"地址管理", "定位", "扫描二维码",
                "使用指南", "优惠券", "历史订单",
                "问题反馈", "关于UD", "客服"};

        final int[] imageRes = {R.drawable.item_fraghome_address,
                R.drawable.item_fraghome_locate,
                R.drawable.item_fraghome_scanner,
                R.drawable.item_fraghome_help,
                R.drawable.item_fraghome_discount,
                R.drawable.item_fraghome_order,
                R.drawable.item_fraghome_feedback,
                R.drawable.item_fraghome_aboutud,
                R.drawable.item_fraghome_consult};
        GridView gridView = (GridView) view.findViewById(R.id.gv_fragHome_func);//初始化

        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> listItemArrayList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < imageRes.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", imageRes[i]);
            map.put("itemText", name[i]);
            listItemArrayList.add(map);
        }
        //生成适配器的ImageItem 与动态数组的元素相对应
        SimpleAdapter saImageItems = new SimpleAdapter(getActivity(),
                listItemArrayList,//数据来源
                R.layout.item_grid_func,//item的XML

                //动态数组与ImageItem对应的子项
                new String[]{"itemImage", "itemText"},

                //ImageItem的XML文件里面的一个ImageView,TextView ID
                new int[]{R.id.iv_itemGrid, R.id.tv_itemGrid});
        //添加并且显示
        gridView.setAdapter(saImageItems);
        //添加消息处理
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), name[position], Toast.LENGTH_LONG).show();
            }
        });

        gridView.setFocusable(false);
        //--------------------------九宫格 end--------------------------------------------------------

        //bindviews
        tv_delivering = (TextView) view.findViewById(R.id.tv_fragHome_delivering);
        tv_history = (TextView) view.findViewById(R.id.tv_fragHome_history);
        tv_error = (TextView) view.findViewById(R.id.tv_fragHome_error);
        ll_delivering = (LinearLayout) view.findViewById(R.id.ll_fragHome_delivering);
        ll_history = (LinearLayout)  view.findViewById(R.id.ll_fraghome_history);
        ll_error = (LinearLayout) view.findViewById(R.id.ll_fragHome_error);

        ll_delivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AtyMainFrame.class);
                intent.putExtra("page", "order");
                startActivity(intent);
                getActivity().finish();
            }
        });

        ll_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AtyMainFrame.class);
                intent.putExtra("page", "order_history");
                startActivity(intent);
                getActivity().finish();
            }
        });

        ll_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AtyMainFrame.class);
                intent.putExtra("page", "order");
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    public void fresh() {

        new DownloadOrders(phone, new DownloadOrders.SuccessCallback() {

            @Override
            public void onSuccess(ArrayList<Order> orders) {
                for (Order o : orders) {
                    String orderStatus = o.getOrderStatus();

                    if(orderStatus.equals("0")){
                        history_num++;
                    }else if(orderStatus.equals("1") || orderStatus.equals("2")){
                        delivering_num++;
                    }else if(orderStatus.equals("3")){
                        error_num++;
                    }
                }
                tv_delivering.setText(delivering_num + "");
                tv_history.setText(history_num + "");
                tv_error.setText(error_num + "");

            }
        }, new DownloadOrders.FailCallback() {

            @Override
            public void onFail() {
                Toast.makeText(getActivity(), R.string.fail_to_commit, Toast.LENGTH_LONG).show();
            }
        });

    }
}


