package com.hyphenate.easeui;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final String SERVER_URL = "http://101.132.190.102:8080/TestServer/api.jsp";
    public static final String SERVER_URL_DOWNLOADPORTRAIT = "http://101.132.190.102:8080/TestServer/DownloadPics.jsp";
    public static final String SERVER_URL_UPLOADPORTRAIT = "http://101.132.190.102:8080/TestServer/UploadPics.jsp";
    public static final String SERVER_URL_UPLOADPORTRAITNAME = "http://101.132.190.102:8080/TestServer/UploadPicsName.jsp";
    public static final String SERVER_URL_PORTRAITPATH = "http://101.132.190.102:8080/TestServer/img/";
//    public static final String SERVER_URL = "http://10.0.171.71:8080/TestServer/api.jsp";

//    public static final String SERVER_URL = "http://172.20.10.8:8080/TestServer/api.jsp";

    public static final String PORTRAITPATH = "portrait_path";

    public static final String DEVICEID = "deviceid";
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

    public static final String KEY_PORTRAIT = "portrait";

    // 环信Config
    public static final String KEY_HX_PORTRAIT = "hx_portrait";
    public static final String KEY_HX_USERNAME = "hx_username";
    public static final String KEY_HX_NIKENAME = "hx_nickname";
    public static final String KEY_HX_PASSWORD = "hx_password";
    public static final String KEY_HX_MYNAME = "hx_myname";
    public static final String KEY_HX_FRIENDNAME = "hx_friendname";
    public static final String KEY_HX_FRIENDSNAME = "hx_friendsname";

    public static final int HX_USERID = 1;
    public static final int HX_NICKNAME = 2;
    public static final int HX_PORTRAIT = 3;
    public static final int HX_UNRADMSGCOUNT = 4;

    public static final int REQUEST_READ_PHONE_STATE = 1;
    public static final int RESULT_STATUS_SUCCESS = 1;
    public static final int RESULT_STATUS_FAIL = 0;
    public static final int RESULT_STATUS_INVALID_TOKEN = 2;

    public static final String APP_ID = "com.charles.secret";
    public static final String CHARSET = "utf-8";

    public static final String ACTION_UPLOAD_HXFRIEND = "upload_hxfriend";

    public static final String ACTION_DOWNLOAD_PORTRAIT = "download_portrait";
    public static final String ACTION_DOWNLOAD_ADDRESS = "download_address";
    public static final String ACTION_DOWNLOAD_ORDERS = "download_orders";
    public static final String ACTION_DOWNLOAD_WAITING_ORDERS = "download_waiting_orders";
    public static final String ACTION_DOWNLOAD_TAKEN_ORDERS = "download_taken_orders";
    public static final String ACTION_DOWNLOAD_ONE_ORDER = "download_one_order";
    public static final String ACTION_DOWNLOAD_HXFRIENDS = "download_hxfriends";
    public static final String ACTION_DOWNLOAD_HX_CONTACT = "download_hx_contact";
    public static final String ACTION_DELETE_ORDER = "delete_order";
    public static final String ACTION_TIMELINE = "timeline";
    public static final String ACTION_PUBLISH = "publish";
    public static final String ACTION_GET_COMMENT = "get_comment";
    public static final String ACTION_REGIST = "regist";
    public static final String ACTION_COMPLETE_ORDER = "complete_order";

    public static final int DELAYMILLIS = 500;

    public static final String KEY_SAVED_ADDRESS = "saved_address";

    public static final int ACTIVITY_RESULT_NEED_REFRESH = 10000;

    //if user has already login , then loginStatue = 1
    public static int loginStatus = 0;

    public static Map<String, String> contactPortraitList;

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
        e.putString(KEY_SAVED_ADDRESS, token);
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

    public static void cacheDeviceID(Context context, String deviceID) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(DEVICEID, deviceID);
        e.commit();
    }

    public static String getCachedDeviceID(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(DEVICEID, null);
    }

    public static void cachePortraitPath(Context context, String path) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(PORTRAITPATH, path);
        e.commit();
    }

    public static String getCachedPortraitPath(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(PORTRAITPATH, null);
    }

    public static void cachePreference(Context context, String preference, String value) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(preference, value);
        e.apply();
    }

    public static String getCachedPreference(Context context, String preference) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(preference, null);
    }

    public static void resetContactPortraitList() {
        contactPortraitList = new HashMap<>();
    }

    public static void putContactPortraitList(String key, String value) {
        if (contactPortraitList != null) {
            contactPortraitList.put(key, value);
        }
    }

    public static String getContactPortrait(String key) {
        if (contactPortraitList != null) {
            return contactPortraitList.get(key);
        }
        return null;
    }

    public static Map<String, String> getContactPortraitList() {
        return contactPortraitList;
    }

}
