package com.hyphenate.easeui.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.Config;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

/**
 * Created by Administrator on 2018/2/21 0021.
 */

public class MyChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper{

    private final String TAG = "MyChatFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChatFragmentHelper(this);
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        Log.i(TAG, "onSetMessageAttributes");
        // 用户登录名
        message.setAttribute(Config.getCachedPreference(getActivity(), Config.KEY_PHONE_NUM), Config.HX_USERID);
        // 用户昵称
        message.setAttribute(Config.getCachedPreference(getActivity(), Config.KEY_HX_NIKENAME), Config.HX_NICKNAME);
        // 用户头像本地路径
        message.setAttribute(Config.getCachedPreference(getActivity(), Config.PORTRAITPATH), Config.HX_PORTRAIT);
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
