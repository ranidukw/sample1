package com.example.dinetime.Models;

import java.io.Serializable;

public class Product implements Serializable {

    private String pname;
    private String pprice;
    private float price;
    private String imgeurl;
    private String category;

    public Product() {
    }

    public Product(String pname, String pprice, float price, String imgeurl, String category) {
        this.pname = pname;
        this.pprice = pprice;
        this.price = price;
        this.imgeurl = imgeurl;
        this.category = category;
    }

    public String getPname() {
        return pname;
    }

    public String getPprice() {
        return pprice;
    }

    public float getPrice() {
        return price;
    }

    public String getImgeurl() {
        return imgeurl;
    }

    public String getCategory() {
        return category;
    }
}
