package com.example.administrator.bottom.net;

import com.example.administrator.bottom.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateOrder {
    public UpdateOrder(String phone, String order_taker, String order_num , String point, String takenum, String location, String note, String date,String status, final SuccessCallback successCallback, final FailCallback failCallback) {
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
        }, Config.KEY_ACTION, Config.ACTION_UPDATE_ORDER, Config.KEY_ORDER_NUMBER, order_num, Config.KEY_ORDER_TAKER, order_taker,Config.KEY_ORDER_POINT, point, Config.KEY_ORDER_TAKENUM, takenum,Config.KEY_ORDER_LOCATION, location, Config.KEY_ORDER_NOTE, note,Config.KEY_ORDER_DATE,date, Config.KEY_PHONE_NUM, phone,Config.KEY_ORDER_STATUS,status);
    }

    public static interface SuccessCallback {
        void onSuccess();
    }

    public static interface FailCallback {
        void onFail();
    }
}
