package com.app.features.home.model;

import com.app.features.home.adapter.Banner;

import java.util.ArrayList;

public class HomeModel {
    private String success;
    private String message;
    private String whatsapp_mobile;
    private String offer_image;
    private String discount_image;
    private String count_cart;

    ArrayList<Banner> banner;
    ArrayList<Category> category;
    ArrayList<Brand> brand;
    ArrayList<Product> product;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWhatsapp_mobile() {
        return whatsapp_mobile;
    }

    public void setWhatsapp_mobile(String whatsapp_mobile) {
        this.whatsapp_mobile = whatsapp_mobile;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public void setOffer_image(String offer_image) {
        this.offer_image = offer_image;
    }

    public String getDiscount_image() {
        return discount_image;
    }

    public void setDiscount_image(String discount_image) {
        this.discount_image = discount_image;
    }

    public String getCount_cart() {
        return count_cart;
    }

    public void setCount_cart(String count_cart) {
        this.count_cart = count_cart;
    }

    public ArrayList<Banner> getBanner() {
        return banner;
    }

    public void setBanner(ArrayList<Banner> banner) {
        this.banner = banner;
    }

    public ArrayList<Category> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    public ArrayList<Brand> getBrand() {
        return brand;
    }

    public void setBrand(ArrayList<Brand> brand) {
        this.brand = brand;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }
}
