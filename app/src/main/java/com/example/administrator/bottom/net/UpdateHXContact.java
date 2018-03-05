package com.example.administrator.bottom.net;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.bean.HXContact;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/3/5 0005.
 */

public class UpdateHXContact {
    public UpdateHXContact(HXContact hxContact, final UpdateHXContact.SuccessCallback successCallback, final UpdateHXContact.FailCallback failCallback) {

        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                successCallback.onSuccess();
                            }
                            break;

                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if (failCallback != null) {
                                failCallback.onFail();
                            }
                            break;

                        default:
                            if (failCallback != null) {
                                failCallback.onFail();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (failCallback != null) {
                        failCallback.onFail();
                    }
                }
            }
        }, new NetConnection.FailCallback() {

            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail();
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_UPDATE_HX_CONTACT, Config.KEY_HX_MYNAME, hxContact.getUsername(), Config.KEY_PORTRAIT, hxContact.getPortrait(), Config.KEY_HX_NICKNAME, hxContact.getNickname());

    }

    public static interface SuccessCallback {
        void onSuccess();
    }

    public static interface FailCallback {
        void onFail();
    }
}
