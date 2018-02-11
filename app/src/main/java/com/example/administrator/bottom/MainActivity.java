package com.example.administrator.bottom;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.bottom.atys.AtyLogin;
import com.example.administrator.bottom.frag.FragOrder;
import com.example.administrator.bottom.frag.FragHome;
import com.example.administrator.bottom.frag.FragMe;
import com.example.administrator.bottom.frag.FragCommunity;
import com.example.administrator.bottom.ui.FragChatMainActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView topBar;
    private TextView tabHome;
    private TextView tabGet;
//    private TextView tabPost;
    private TextView tabMe;

    private FrameLayout ly_content;

    private FragHome fragHome;
    private FragOrder fragOrder;
    private FragChatMainActivity fragCommunity;
    private FragMe fragMe;
//    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    //UI组件初始化与事件绑定
    private void bindView() {
//        topBar = (TextView)this.findViewById(R.id.txt_top);
        tabHome = (TextView)this.findViewById(R.id.txt_home);
        tabGet = (TextView)this.findViewById(R.id.txt_get);
//        tabPost = (TextView)this.findViewById(R.id.txt_post);
        tabMe = (TextView)this.findViewById(R.id.txt_me);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        tabHome.setOnClickListener(this);
        tabGet.setOnClickListener(this);
//        tabPost.setOnClickListener(this);
        tabMe.setOnClickListener(this);

    }

    //重置所有文本的选中状态
    public void selected(){
        tabHome.setSelected(false);
        tabGet.setSelected(false);
//        tabPost.setSelected(false);
        tabMe.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(fragHome !=null){
            transaction.hide(fragHome);
        }
        if(fragOrder !=null){
            transaction.hide(fragOrder);
        }
        if(fragCommunity !=null){
            transaction.hide(fragCommunity);
        }
        if(fragMe !=null){
            transaction.hide(fragMe);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.txt_home:
                selected();
                tabHome.setSelected(true);
                if(fragHome ==null){
                    fragHome = new FragHome();
                    transaction.add(R.id.fragment_container, fragHome);
                }else{
                    transaction.show(fragHome);
                }
                break;

            case R.id.txt_get:
                selected();
                tabGet.setSelected(true);
                if(fragOrder ==null){
                    fragOrder = new FragOrder();
                    transaction.add(R.id.fragment_container, fragOrder);
                }else{
                    transaction.show(fragOrder);
                }
                break;

            case R.id.txt_me:
                selected();
                tabMe.setSelected(true);
                if(fragMe ==null){
                    fragMe = new FragMe();
                    transaction.add(R.id.fragment_container, fragMe);
                }else{
                    transaction.show(fragMe);
                }
                break;
        }

        transaction.commit();
    }
}