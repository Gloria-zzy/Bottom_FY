package com.example.administrator.bottom.frag;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.atys.AtyLogin;
import com.example.administrator.bottom.atys.AtyMainFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/29.
 */

public class FragCommunity extends Fragment {
    private String context;
    private TextView mTextView;
    private ViewPager pager;
    private List<View> views;
    private List<TextView> tvs = new ArrayList<TextView>();
    TextView tv1;
    TextView tv2;
    TextView tv3;


    public FragCommunity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (Config.loginStatus == 0) {
            view = inflater.inflate(R.layout.aty_unlog, container, false);
            view.findViewById(R.id.to_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AtyLogin.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            });

            view.findViewById(R.id.back_to_home).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), AtyMainFrame.class);
                    i.putExtra("page", "home");
                    startActivity(i);
                }
            });
        } else {
            view = inflater.inflate(R.layout.frag_community, container, false);
            pager = (ViewPager) view.findViewById(R.id.pager);
            tv1 = (TextView) view.findViewById(R.id.page1);
            tv1.setTextColor(Color.BLUE);
            tv2 = (TextView) view.findViewById(R.id.page2);
            tv3 = (TextView) view.findViewById(R.id.page3);
            tv1.setOnClickListener(new MyClickListener(0));
            tv2.setOnClickListener(new MyClickListener(1));
            tv3.setOnClickListener(new MyClickListener(2));
            tvs.add(tv1);
            tvs.add(tv2);
            tvs.add(tv3);
//        初始化ViewPager组件
            initView();
            initViewPager();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Config.loginStatus == 1) {
            tv1.setOnClickListener(new MyClickListener(0));
            tv2.setOnClickListener(new MyClickListener(1));
            tv3.setOnClickListener(new MyClickListener(2));
        }

    }

    private class MyClickListener implements View.OnClickListener {

        private int index;

        public MyClickListener(int index) {
            // TODO Auto-generated constructor stub
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //改变ViewPager当前显示页面
            pager.setCurrentItem(index);
        }
    }

    //初始化ViewPager中显示的数据
    public void initView() {
        // TODO Auto-generated method stub
        views = new ArrayList<View>();
        LayoutInflater li = getActivity().getLayoutInflater();
        views.add(li.inflate(R.layout.mod_chat, null));
        views.add(li.inflate(R.layout.mod_discover, null));
        views.add(li.inflate(R.layout.mod_contact, null));
    }


    public void initViewPager() {
        // TODO Auto-generated method stub

        PagerAdapter adapter = new MyPagerAdapter();
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                // TODO Auto-generated method stub
                for (int i = 0; i < tvs.size(); i++) {
                    if (i == index) {
                        tvs.get(i).setTextColor(Color.BLUE);
                    } else {
                        tvs.get(i).setTextColor(Color.rgb(55, 55, 55));
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        //有多少个切换页
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }

        //对超出范围的资源进行销毁
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            //super.destroyItem(container, position, object);
            container.removeView(views.get(position));
        }

        //对显示的资源进行初始化
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            //return super.instantiateItem(container, position);
            container.addView(views.get(position));
            return views.get(position);
        }
    }
}
