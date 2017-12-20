package com.example.administrator.bottom.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.bottom.R;

/**
 * Created by Administrator on 2017\11\25 0025.
 */

public class OrderView extends RelativeLayout {

    private TextView order_intro;
    private TextView time;
    private TextView order_point;
    private TextView order_takenum;
    private TextView order_loc;
    private TextView order_note;
    private TextView order_status;
    private Button order_cancel;
    private Button order_contact;
    private Button order_change;
    private Button order_code;
    private Button discharge_order;
    private TextView order_num;
    private String num;
    private TextView tv_order_ordernum;
    private TextView tvshow_order_ordernum;
    private LinearLayout ll_order_picknum_status;
    private String selfphone;

    public OrderView(Context context){
        super(context);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.mod_record, this);

        // 获取控件
        order_intro = (TextView) findViewById(R.id.introduction);
        time = (TextView) findViewById(R.id.time);
        order_point = (TextView) findViewById(R.id.order_point);
        order_takenum = (TextView) findViewById(R.id.order_takenum);
        order_loc = (TextView) findViewById(R.id.order_loc);
        order_note = (TextView) findViewById(R.id.order_note);
        order_status = (TextView) findViewById(R.id.order_status);
        order_cancel = (Button) findViewById(R.id.order_cancel);
        order_change = (Button) findViewById(R.id.order_change);
        order_code = (Button) findViewById(R.id.order_code);
        discharge_order = (Button) findViewById(R.id.discharge_order);
        order_num = (TextView) findViewById(R.id.tvshow_order_ordernum);
        tv_order_ordernum =(TextView) findViewById(R.id.tv_order_ordernum);
        tvshow_order_ordernum=(TextView)findViewById(R.id.tvshow_order_ordernum);
        ll_order_picknum_status=(LinearLayout)findViewById(R.id.ll_order_picknum_status);
    }

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.mod_record, this);

        // 获取控件
        order_intro = (TextView) findViewById(R.id.introduction);
        time = (TextView) findViewById(R.id.time);
        order_point = (TextView) findViewById(R.id.order_point);
        order_takenum = (TextView) findViewById(R.id.tv_order_takenum);
        order_loc = (TextView) findViewById(R.id.order_loc);
        order_note = (TextView) findViewById(R.id.order_note);
        order_status = (TextView) findViewById(R.id.order_status);
        order_cancel = (Button) findViewById(R.id.order_cancel);
        order_change = (Button) findViewById(R.id.order_change);
        order_code = (Button) findViewById(R.id.order_code);
        discharge_order = (Button) findViewById(R.id.discharge_order);
        order_num = (TextView) findViewById(R.id.tvshow_order_ordernum);
        tv_order_ordernum =(TextView) findViewById(R.id.tv_order_ordernum);
        tvshow_order_ordernum=(TextView)findViewById(R.id.tvshow_order_ordernum);
        ll_order_picknum_status=(LinearLayout)findViewById(R.id.ll_order_picknum_status);
    }

    public Button getDischarge_order() {
        return discharge_order;
    }

    public void setDischarge_order(Button discharge_order) {
        this.discharge_order = discharge_order;
    }

    public String getSelfphone() {
        return selfphone;
    }

    public void setSelfphone(String selfphone) {
        this.selfphone = selfphone;
    }

    public TextView getTv_order_ordernum() {
        return tv_order_ordernum;
    }

    public void setTv_order_ordernum(TextView tv_order_ordernum) {
        this.tv_order_ordernum = tv_order_ordernum;
    }

    public TextView getOrder_takenum() {
        return order_takenum;
    }

    public TextView getTvshow_order_ordernum() {
        return tvshow_order_ordernum;
    }

    public void setTvshow_order_ordernum(TextView tvshow_order_ordernum) {
        this.tvshow_order_ordernum = tvshow_order_ordernum;
    }

    public LinearLayout getLl_order_picknum_status() {
        return ll_order_picknum_status;
    }

    public void setLl_order_picknum_status(LinearLayout ll_order_picknum_status) {
        this.ll_order_picknum_status = ll_order_picknum_status;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setOrder_intro(String intro) {
        this.order_intro.setText(intro);
    }

    public void setTime(String time) {
        this.time.setText(time);
    }

    public TextView getTime() {
        return time;
    }

    public void setOrder_point(String order_time) {
        this.order_point.setText(order_time);
    }

    public void setOrder_takenum(String order_takenum) {
        this.order_takenum.setText(order_takenum);
    }

    public void setOrder_loc(String order_loc) {
        this.order_loc.setText(order_loc);
    }

    public void setOrder_num(String order_num){
        this.order_num.setText(order_num);
    }

    public void setOrder_note(String order_note){
        this.order_note.setText(order_note);
    }

    public TextView getOrder_intro() {
        return order_intro;
    }

    public TextView getOrder_point() {
        return order_point;
    }

    public TextView getOrder_loc() {
        return order_loc;
    }

    public TextView getOrder_note() {
        return order_note;
    }

    public TextView getOrder_status() {
        return order_status;
    }

    public Button getOrder_cancel() {
        return order_cancel;
    }

    public Button getOrder_contact() {
        return order_contact;
    }

    public Button getOrder_change() {
        return order_change;
    }

    public Button getOrder_code() {
        return order_code;
    }

    public TextView getOrder_num() {
        return order_num;
    }

    public void setOrder_status(String order_status){
        this.order_status.setText(order_status);
    }

    public void setCancelButtonListener(OnClickListener listener) {
        order_cancel.setOnClickListener(listener);
    }

    public void setChangeButtonListener(OnClickListener listener){
        order_change.setOnClickListener(listener);
    }

    public void setGetCodeButtonListener(OnClickListener listener){
        order_code.setOnClickListener(listener);
    }
}
