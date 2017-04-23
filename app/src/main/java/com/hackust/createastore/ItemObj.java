package com.hackust.createastore;

/**
 * Created by Ishaan Batra on 4/23/2017.
 */

public class ItemObj {
    private String name;
    private int quantity;
    private double price;
    private String imageUrl;

    public ItemObj(String name,int quantity, double price)
    {
        this.name=name;
        this.quantity=quantity;
        this.price=price;
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
