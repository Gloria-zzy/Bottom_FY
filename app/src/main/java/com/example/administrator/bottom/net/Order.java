package com.example.administrator.bottom.net;

//        手机号   phone
//        订单号   order_number
//        下单时间 order_time
//        信任好友 trust_friend
//        快递体积 size(L M S)
//        快递数量 amount
//        收货地点 arrive_address
//        收货时间 arrive_time
//        快递点   pick_point
//        取货号   pick_number
//        派送员   taker
//        备注     note
//        状态     order_status(int)

public class Order {
    private String orderNumber;
    private String phone;
    private String pickPoint;
    private String arriveAddress;
    private String note;
    private String orderStatus;
    private String orderTime;
    private String pickNumber;
    private String taker;
    private String trust_friend;
    private String size;
    private String amount;
    private String arriveTime;

    public Order(String phone,
                 String orderNum,
                 String orderTime,
                 String trustFriend,
                 String size,
                 String amount,
                 String arriveAddress,
                 String arriveTime,
                 String pickPoint,
                 String pickNumber,
                 String taker,
                 String note,
                 String orderStatus) {
        this.orderNumber = orderNum;
        this.phone = phone;
        this.pickPoint = pickPoint;
        this.pickNumber = pickNumber;
        this.arriveAddress = arriveAddress;
        this.note = note;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.taker = taker;
        this.trust_friend = trustFriend;
        this.size = size;
        this.amount = amount;
        this.arriveTime = arriveTime;
    }

    public String getArriveAddress() {
        return arriveAddress;
    }

    public String getNote() {
        return note;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPickPoint() {
        return pickPoint;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getPickNumber() {
        return pickNumber;
    }

    public String getTaker() {
        return taker;
    }

    public String getTrust_friend() {
        return trust_friend;
    }

    public String getSize() {
        return size;
    }

    public String getAmount() {
        return amount;
    }

    public String getArriveTime() {
        return arriveTime;
    }
}
