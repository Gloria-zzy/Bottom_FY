package com.example.administrator.bottom;

import android.app.FragmentTransaction;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView topBar;
    private TextView tabHome;
    private TextView tabGet;
//    private TextView tabPost;
    private TextView tabMe;

    private FrameLayout ly_content;

    private FragHome fragHome;
    private FragOrder fragOrder;
    private FragCommunity fragCommunity;
    private FragMe fragMe;
//    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        startActivity(new Intent(MainActivity.this, AtyLogin.class));
        setContentView(R.layout.aty_login);

//        bindView();
//        getSupportActionBar().hide();


// Written by charles
//        String token = Config.getCachedToken(this);
//        String phone_num = Config.getCachedPhoneNum(this);

//        if (token != null && phone_num != null) {
//            Intent i = new Intent(this, AtyTimeline.class);
//            i.putExtra(Config.KEY_TOKEN, token);
//            i.putExtra(Config.KEY_PHONE_NUM, phone_num);
//            startActivity(i);
//        } else {
//            startActivity(new Intent(this, AtyLogin.class));
//        }
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
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

//            case R.id.txt_post:
//                selected();
////                tabPost.setSelected(true);
//                if(fragCommunity ==null){
//                    fragCommunity = new FragCommunity();
//                    transaction.add(R.id.fragment_container, fragCommunity);
//                }else{
//                    transaction.show(fragCommunity);
//                }
//                break;

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