/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.Config;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.bean.HXContact;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.net.DownloadHXContact;
import com.hyphenate.easeui.net.DownloadHXFriends;
import com.hyphenate.easeui.net.DownloadPortrait;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseContactList;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * contact list
 */
public class EaseContactListFragment extends EaseBaseFragment {
    private static final String TAG = "EaseContactListFragment";
    protected List<EaseUser> contactList;
    protected ListView listView;
    protected boolean hidden;
    protected ImageButton clearSearch;
    protected EditText query;
    protected EaseUser toBeProcessUser;
    protected String toBeProcessUsername;
    protected EaseContactList contactListLayout;
    protected boolean isConflict;
    protected FrameLayout contentContainer;
    private ImageView add;

    private Map<String, EaseUser> contactsMap;

    private final int REQUEST_CODE_REFRESH = 1;
    private final int MSG_REFRESH_PORTRAITLIST = 2;
    private final int MSG_REFRESH_VIEW = 3;

    private int countPortrait;
    private boolean needRefreshView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //to avoid crash when open app after long time stay in background after user logged into
        // another device
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        contentContainer = (FrameLayout) getView().findViewById(R.id.content_container);

        contactListLayout = (EaseContactList) getView().findViewById(R.id.contact_list);
        // listView存储contactListLayout里的元素
        listView = contactListLayout.getListView();

        //search
        query = getView().findViewById(R.id.query);
        clearSearch = getView().findViewById(R.id.search_clear);

        add = getView().findViewById(R.id.iv_fragContact_add);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setUpView() {

        //不显示title bar
        this.hideTitleBar();

        //点击加号转到add friend
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AtyAddfriend.class);
                startActivityForResult(intent, REQUEST_CODE_REFRESH);
//                getActivity().overridePendingTransition(R.transition.switch_slide_in_top, R
// .transition.switch_still);
            }
        });

        EMClient.getInstance().addConnectionListener(connectionListener);

        contactList = new ArrayList<>();
        getContactList();
        //init list
        contactListLayout.init(contactList);

        if (listItemClickListener != null) {
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EaseUser user = (EaseUser) listView.getItemAtPosition(position);
                    listItemClickListener.onListItemClicked(user);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                               long id) {
                    EaseUser user = (EaseUser) listView.getItemAtPosition(position);
                    listItemClickListener.onListItemLongClicked(user);
                    return true;
                }
            });
        }

        // 对搜索框添加监听器
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactListLayout.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
            refreshContactList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
            refreshContactList();
        }
    }


    /**
     * move user to blacklist
     */
    protected void moveToBlacklist(final String username) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st1 = getResources().getString(R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //move to blacklist
                    EMClient.getInstance().contactManager().addUserToBlackList(username, false);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st3, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    // refresh ui
    public void refresh() {
        Log.i(TAG, "refresh()");
        getContactList();
        contactListLayout.refresh();
        // 更新界面
    }


    @Override
    public void onDestroy() {

        EMClient.getInstance().removeConnectionListener(connectionListener);

        super.onDestroy();
    }

    /**
     * get contact list and sort, will filter out users in blacklist
     */
    protected void getContactList() {
        Log.i(TAG, "getContactList");
        contactList.clear();
        if (contactsMap == null) {
            Log.i(TAG, "comtactsMap == null");
            return;
        }
        synchronized (this.contactsMap) {
            Iterator<Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager()
                    .getBlackListUsernames();
            while (iterator.hasNext()) {
                Log.i(TAG, "in synchronized");
                Entry<String, EaseUser> entry = iterator.next();
                // to make it compatible with data in previous version, you can remove this check
                // if this is new app
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")) {
                    if (!blackList.contains(entry.getKey())) {
                        //filter out users in blacklist
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        contactList.add(user);
                    }
                }
            }
        }

        // sorting
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                    Log.i(TAG, "in sort");
                    return lhs.getNick().compareTo(rhs.getNick());
                } else {
                    if ("#".equals(lhs.getInitialLetter())) {
                        return 1;
                    } else if ("#".equals(rhs.getInitialLetter())) {
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });

    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE ||
                    error == EMError.SERVER_SERVICE_RESTRICTED) {
                isConflict = true;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        onConnectionDisconnected();
                    }

                });
            }
        }

        @Override
        public void onConnected() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    onConnectionConnected();
                }

            });
        }
    };
    private EaseContactListItemClickListener listItemClickListener;


    protected void onConnectionDisconnected() {

    }

    protected void onConnectionConnected() {

    }

    /**
     * set contacts map, key is the hyphenate id
     *
     * @param contactsMap
     */
    public void setContactsMap(Map<String, EaseUser> contactsMap) {
        Log.i(TAG, "setContactsMap");
        this.contactsMap = contactsMap;
    }

    public interface EaseContactListItemClickListener {
        /**
         * on click event for item in contact list
         *
         * @param user --the user of item
         */
        void onListItemClicked(EaseUser user);

        void onListItemLongClicked(EaseUser user);
    }

    /**
     * set contact list item click listener
     *
     * @param listItemClickListener
     */
    public void setContactListItemClickListener(EaseContactListItemClickListener
                                                        listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REFRESH) {
            Log.i(TAG, "refresh");
            refreshContactList();
        }
    }

    private void refreshContactList() {
        new DownloadHXFriends(Config.getCachedPhoneNum(getActivity()), new DownloadHXFriends
                .SuccessCallback() {
            @Override
            public void onSuccess(ArrayList<String> friendsName) {
                final Map<String, EaseUser>[] arrContacts = new HashMap[1];
                arrContacts[0] = new HashMap<>();
                for (int i = 0; i < friendsName.size(); i++) {
                    EaseUser user = new EaseUser(friendsName.get(i));
                    arrContacts[0].put(user.getUsername(), user);
                    Log.i(TAG, "write arrContacts");
                    String fname = arrContacts[0].get(user.getUsername()).getUsername();
                    Log.i(TAG, "friend name is " + fname);
                }
                setContactsMap(arrContacts[0]);
                handler.sendEmptyMessage(MSG_REFRESH_PORTRAITLIST);
            }
        }, new DownloadHXFriends.FailCallback() {
            @Override
            public void onFail() {

            }
        });
    }

    // 若联系人有增减或联系人头像有变化则刷新界面
    private void refreshPortraitList() {
        Config.resetContactPortraitList();
        countPortrait = 0;
        needRefreshView = false;
        Iterator<Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, EaseUser> entry = iterator.next();
            EaseUser eu = entry.getValue();
            final String username = eu.getUsername();
            // 不能当头像有变化时下载。你咋知道有没有变化？那不还是得问问服务器有没有变化吗，所以还得全部下载下来，加个判断，要是和原来没两样就不用刷新了

            new DownloadHXContact(username, new DownloadHXContact.SuccessCallback() {
                @Override
                public void onSuccess(HXContact hxContact) {
                    // 下载成功的头像个数+1
                    countPortrait++;
                    String portrait = hxContact.getPortrait();
                    String nickname = hxContact.getNickname();
                    Config.putContactPortraitList(username, Config.SERVER_URL_PORTRAITPATH +
                            portrait);
                    // 当头像路径变化时，缓存（sharedPreference）头像新路径
                    if ((portrait != null && !portrait.equals("null") && !portrait.equals(Config
                            .getCachedPreference(getActivity(), Config.KEY_HX_PORTRAIT +
                                    username))) || (nickname != null && !nickname.equals("null") &&
                            !nickname.equals(Config.getCachedPreference(getActivity(), Config
                                    .KEY_HX_NIKENAME + username)))) {
                        // 存储昵称
                        Config.cachePreference(getActivity(), Config.KEY_HX_NIKENAME + username,
                                nickname);
                        // 存储头像文件名
                        Config.cachePreference(getActivity(), Config.KEY_HX_PORTRAIT + username,
                                portrait);
                        needRefreshView = true;
                    }
                    // 当‘头像变化标志’不为0，且所有头像都下载完毕时刷新页面
                    if (countPortrait == contactsMap.size() && needRefreshView) {
                        Log.i(TAG, "countPortrait:" + countPortrait);
                        handler.sendEmptyMessage(MSG_REFRESH_VIEW);
                    }
                }
            }, new DownloadHXContact.FailCallback() {
                @Override
                public void onFail() {

                }
            });
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_PORTRAITLIST:
                    refreshPortraitList();
                    break;
                case MSG_REFRESH_VIEW:
                    refresh();
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
