package com.example.administrator.bottom.frag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyuncs.exceptions.ClientException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.alipush.PushMessage;
import com.example.administrator.bottom.atys.AtyAddressMng;
import com.example.administrator.bottom.atys.AtyJoinUs;
import com.example.administrator.bottom.atys.AtyLogin;
import com.example.administrator.bottom.atys.AtyMainFrame;
import com.example.administrator.bottom.atys.AtyAboutUD;
import com.example.administrator.bottom.atys.AtyStaffOnly;
import com.example.administrator.bottom.atys.AtyTrustOrders;
import com.example.administrator.bottom.atys.AtyUnlog;
import com.example.administrator.bottom.net.CompleteOrder;
import com.example.administrator.bottom.net.DownloadPortrait;
import com.example.administrator.bottom.net.UploadPortraitName;
import com.example.administrator.bottom.utils.DownloadUtil;
import com.example.administrator.bottom.utils.UploadUtil;
import com.example.administrator.zxinglibrary.android.CaptureActivity;
import com.example.administrator.zxinglibrary.bean.ZxingConfig;
import com.example.administrator.zxinglibrary.common.Constant;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/29.
 */

public class FragMe extends Fragment implements DownloadUtil.OnDownloadProcessListener, UploadUtil.OnUploadProcessListener {
    private String context;
    private TextView mTextView, phone_num;
    private int REQUEST_CODE_SCAN = 111;
    private TextView result;
    private String IMAGE_UNSPECIFIED = "image/*";
    private ImageView avatar;
    private File portraitFile;
    private String hxPortraitURL;

    private LinearLayout linearLayout_id;
    private TextView textView_id;
    protected boolean hidden;

    final int PHOTO_REQUEST_GALLERY = 1;// 从相册中选择
    final int PHOTO_REQUEST_CUT = 2;// 剪切结果结果
    final int REQUEST_CODE_GETIMAGE_BYSDCARD = 3;// 上传头像
    /**
     * 去上传文件
     */
    protected static final int TO_DOWNLOAD_FILE = 4;

    /**
     * 上传文件响应
     */
    protected static final int DOWNLOAD_FILE_DONE = 5;  //
    protected static final int TO_RRFRESH = 6;  //

    private String SDCARD_MNT = "/mnt/sdcard";
    private String SDCARD = "/sdcard";
    private Uri cropUri;

    private final String TAG = "FragMe";

    private String TOKEN;
    private String PHONE;

    public FragMe() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PHONE = Config.getCachedPhoneNum(getActivity());
        TOKEN = Config.getCachedToken(getActivity());
        View view = inflater.inflate(R.layout.frag_me, container, false);
        phone_num = view.findViewById(R.id.textView);
        avatar = view.findViewById(R.id.iv_avatar);
        linearLayout_id = view.findViewById(R.id.ll_fragMe_id);
        textView_id = view.findViewById(R.id.tv_fragMe_id);
        showPhoneNumber();
        //login btn
        mTextView = view.findViewById(R.id.func_btn);
        Log.i(TAG, "Token:" + TOKEN);
        if (TOKEN == null || TOKEN.equals("")) {
            linearLayout_id.setVisibility(View.GONE);
            mTextView.setText("登录");
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AtyLogin.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            });
        } else if (TOKEN.equals(PHONE)) {
            linearLayout_id.setVisibility(View.VISIBLE);
            mTextView.setText("退出登录");
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 退出登录时清除本地保存的Token，避免下次启动时获取到之前用户的Token
                    Config.cacheToken(getActivity(), "");
                    // 退出登录时将路径清空，避免更换账号登录时获取了之前账号的头像，这是唯一需要清空头像路径的地方
                    Config.cachePortraitPath(getActivity(), "");
                    // 退出云通信账号
                    EMClient.getInstance().logout(true, new EMCallBack() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onProgress(int progress, String status) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onError(int code, String message) {
                            // TODO Auto-generated method stub

                        }
                    });
                    Config.loginStatus = 0;
                    // 重启这个Activity
//                    Intent intent = new Intent(getActivity(), AtyMainFrame.class);
                    Intent intent = getActivity().getIntent();
                    intent.putExtra("page", "me");
                    getActivity().finish();
                    startActivity(intent);
                }
            });

            // 获取本地头像地址
            String uri;
            uri = Config.getCachedPreference(getActivity(), Config.KEY_HX_PORTRAIT + Config.getCachedPhoneNum(getActivity()));

            FileInputStream fis = null;
            if (uri != null && uri != "") {
//
//                try {
//                    fis = new FileInputStream(new File(uri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                // 压缩图片
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
//                Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);
//                if (bitmap != null) {
//                    Log.i(TAG, "avatar width: " + FragMe.this.avatar.getWidth());
//                    Log.i(TAG, "avatar height: " + FragMe.this.avatar.getHeight());
//                this.avatar.setImageBitmap(bitmap);
                Glide.with(getActivity()).load(Config.getCachedPreference(getActivity(), Config.KEY_HX_PORTRAIT + PHONE)).into(avatar);
//                }
            } else {
                // 本地头像地址不存在，获取服务器端头像，并设置头像
                Log.i("no_portrait_path", "here");

                handler.sendEmptyMessage(TO_DOWNLOAD_FILE);
            }
        }

        // 绑定地址管理事件
        view.findViewById(R.id.address_mng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Config.loginStatus == 0)
//                {
//                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
//                } else
                {
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);

                    if (TOKEN != null && !TOKEN.equals("") && TOKEN.equals(PHONE)) {
                        Intent intent = new Intent(getActivity(), AtyAddressMng.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                    } else {
                        Intent intent = new Intent(getActivity(), AtyUnlog.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                    }

                }
            }
        });

        // 绑定按钮到员工通道
        view.findViewById(R.id.ll_fragMe_staffOnly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AtyStaffOnly.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
            }
        });

        // 绑定按钮到加入UDers
        view.findViewById(R.id.ll_fragMe_joinUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AtyJoinUs.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
            }
        });

//        // 绑定按钮到扫描二维码
////        result = view.findViewById(R.id.result_tv);
//        view.findViewById(R.id.ll_fragMe_staffOnly).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AndPermission.with(getActivity())
//                        .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
//                    @Override
//                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
//
//                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
//
//                                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
//                                * 也可以不传这个参数
//                                * 不传的话  默认都为默认不震动  其他都为true
//                                * */
//
//                        ZxingConfig config = new ZxingConfig();
//                        config.setPlayBeep(false);
//                        config.setShake(true);
//                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//
//                        startActivityForResult(intent, REQUEST_CODE_SCAN);
//
//                    }
//
//                    @Override
//                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//                        Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        startActivity(intent);
//
//                        Toast.makeText(getActivity(), "没有权限无法扫描", Toast.LENGTH_LONG).show();
//                    }
//
//                }).start();
//
//            }
//
//        });

        // 信任订单
        view.findViewById(R.id.tv_trust_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                if (TOKEN != null && !TOKEN.equals("") && TOKEN.equals(PHONE)) {
                    Intent intent = new Intent(getActivity(), AtyTrustOrders.class);
                    intent.putExtra("TAG","init");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                } else {
                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            }
        });

        // 绑定按钮到选择相册中图片
        view.findViewById(R.id.tv_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TOKEN != null && !TOKEN.equals("") && TOKEN.equals(PHONE)) {

                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                    startActivityForResult(intent, PHOTO_REQUEST_CUT);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);

                } else {
                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TOKEN != null && !TOKEN.equals("") && TOKEN.equals(PHONE)) {

                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                    startActivityForResult(intent, PHOTO_REQUEST_CUT);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);

                } else {
                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            }
        });

        // 制作团队
        view.findViewById(R.id.team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AtyAboutUD.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
            }
        });

        return view;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_DOWNLOAD_FILE:
                    // 本地头像不存在，获取服务器端头像
                    Log.i("no_portrait", "here");
                    // 从服务器获取头像文件名
                    new DownloadPortrait(Config.getCachedPhoneNum(getActivity()), new DownloadPortrait.SuccessCallback() {
                        @Override
                        public void onSuccess(String portrait) {
//                            // 检查写入文件权限
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                                getActivity().requestPermissions(new String[]{
//                                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                                }, 1);
//                            }
                            // 获得文件名后向服务器请求下载头像文件
                            DownloadUtil downloadUtil = new DownloadUtil();
                            downloadUtil.setOnDownloadProcessListener(FragMe.this);
                            downloadUtil.downLoad(Config.SERVER_URL_PORTRAITPATH + portrait, portrait);
                            // 同时将图片的URL保存为环信头像（此时没有上传头像，因此hxPortraitPath不存在）
                            Config.cachePreference(getActivity(), Config.KEY_HX_PORTRAIT + PHONE, Config.SERVER_URL_PORTRAITPATH + portrait);
                            Log.i("Handler", "download Pics and the PortraitURL:" + Config.SERVER_URL_PORTRAITPATH + portrait);
                        }
                    }, new DownloadPortrait.FailCallback() {
                        @Override
                        public void onFail() {
                            // 获取文件名失败
                            Log.i("Server_portrait", "get portrait failed");
                        }
                    });
                    break;

                case DOWNLOAD_FILE_DONE:
                    //响应返回的结果
                    if (msg.arg1 == DownloadUtil.DOWNLOAD_SUCC) {
                        String path = (String) msg.obj;
                        // 将头像路径保存在本地，便于下次登录时使用（这个更新的前提是本地没有已保存的路径，既然需要从服务器下载就决定了这一点）
                        // 同时在退出登录时需要清空本地保存的路径，因为该路径不支持多用户，只保存了一个用户的头像路径
                        Config.cachePortraitPath(getActivity(), path);

//                        FileInputStream fis = null;
//                        try {
//                            fis = new FileInputStream(new File(path));
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }

                        Glide.with(getActivity()).load(Config.getCachedPreference(getActivity(), Config.KEY_HX_PORTRAIT + PHONE)).into(avatar);
                    } else if (msg.arg1 == DownloadUtil.DOWNLOAD_FAIL) {
                        try {
                            Glide.with(getActivity()).load(Config.getCachedPreference(getActivity(), Config.KEY_HX_PORTRAIT + PHONE)).into(avatar);
                        } catch (Exception e) {
                            Log.i(TAG, "no portrait URL");
                        }
                    }
                    break;
                case TO_RRFRESH:
                    Drawable drawable = null;
                    try {
                        FileInputStream fis = new FileInputStream(Config.getCachedPortraitPath(getActivity()));
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        avatar.setImageBitmap(bitmap);
                        drawable = new BitmapDrawable(getResources(), bitmap);
                    } catch (FileNotFoundException e) {
                        Log.i(TAG, "portrait file does not exist");
                        e.printStackTrace();
                    }

                    Log.i(TAG, "img toString:" + avatar.toString());

                    Drawable.ConstantState state = avatar.getDrawable().getCurrent().getConstantState();

//                    if (!state.equals(drawable.getConstantState())) {
//                        Log.i(TAG, "avatar is null");
//                        Glide.with(getActivity()).load("android.resource://com.example.administrator.bottom/drawable/" + R.drawable.item_head).into(avatar);
//                    }

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void showPhoneNumber() {
        // 显示用户的手机号（在用户的头像旁边）
        String phone = Config.getCachedPhoneNum(getActivity());

        if (TOKEN != null && !TOKEN.equals("") && TOKEN.equals(PHONE)) {
            phone_num.setText(phone);
            textView_id.setText(phone);
        } else {
            phone_num.setText("未登录");
        }
    }

    // 处理startActivityForResult的返回值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imgReturnIntent) {
        super.onActivityResult(requestCode, resultCode, imgReturnIntent);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (imgReturnIntent != null) {
                //-------------------下单成功 给自己发一条推送-----------------------

                Runnable networkTask = new Runnable() {

                    @Override
                    public void run() {
                        // TODO
                        // 在这里进行 http request.网络请求相关操作
                        PushMessage pushMessage = new PushMessage();
                        try {
                            pushMessage.PushToSelf(Config.getCachedDeviceID(getActivity()), "下单成功！", "UDers正在努力派送中…");
                        } catch (ClientException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(networkTask);
                thread.start();
                //---------------------------推送结束-----------------------------

                String content = imgReturnIntent.getStringExtra(Constant.CODED_CONTENT);
//                result.setText("扫描结果为：" + content);
                new CompleteOrder(content, new CompleteOrder.SuccessCallback() {

                    @Override
                    public void onSuccess() {

                        Toast.makeText(getActivity(), "完成订单！", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), AtyMainFrame.class);
                        i.putExtra("page", "me");
                        startActivity(i);
                    }
                }, new CompleteOrder.FailCallback() {

                    @Override
                    public void onFail() {
                        Toast.makeText(getActivity(), R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (imgReturnIntent != null) {
                // 得到图片的全路径
                Uri uri = imgReturnIntent.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从相册返回的数据
            if (imgReturnIntent != null) {
                // 得到图片的全路径
                Uri uri = imgReturnIntent.getData();
                crop(uri);
            }

        } else if (requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD) {
            // 剪裁结束上传头像
            String portraitPath = portraitFile.getAbsolutePath();

            // 上传并设置好头像之后，把保存头像的路径写入本地文件中（更新头像的需要，这个路径更新是上传时更新，和下载头像时的路径更新不重叠）
            Config.cachePortraitPath(getActivity(), portraitPath);

            // 同时将头像的URL（由于选择头像上传，因此hxPortraitPath此时肯定存在）保存到本地
            Config.cachePreference(getActivity(), Config.KEY_HX_PORTRAIT + PHONE, hxPortraitURL);

            toUploadFile();
        }

        super.onActivityResult(requestCode, resultCode, imgReturnIntent);
    }

    protected void toUploadFile() {
        String fileKey = "img";
        UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);
//        uploadUtil.setOnUploadProcessListener(getActivity());  // 设置监听器监听上传状态

        //定义一个Map集合，封装请求服务端时需要的参数
        Map<String, String> params = new HashMap<>();
        //根据服务端需要的自己决定参数
//      params.put("userId", user.getUserId());

        // 如果头像路径存在则上传
        if (portraitFile.exists()) {
            Log.i("AbsolutePath", portraitFile.getAbsolutePath());
            //参数三：请求的url，
            uploadUtil.uploadFile(portraitFile.getAbsolutePath(), fileKey, Config.SERVER_URL_UPLOADPORTRAIT, params);
        }
    }

    private void crop(Uri uri) {
        // 裁剪图片
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("output", this.getUploadTempFile(uri));
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，返回码为REQUEST_CODE_GETIMAGE_BYSDCARD
        startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    //获取保存头像地址的Uri
    private Uri getUploadTempFile(Uri uri) {
        String portraitPath;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图像的文件夹路径
            portraitPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/image/Portrait";
            File saveDir = new File(portraitPath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
        } else {
            Toast.makeText(getActivity(), "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());

        String thePath = getAbsolutePathFromNoStandardUri(uri);
        if (thePath.isEmpty()) {
            thePath = getAbsoluteImagePath(uri);
        }

        //获取图片路径的扩展名，如果扩展名为空，则默认为jpg格式
        String ext = thePath.substring(thePath.lastIndexOf('.') + 1);
        ext = ext.isEmpty() ? "jpg" : ext;

        // 照片命名
        String cropFileName = "crop_" + timeStamp + "." + ext;
        hxPortraitURL = Config.SERVER_URL_PORTRAITPATH + cropFileName;
        Config.cachePreference(getActivity(), Config.KEY_HX_PORTRAIT + PHONE, hxPortraitURL);

        new UploadPortraitName(PHONE, cropFileName, new UploadPortraitName.SuccessCallback() {
            @Override
            public void onSuccess() {
                Log.i("UploadName", "succ");
            }
        }, new UploadPortraitName.FailCallback() {
            @Override
            public void onFail() {
                Log.i("UploadName", "fail");
            }
        });

        portraitFile = new File(portraitPath, cropFileName);
        cropUri = Uri.fromFile(portraitFile);

        return cropUri;
    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     * //     * @param uri
     *
     * @return
     */
    private String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = "";

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre2.length());
        }

        return filePath;
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    @SuppressWarnings("deprecation")
    private String getAbsoluteImagePath(Uri uri) {

        String imagePath = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }
        return imagePath;
    }

    @Override
    public void onDownloadDone(int responseCode, String message) {
        // 当下载类实例返回成功标志时，向Handler发送成功消息，进行头像设置
        Log.i("onDownloadDone", "done");
        Message msg = Message.obtain();
        msg.what = DOWNLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    private void refresh() {
        Log.i(TAG, "Phone:" + PHONE);
        Log.i(TAG, "Avatar:" + Config.getCachedPreference(getActivity(), Config.KEY_HX_PORTRAIT + PHONE));
        Glide.with(getActivity()).load(Config.getCachedPreference(getActivity(), Config.KEY_HX_PORTRAIT + PHONE)).asBitmap()
                .into(new BitmapImageViewTarget(avatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        //Play with bitmap
                        super.setResource(resource);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (TOKEN != null && !TOKEN.equals("") && TOKEN.equals(PHONE)) {
            refresh();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    private int time = 2000;

    @Override
    public void onUploadDone(int responseCode, String message) {
        Log.i(TAG, "upload done and set the avatar");
        handler.sendEmptyMessage(TO_RRFRESH);
    }
}
