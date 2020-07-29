package com.app.features.home.model;

import com.app.features.home.adapter.Banner;

import java.util.ArrayList;

public class HomeModel {
    private String success;
    private String message;
    private String msg;
    private String whatsapp_mobile;
    private String offerImage;
    private String dicountImage;
    private String count_cart;

    ArrayList<Banner> banner;
    ArrayList<Banner> advertisement;
    ArrayList<Category> category;
    ArrayList<Brand> brand;
    ArrayList<Product> product;
    ArrayList<Product> recentViewProduct;
    ArrayList<Product> BestSellingProduct;
    ArrayList<Product> productsList;

    public ArrayList<Banner> getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(ArrayList<Banner> advertisement) {
        this.advertisement = advertisement;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public String getDicountImage() {
        return dicountImage;
    }

    public void setDicountImage(String dicountImage) {
        this.dicountImage = dicountImage;
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

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }

    public ArrayList<Product> getRecentViewProduct() {
        return recentViewProduct;
    }

    public void setRecentViewProduct(ArrayList<Product> recentViewProduct) {
        this.recentViewProduct = recentViewProduct;
    }

    public ArrayList<Product> getBestSellingProduct() {
        return BestSellingProduct;
    }

    public void setBestSellingProduct(ArrayList<Product> bestSellingProduct) {
        BestSellingProduct = bestSellingProduct;
    }
}
