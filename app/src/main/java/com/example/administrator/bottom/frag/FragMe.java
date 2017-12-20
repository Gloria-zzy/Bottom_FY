package com.example.administrator.bottom.frag;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bottom.Config;
import com.example.administrator.bottom.R;
import com.example.administrator.bottom.atys.AtyAddressMng;
import com.example.administrator.bottom.atys.AtyLogin;
import com.example.administrator.bottom.atys.AtyMainFrame;
import com.example.administrator.bottom.atys.AtyTakenOrders;
import com.example.administrator.bottom.atys.AtyUnlog;
import com.example.administrator.bottom.net.CompleteOrder;
import com.example.administrator.zxinglibrary.android.CaptureActivity;
import com.example.administrator.zxinglibrary.bean.ZxingConfig;
import com.example.administrator.zxinglibrary.common.Constant;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.administrator.bottom.Config.APP_ID;

/**
 * Created by Administrator on 2017/10/29.
 */

public class FragMe extends Fragment {
    private String context;
    private TextView mTextView, phone_num;
    private int REQUEST_CODE_SCAN = 111;
    private TextView result;
    private String IMAGE_UNSPECIFIED = "image/*";
    private ImageView avatar;

    public FragMe() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_me, container, false);

        avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        //login btn
        mTextView = (TextView) view.findViewById(R.id.func_btn);
        if (Config.loginStatus == 0) {
            mTextView.setText("登录");
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AtyLogin.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            });
        } else {
            mTextView.setText("退出登录");
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Config.loginStatus = 0;
                    Intent intent = new Intent(getActivity(), AtyLogin.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                    mTextView.setText("登录");
                    Config.cacheToken(getActivity(), "");
                }
            });
        }


        //address mng
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
                    if (Config.loginStatus == 1) {
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

        //show phone number!!!!
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString(Config.KEY_PHONE_NUM, "");
        phone_num = (TextView) view.findViewById(R.id.textView);
        if (Config.loginStatus == 1) {
            phone_num.setText(phone);
        } else {
            phone_num.setText("未登录");
        }

        //scanner!!!!
//        result = view.findViewById(R.id.result_tv);
        view.findViewById(R.id.scanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(getActivity())
                        .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(getActivity(), CaptureActivity.class);

                                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                                * 也可以不传这个参数
                                * 不传的话  默认都为默认不震动  其他都为true
                                * */

                        ZxingConfig config = new ZxingConfig();
                        config.setPlayBeep(false);
                        config.setShake(true);
                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);

                        startActivityForResult(intent, REQUEST_CODE_SCAN);

                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        Toast.makeText(getActivity(), "没有权限无法扫描", Toast.LENGTH_LONG).show();
                    }

                }).start();

            }

        });

        view.findViewById(R.id.tv_taken_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                if (Config.loginStatus == 1) {
                    Intent intent = new Intent(getActivity(), AtyTakenOrders.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                } else {
                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }
            }
        });

        view.findViewById(R.id.tv_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Config.loginStatus == 1) {
                    final int PHOTO_REQUEST_GALLERY = 1;//从相册中选择

                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);

                    Bitmap bitmap = intent.getParcelableExtra("data");


                } else {
                    Intent intent = new Intent(getActivity(), AtyUnlog.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.switch_slide_in_right, R.transition.switch_still);
                }

            }
        });

        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
//                result.setText("扫描结果为：" + content);
                new CompleteOrder(content, new CompleteOrder.SuccessCallback() {

                    @Override
                    public void onSuccess() {

                        Toast.makeText(getActivity(),"完成订单！", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), AtyMainFrame.class);
                        i.putExtra("page","me");
                        startActivity(i);
                    }
                }, new CompleteOrder.FailCallback() {

                    @Override
                    public void onFail() {
                        Toast.makeText(getActivity(), R.string.fail_to_commit, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        //选择头像
        int PHOTO_REQUEST_CAREMA = 0;//相机拍照
        final int PHOTO_REQUEST_GALLERY = 1;//从相册中选择
        final int PHOTO_REQUEST_CUT = 2;//剪切结果结果
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                this.avatar.setImageBitmap(bitmap);
            }
//            try {
//                // 将临时文件删除
//                tempFile.delete();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }

        super.onActivityResult(requestCode, resultCode, data);


    }

    private void crop(Uri uri) {
        final int PHOTO_REQUEST_CUT = 2;//剪切结果结果
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

}
