package com.example.administrator.bottom.atys;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.bottom.R;
import com.example.administrator.bottom.frag.FragCommunity;
import com.example.administrator.bottom.frag.FragHome;
import com.example.administrator.bottom.frag.FragMe;
import com.example.administrator.bottom.frag.FragOrder;

/**
 * Created by Administrator on 2017/10/31.
 */

public class AtyMainFrame extends Activity implements View.OnClickListener {

    private TextView tabHome;
    private TextView tabGet;
    private TextView tabPost;
    private TextView tabMe;

    private FrameLayout ly_content;

    private Fragment[] fragments = new Fragment[4];
    private FragHome fragHome;
    private FragOrder fragOrder;
    private FragCommunity fragCommunity;
    private FragMe fragMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bindView();

//        getSupportActionBar().hide();

        //      load page!!!
//        showFragHome();
        Intent intent = getIntent();
        setIntent(intent);
        String page = intent.getStringExtra("page");
        bindView();

        // by Charles


//        Toast.makeText(AtyMainFrame.this, page, Toast.LENGTH_LONG).show();
        if (page != null) {
            if (page.equals("home")) {
                showFragHome();
            } else if (page.equals("order")) {
                showFragOrder();
            } else if (page.equals("community")) {
//                showFragCommunity();
            } else if (page.equals("me")) {
                showFragMe();
            } else {
                System.out.println("hellooooooooo:" + page + "1111");
                showFragHome();
            }
        }
    }

    //UI组件初始化与事件绑定
    private void bindView() {
//        topBar = (TextView)this.findViewById(R.id.txt_top);
        tabHome = (TextView) this.findViewById(R.id.txt_home);
        tabGet = (TextView) this.findViewById(R.id.txt_get);
//        tabPost = (TextView) this.findViewById(R.id.txt_post);
        tabMe = (TextView) this.findViewById(R.id.txt_me);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        tabHome.setOnClickListener(this);
        tabGet.setOnClickListener(this);
//        tabPost.setOnClickListener(this);
        tabMe.setOnClickListener(this);

    }

    //重置所有文本的选中状态
    public void selected() {
        tabHome.setSelected(false);
        tabGet.setSelected(false);
//        tabPost.setSelected(false);
        tabMe.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (fragHome != null) {
            transaction.hide(fragHome);
        }
        if (fragOrder != null) {
            transaction.hide(fragOrder);
        }
        if (fragCommunity != null) {
            transaction.hide(fragCommunity);
        }
        if (fragMe != null) {
            transaction.hide(fragMe);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.txt_home:
                selected();
                tabHome.setSelected(true);
                if (fragHome == null) {
                    fragHome = new FragHome();
                    fragHome.fresh();
                    transaction.add(R.id.fragment_container, fragHome);
                } else {
                    fragHome.fresh();
                    transaction.show(fragHome);
                }
                break;

            case R.id.txt_get:
                selected();
                tabGet.setSelected(true);
                if (fragOrder == null) {
                    fragOrder = new FragOrder();
                    fragOrder.fresh();
                    transaction.add(R.id.fragment_container, fragOrder);
                } else {
                    fragOrder.fresh();
                    transaction.show(fragOrder);

                }
                break;

            case R.id.txt_me:
                selected();
                tabMe.setSelected(true);
                if (fragMe == null) {
                    fragMe = new FragMe();
                    transaction.add(R.id.fragment_container, fragMe);
                } else {
                    transaction.show(fragMe);
                }
                break;
        }

        transaction.commit();
    }

    public static void close() {

    }

    public void showFragHome() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();
        tabHome.setSelected(true);
        fragHome = new FragHome();
        transaction.add(R.id.fragment_container, fragHome);
        transaction.show(fragHome);
        transaction.commit();
        fragHome.fresh();
    }

    public void showFragOrder() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();
        tabGet.setSelected(true);
        fragOrder = new FragOrder();
        transaction.add(R.id.fragment_container, fragOrder);
        transaction.show(fragOrder);
        transaction.commit();
    }

//    public void showFragCommunity() {
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        hideAllFragment(transaction);
//        selected();
//        tabPost.setSelected(true);
//        fragCommunity = new FragCommunity();
//        transaction.add(R.id.fragment_container, fragCommunity);
//        transaction.show(fragCommunity);
//        transaction.commit();
//    }

    public void showFragMe() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();
        tabMe.setSelected(true);
        fragMe = new FragMe();
        transaction.add(R.id.fragment_container, fragMe);
        transaction.show(fragMe);
        transaction.commit();
    }
}
