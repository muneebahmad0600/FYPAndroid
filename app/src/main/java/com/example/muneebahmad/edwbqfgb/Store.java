package com.example.muneebahmad.edwbqfgb;

/**
 * Created by Muneeb Ahmad on 3/24/2018.
 */

public class Store {

    private String store_name;
    private String address;
    private int rating;
    private String store_pic;
    private String delivery_start_time;
    private String delivery_end_time;
    private String delivery_fee;

    public String getStore_pic() {return store_pic;}

    public void setStore_pic(String store_pic) {this.store_pic = store_pic;}

    public String getDelivery_start_time() {return delivery_start_time;}

    public void setDelivery_start_time(String delivery_start_time) {this.delivery_start_time = delivery_start_time;}

    public String getDelivery_end_time() {return delivery_end_time;}

    public void setDelivery_end_time(String delivery_end_time) {this.delivery_end_time = delivery_end_time;}

    public String getDelivery_fee() {return delivery_fee;}

    public void setDelivery_fee(String delivery_fee) {this.delivery_fee = delivery_fee;}

    public Store() {}

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
