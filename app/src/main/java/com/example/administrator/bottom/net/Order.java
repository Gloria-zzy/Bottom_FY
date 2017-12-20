package com.example.administrator.bottom.net;

/**
 * Created by Administrator on 2017/11/26 0026.
 */

public class Order {
    private String orderNum;
    private String phone;
    private String point;
    private String location;
    private String note;
    private String status;
    private String date;
    private String takenum;
    private String taker;

    public Order(String orderNum, String phone, String point,String takenum, String location, String note, String status,String date,String taker) {
        this.orderNum = orderNum;
        this.phone = phone;
        this.point = point;
        this.takenum = takenum;
        this.location = location;
        this.note = note;
        this.status = status;
        this.date = date;
        this.taker = taker;
    }

    public String getLocation() {
        return location;
    }

    public String getNote() {
        return note;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getPoint() {
        return point;
    }

    public String getDate() {
        return date;
    }

    public String getTakenum() {
        return takenum;
    }

    public String getTaker() {
        return taker;
    }
}
