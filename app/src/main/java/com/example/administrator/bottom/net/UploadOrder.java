package com.example.administrator.bottom.net;

import com.example.administrator.bottom.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadOrder {
    public UploadOrder(String phone, String point, String takenum,String location, String note,String date, final SuccessCallback successCallback, final FailCallback failCallback) {
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
        }, Config.KEY_ACTION, Config.ACTION_UPLOAD_ORDER, Config.KEY_ORDER_POINT, point, Config.KEY_ORDER_TAKENUM, takenum,Config.KEY_ORDER_LOCATION, location, Config.KEY_ORDER_NOTE, note,Config.KEY_ORDER_DATE,date, Config.KEY_PHONE_NUM, phone);
    }

    public static interface SuccessCallback {
        void onSuccess();
    }

    public static interface FailCallback {
        void onFail();
    }
}
