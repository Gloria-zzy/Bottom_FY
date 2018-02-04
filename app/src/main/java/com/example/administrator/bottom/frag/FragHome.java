package com.example.administrator.bottom.frag;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.atys.AtyFetch;
import com.example.administrator.bottom.atys.AtyUnlog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/29.
 */

public class FragHome extends Fragment {

    private Button get_btn;
    //    private LinearLayout linearLayout;
    private String phone;

    public FragHome() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        get_btn = (Button) view.findViewById(R.id.get_btn);
//        linearLayout = (LinearLayout) view.findViewById(R.id.take_orders);
//        fresh();
        get_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Config.loginStatus == 0) {
                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                } else {

//                    Runnable networkTask = new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO
//                            // 在这里进行 http request.网络请求相关操作
//                            PushMessage pushMessage = new PushMessage();
//                            try {
//                                pushMessage.Push();
//                            } catch (ClientException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    Thread thread = new Thread(networkTask);
//                    thread.start();

                    startActivityForResult(new Intent(getActivity(), AtyFetch.class), Activity.RESULT_FIRST_USER);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            }
        });

        //--------------------------九宫格 begin--------------------------------------------------------
        final String[] name={"地址管理","定位","扫描二维码",
                             "使用指南","优惠券","历史订单",
                             "问题反馈","关于UD","客服"};

        final int[] imageRes={R.drawable.item_fraghome_address,
                R.drawable.item_fraghome_locate,
                R.drawable.item_fraghome_scanner,
                R.drawable.item_fraghome_help,
                R.drawable.item_fraghome_discount,
                R.drawable.item_fraghome_order,
                R.drawable.item_fraghome_feedback,
                R.drawable.item_fraghome_aboutud,
                R.drawable.item_fraghome_consult};
        GridView gridView = (GridView) view.findViewById(R.id.gv_fragHome);//初始化

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
                R.layout.item_grid,//item的XML

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
        //--------------------------九宫格 end--------------------------------------------------------

//        final RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout_frag_home);
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
//                            //-----------------BEGIN-----------------
//                            fresh();
//                            //-----------------END-----------------
//                        }
//                    }, Config.DELAYMILLIS);
//                }
//            });
//        }
//        QQRefreshHeader header = new QQRefreshHeader(getActivity());
//        refreshLayout.setRefreshHeader(header);
//        refreshLayout.autoRefresh();

        return view;
    }

    public void fresh() {


    }
}


