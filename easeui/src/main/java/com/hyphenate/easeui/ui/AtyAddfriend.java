package com.hyphenate.easeui.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.Config;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.bean.HXContact;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.net.DownloadAddress;
import com.hyphenate.easeui.net.DownloadHXContact;
import com.hyphenate.easeui.net.UploadHXFriend;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseContactList;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AtyAddfriend extends Activity {

    protected EaseContactList contactListLayout;
    protected FrameLayout contentContainer;
    protected ListView listView;
    protected List<EaseUser> contactList;
    private Map<String, EaseUser> contactsMap;

    private EaseTitleBar titleBar;
    protected EditText query;
    protected ImageButton clearSearch;
    protected FrameLayout errorItemContainer;
    protected InputMethodManager inputMethodManager;
    private Button add;
    private TextView tv_phone;
    private EaseImageView eiv_userhead;
    private String phone;

    private final String TAG = "AtyAddFriend";

    private final int TO_REFRESH = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_addfriend);
//        getSupportActionBar().hide();

        contentContainer = (FrameLayout) findViewById(R.id.content_container);
        contactListLayout = (EaseContactList) findViewById(R.id.contact_list);
        listView = contactListLayout.getListView();

        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        query = (EditText) findViewById(R.id.query);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) findViewById(R.id.fl_error_item);
//        tv_phone = (TextView) findViewById(R.id.tv_atyAddfriend_phone);
//        eiv_userhead = findViewById(R.id.iv_userhead);
        add = (Button)  findViewById(R.id.btn_atyAddfriend_add);

        contactListLayout.setShowSiderBar(false);

        // 初始化内框
        setUpView();

        //---------------------状态栏透明 begin----------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AtyAddfriend.this.getWindow();
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

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                onBackPressed();
                overridePendingTransition(com.hyphenate.easeui.R.transition.switch_still, com.hyphenate.easeui.R.transition.switch_still);
            }
        });

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
                if(s.length() == 11){

                    Log.i(TAG, "user downloading");
                    phone = s.toString();
                    new DownloadHXContact(phone, new DownloadHXContact.SuccessCallback() {
                        @Override
                        public void onSuccess(HXContact hxContact) {
                            // 用户存在
                            Log.i(TAG, "user exist");
                            // 提取联系人
                            Map<String, EaseUser> contacts = new HashMap<>();
                            EaseUser user = new EaseUser(phone);
                            user.setNickname(hxContact.getNickname());
                            user.setAvatar(hxContact.getPortrait());
                            contacts.put(phone, user);
                            // 刷新联系人
                            setContactsMap(contacts);
                            //刷新页面
                            refresh();
                            add.setEnabled(true);
                        }
                    }, new DownloadHXContact.FailCallback() {
                        @Override
                        public void onFail() {
                            Log.i(TAG, "user not exist");
                        }
                    });

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                contentContainer.removeAllViews();
                add.setEnabled(false);
                hideSoftKeyboard();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UploadHXFriend(Config.getCachedPhoneNum(AtyAddfriend.this) ,phone, new UploadHXFriend.SuccessCallback() {

                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "Upload friend succ");
                        finish();
                        hideSoftKeyboard();
                    }
                }, new UploadHXFriend.FailCallback() {

                    @Override
                    public void onFail() {
                        Toast.makeText(AtyAddfriend.this, R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    protected void setUpView() {

        contactList = new ArrayList<>();
        getContactList();
        //init list
        contactListLayout.init(contactList);

        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }

    private void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // refresh ui
    public void refresh() {
        Log.i(TAG, "refresh()");
        getContactList();
    }

    /**
     * get contact list and sort, will filter out users in blacklist
     */
    protected void getContactList() {
        contactList.clear();
        if(contactsMap == null){
            return;
        }
        synchronized (this.contactsMap) {
            Iterator<Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Entry<String, EaseUser> entry = iterator.next();
                // to make it compatible with data in previous version, you can remove this check if this is new app
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")){
                    if(!blackList.contains(entry.getKey())){
                        //filter out users in blacklist
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        contactList.add(user);
                    }
                }
            }
            contactListLayout.init(contactList);
            handler.sendEmptyMessage(TO_REFRESH);
        }

        // sorting
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNick().compareTo(rhs.getNick());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });
    }

    /**
     * set contacts map, key is the hyphenate id
     * @param contactsMap
     */
    public void setContactsMap(Map<String, EaseUser> contactsMap){
        this.contactsMap = contactsMap;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_REFRESH:
                    Log.i(TAG, "handler refresh");
                    contactListLayout.refresh();
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
