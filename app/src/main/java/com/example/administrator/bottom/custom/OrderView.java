package com.example.administrator.bottom.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.bottom.R;

//        订单号   order_number
//        下单时间 order_time
//        快递体积 size(L M S)
//        收货地点 arrive_address
//        收货时间 arrive_time
//        备注     note

public class OrderView extends RelativeLayout {

    private TextView tv_size;
    private TextView tv_orderTime;
    private TextView tv_arriveAddress;
    private TextView tv_arriveTime;
    private TextView tv_note;
    private TextView tv_order_number;
    private LinearLayout ll_modOrder_allAround;
    private String phone;

    public OrderView(Context context){
        super(context);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.mod_order, this);

        // 获取控件
        tv_size = (TextView) findViewById(R.id.tv_modOrder_size);
        tv_orderTime = (TextView) findViewById(R.id.tv_modOrder_orderTime);
        tv_note = (TextView) findViewById(R.id.tv_modOrder_note);
        tv_arriveAddress = (TextView) findViewById(R.id.tv_modOrder_arriveAddress);
        tv_arriveTime = (TextView) findViewById(R.id.tv_modOrder_arriveTime);
        tv_order_number = (TextView) findViewById(R.id.tv_modOrder_orderNumber);
        ll_modOrder_allAround = (LinearLayout) findViewById(R.id.ll_modOrder_allAround);
    }

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.mod_record, this);

        // 获取控件
        tv_size = (TextView) findViewById(R.id.tv_modOrder_size);
        tv_orderTime = (TextView) findViewById(R.id.tv_modOrder_orderTime);
        tv_note = (TextView) findViewById(R.id.tv_modOrder_note);
        tv_arriveAddress = (TextView) findViewById(R.id.tv_modOrder_arriveAddress);
        tv_arriveTime = (TextView) findViewById(R.id.tv_modOrder_arriveTime);
        tv_order_number = (TextView) findViewById(R.id.tv_modOrder_orderNumber);
        ll_modOrder_allAround = (LinearLayout) findViewById(R.id.ll_modOrder_allAround);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTv_size(String size) {
        switch (size){
            case "S" :
                this.tv_size.setText("小件快递");
                break;
            case "M" :
                this.tv_size.setText("中件快递");
                break;
            case "L" :
                this.tv_size.setText("大件快递");
                break;
        }
    }

    public void setTv_orderTime(String orderTime) {
        this.tv_orderTime.setText(orderTime);
    }

    public void setTv_arriveAddress(String arriveAddress) {
        this.tv_arriveAddress.setText(arriveAddress);
    }

    public void setTv_arriveTime(String arriveTime) {
        this.tv_arriveTime.setText(arriveTime);
    }

    public void setTv_note(String note) {
        this.tv_note.setText(note);
    }

    public void setTv_order_number(String order_number) {
        this.tv_order_number.setText(order_number);
    }

    public LinearLayout getLl_modOrder_allAround() {
        return ll_modOrder_allAround;
    }
}
