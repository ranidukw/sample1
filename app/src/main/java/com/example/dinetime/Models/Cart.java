package com.example.dinetime.Models;

public class Cart {

    private String productname;
    private String productDisprice;
    private float price;
    private String imageurl;
    private float totalPrice;
    private String dispalyTotal;

    public Cart() {
    }

    public Cart(String productname, String productDisprice, float price, String imageurl, float totalPrice, String dispalyTotal) {
        this.productname = productname;
        this.productDisprice = productDisprice;
        this.price = price;
        this.imageurl = imageurl;
        this.totalPrice = totalPrice;
        this.dispalyTotal = dispalyTotal;

    }

    public String getProductname() {
        return productname;
    }

    public String getProductDisprice() {
        return productDisprice;
    }

    public float getPrice() {
        return price;
    }

    public String getImageurl() {
        return imageurl;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public String getDispalyTotal() {
        return dispalyTotal;
    }
}
