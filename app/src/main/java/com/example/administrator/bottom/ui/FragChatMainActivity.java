package com.example.administrator.bottom.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.net.DownloadHXFriends;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/11 0011.
 */

public class FragChatMainActivity extends Fragment {
//    private TextView unreadLabel;
    private Button[] mTabs;
    private EaseConversationListFragment conversationListFragment;
    private EaseContactListFragment contactListFragment;
//    private SettingsFragment settingFragment;
    private android.support.v4.app.Fragment[] fragments;
    private int index;
    private int currentTabIndex;

    private final String TAG = "FragChatMain";

    protected InputMethodManager inputMethodManager;

    //    protected void onCreate(Bundle arg0) {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aty_chat_main, container, false);

        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        // should be in launcher activity, but all app use this can avoid the problem
        if (!getActivity().isTaskRoot()) {
            Intent intent = getActivity().getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                getActivity().finish();
                return view;
            }
        }
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        /*
        *  以上用于避免extends EaseBaseActivity，已经将EaseBaseActivity中的有价值内容复制过来了
        * */

        // 控件绑定
//        unreadLabel = (TextView) view.findViewById(R.id.unread_msg_number);
        // 用数组来存放三个按钮
        mTabs = new Button[3];
        // 三个按钮用来跳转到各自的fragment
        // 会话按钮
        mTabs[0] = (Button) view.findViewById(R.id.btn_conversation);
        // 联系人列表按钮
        mTabs[1] = (Button) view.findViewById(R.id.btn_address_list);
        // 设置按钮
//        mTabs[2] = (Button) view.findViewById(R.id.btn_setting);

        // set first tab as selected
        mTabs[0].setSelected(true);
        mTabs[0].setBackgroundResource(R.drawable.item_sublime_text);

        for (int i = 0; i < 2; i++) {
            mTabs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTabClicked(v);
                }
            });
        }

        conversationListFragment = new EaseConversationListFragment();
        contactListFragment = new EaseContactListFragment();
//        settingFragment = new SettingsFragment();
        contactListFragment.setContactsMap(getContacts());

        // 设置会话列表里的会话点击事件，就是点击会话之后的处理，即弹出会话窗口，会话窗口类是ChatActivity
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
//                getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
            }
        });

        // 设置联系人列表里的点击事件，点击联系人之后弹出会话窗口
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
//                getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
            }
        });

        // 将三个fragment存入数组
        fragments = new android.support.v4.app.Fragment[]{conversationListFragment, contactListFragment};
        // add and show first fragment
        getChildFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
                .add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(conversationListFragment)
                .commit();
        return view;
    }

    // 可移植到fragment

    /**
     * onTabClicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;
            case R.id.btn_address_list:
                index = 1;
                break;
//            case R.id.btn_setting:
//                index = 2;
//                break;
        }
        // 如果选中的页面和当前页面不一致
        if (currentTabIndex != index) {
            FragmentTransaction trx = getChildFragmentManager().beginTransaction();
            // 将当前页面隐藏
            trx.hide(fragments[currentTabIndex]);
            mTabs[currentTabIndex].setBackgroundResource(Color.TRANSPARENT);
            // 如果选中的页面还没有被添加到transaction里面，那么就进行添加
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            // 呈现选中的页面
            trx.show(fragments[index]).commit();
            mTabs[index].setBackgroundResource(R.drawable.item_sublime_text);
        }
        // 将当前页面（跳转之前的页面）的选中状态设置为false
        mTabs[currentTabIndex].setSelected(false);
        // set current tab as selected.
        // 将要跳转到的页面的选中状态设置为true
        mTabs[index].setSelected(true);
        // 将当前页面编号设置为要跳转页面的编号
        currentTabIndex = index;
    }

    /**
     * prepared users, password is "123456"
     * you can use these user to test
     *
     * @return
     */
    private Map<String, EaseUser> getContacts() {
        final Map<String, EaseUser>[] arrContacts = new HashMap[1];
        Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
        for (int i = 1; i <= 10; i++) {
            EaseUser user = new EaseUser("easeuitest" + i);
            contacts.put("easeuitest" + i, user);
        }

        new DownloadHXFriends(Config.getCachedPhoneNum(getActivity()), new DownloadHXFriends.SuccessCallback() {
            @Override
            public void onSuccess(ArrayList<String> friendsName) {
                arrContacts[0] = new HashMap<String, EaseUser>();
                for (int i = 0; i < friendsName.size(); i++) {
                    EaseUser user = new EaseUser(friendsName.get(i));
                    arrContacts[0].put(user.getUsername(), user);
                    Log.i(TAG, "write arrContacts");
                    String fname = arrContacts[0].get(user.getUsername()).getUsername();
                    Log.i(TAG, "friend name is " + fname);
                }
                contactListFragment.setContactsMap(arrContacts[0]);
            }
        }, new DownloadHXFriends.FailCallback() {
            @Override
            public void onFail() {

            }
        });

        return arrContacts[0];
    }

    @Override
    public void onResume() {
        super.onResume();
        // cancel the notification
        EaseUI.getInstance().getNotifier().reset();
        onFragChatListener.onConversationClicked(0);
    }

    private OnFragChatListener onFragChatListener;

    public interface OnFragChatListener {
        void onConversationClicked(int responseCode);
    }

    public void setOnFragChatListener(OnFragChatListener onFragHomeListener) {
        this.onFragChatListener = onFragHomeListener;
    }
}
