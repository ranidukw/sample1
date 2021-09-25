package com.example.dinetime.Models;

public class Checkout {

    private String orderid;
    private String date;
    private String totalprice;
    private String username;
    private String mobile;
    private String address;
    private String paymentmethod;

    public Checkout() {
    }

    public Checkout(String orderid, String date, String totalprice, String username, String mobile, String address, String paymentmethod) {
        this.orderid = orderid;
        this.date = date;
        this.totalprice = totalprice;
        this.username = username;
        this.mobile = mobile;
        this.address = address;
        this.paymentmethod = paymentmethod;
    }


    public String getOrderid() {
        return orderid;
    }

    public String getDate() {
        return date;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }
}
