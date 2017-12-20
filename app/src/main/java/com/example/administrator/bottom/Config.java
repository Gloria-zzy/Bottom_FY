package com.example.administrator.bottom;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

public class Config {

    public static final String SERVER_URL = "http://101.132.190.102:8080/TestServer/api.jsp";
//    public static final String SERVER_URL = "http://10.0.171.71:8080/TestServer/api.jsp";

//    public static final String SERVER_URL = "http://172.20.10.8:8080/TestServer/api.jsp";

    public static final String KEY_TOKEN = "token";
    public static final String KEY_ACTION = "action";
    public static final String KEY_PHONE_NUM = "item_phone";
    public static final String KEY_PHONE_MD5 = "phone_md5";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CODE = "key_code";
    public static final String KEY_CONTACTS = "contacts";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PERPAGE = "perpage";
    public static final String KEY_TIMELINE = "timeline";
    public static final String KEY_ORDERS = "orders";
    public static final String KEY_MSG_ID = "msgId";
    public static final String KEY_MSG = "msg";
    public static final String KEY_COMMENTS = "items";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "item_email";

    public static final String KEY_ADDRESS_SCHOOL = "address_school";
    public static final String KEY_ADDRESS_AREA = "address_area";
    public static final String KEY_ADDRESS_BUILDING = "address_building";
    public static final String KEY_ADDRESS_ROOM = "address_room";

    public static final String KEY_ORDER_NUMBER = "order_number";
    public static final String KEY_ORDER_TAKER = "order_taker";
    public static final String KEY_ORDER_POINT = "order_point";
    public static final String KEY_ORDER_TAKENUM = "order_takenum";
    public static final String KEY_ORDER_LOCATION = "order_location";
    public static final String KEY_ORDER_NOTE = "order_note";
    public static final String KEY_ORDER_DATE = "order_date";
    public static final String KEY_ORDER_STATUS = "order_status";

    public static final int RESULT_STATUS_SUCCESS = 1;
    public static final int RESULT_STATUS_FAIL = 0;
    public static final int RESULT_STATUS_INVALID_TOKEN = 2;

    public static final String APP_ID = "com.charles.secret";
    public static final String CHARSET = "utf-8";

    public static final String ACTION_GET_CODE = "send_pass";
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_UPLOAD_ADDRESS = "upload_address";
    public static final String ACTION_UPLOAD_CONTACTS = "upload_contacts";
    public static final String ACTION_UPLOAD_ORDER = "upload_order";
    public static final String ACTION_UPDATE_ORDER = "update_order";
    public static final String ACTION_UPLOAD_TOKEN = "upload_token";
    public static final String ACTION_DOWNLOAD_ADDRESS = "download_address";
    public static final String ACTION_DOWNLOAD_ORDERS = "download_orders";
    public static final String ACTION_DOWNLOAD_WAITING_ORDERS = "download_waiting_orders";
    public static final String ACTION_DOWNLOAD_TAKEN_ORDERS = "download_taken_orders";
    public static final String ACTION_DOWNLOAD_ONE_ORDER = "download_one_order";
    public static final String ACTION_DELETE_ORDER = "delete_order";
    public static final String ACTION_TIMELINE = "timeline";
    public static final String ACTION_PUBLISH = "publish";
    public static final String ACTION_GET_COMMENT = "get_comment";
    public static final String ACTION_REGIST = "regist";
    public static final String ACTION_COMPLETE_ORDER = "complete_order";

    public static String ADDRESS = "";
    public static Bitmap AVATAR;

    public static final int ACTIVITY_RESULT_NEED_REFRESH = 10000;

    //if user has already login , then loginStatue = 1
    public static int loginStatus = 0;

    public static String getCachedToken(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
    }

    public static void cacheToken(Context context, String token) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(KEY_TOKEN, token);
        e.commit();
    }

    public static void cacheAddress(Context context, String token) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(ADDRESS, token);
        e.commit();
    }

    public static String getCachedPhoneNum(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(KEY_PHONE_NUM, null);
    }

    public static void cachePhoneNum(Context context, String phoneNum) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(KEY_PHONE_NUM, phoneNum);
        e.commit();
    }
}
