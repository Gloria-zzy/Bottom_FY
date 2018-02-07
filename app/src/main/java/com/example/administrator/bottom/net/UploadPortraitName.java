package com.example.administrator.bottom.net;

import com.example.administrator.bottom.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/2/6 0006.
 */

public class UploadPortraitName {
    public UploadPortraitName(String phone, String fileName,final UploadPortraitName.SuccessCallback successCallback, final UploadPortraitName.FailCallback failCallback) {
        new NetConnection(Config.SERVER_URL_UPLOADPORTRAITNAME, HttpMethod.POST, new NetConnection.SuccessCallback() {

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
                            if (successCallback != null) {
                                successCallback.onSuccess();
                            }
                            break;
                        default:
                            if (failCallback != null) {
                                failCallback.onFail();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new NetConnection.FailCallback() {

            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail();
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_UPLOAD_PORTRAIT, Config.KEY_PHONE_NUM, phone, Config.KEY_PORTRAIT, fileName);
    }

    public static interface SuccessCallback {
        void onSuccess();
    }

    public static interface FailCallback {
        void onFail();
    }
}