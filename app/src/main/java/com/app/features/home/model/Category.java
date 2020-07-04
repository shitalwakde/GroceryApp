package com.app.features.home.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable
{
    private String categoryId;
    private String status;
    private String categoryName;
    private String image;

    ArrayList<SubCategory> subcategory;

    public int qty;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SubCategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<SubCategory> subcategory) {
        this.subcategory = subcategory;
    }

    //=========================================================================================================================
    private int iv_category, iv_best, iv_brand;
    private String product_type, tv_category_name, tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_orderId, tv_orderDate, tv_deliveryDate,
            tv_orderStatus,tv_payment_status, tv_orderAmount,subCat, tv_title, tv_subTitle;

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public int getIv_category() {
        return iv_category;
    }

    public void setIv_category(int iv_category) {
        this.iv_category = iv_category;
    }

    public String getTv_category_name() {
        return tv_category_name;
    }

    public void setTv_category_name(String tv_category_name) {
        this.tv_category_name = tv_category_name;
    }

    public int getIv_best() {
        return iv_best;
    }

    public void setIv_best(int iv_best) {
        this.iv_best = iv_best;
    }

    public String getTv_pr_name() {
        return tv_pr_name;
    }

    public void setTv_pr_name(String tv_pr_name) {
        this.tv_pr_name = tv_pr_name;
    }

    public String getTv_pr_sub_name() {
        return tv_pr_sub_name;
    }

    public void setTv_pr_sub_name(String tv_pr_sub_name) {
        this.tv_pr_sub_name = tv_pr_sub_name;
    }

    public String getTv_price() {
        return tv_price;
    }

    public void setTv_price(String tv_price) {
        this.tv_price = tv_price;
    }

    public String getTv_discount_price() {
        return tv_discount_price;
    }

    public void setTv_discount_price(String tv_discount_price) {
        this.tv_discount_price = tv_discount_price;
    }

    public int getIv_brand() {
        return iv_brand;
    }

    public void setIv_brand(int iv_brand) {
        this.iv_brand = iv_brand;
    }

    public String getTv_orderId() {
        return tv_orderId;
    }

    public void setTv_orderId(String tv_orderId) {
        this.tv_orderId = tv_orderId;
    }

    public String getTv_orderDate() {
        return tv_orderDate;
    }

    public void setTv_orderDate(String tv_orderDate) {
        this.tv_orderDate = tv_orderDate;
    }

    public String getTv_deliveryDate() {
        return tv_deliveryDate;
    }

    public void setTv_deliveryDate(String tv_deliveryDate) {
        this.tv_deliveryDate = tv_deliveryDate;
    }

    public String getTv_orderStatus() {
        return tv_orderStatus;
    }

    public void setTv_orderStatus(String tv_orderStatus) {
        this.tv_orderStatus = tv_orderStatus;
    }

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    public String getTv_payment_status() {
        return tv_payment_status;
    }

    public void setTv_payment_status(String tv_payment_status) {
        this.tv_payment_status = tv_payment_status;
    }

    public String getTv_orderAmount() {
        return tv_orderAmount;
    }

    public void setTv_orderAmount(String tv_orderAmount) {
        this.tv_orderAmount = tv_orderAmount;
    }

    public String getTv_title() {
        return tv_title;
    }

    public void setTv_title(String tv_title) {
        this.tv_title = tv_title;
    }

    public String getTv_subTitle() {
        return tv_subTitle;
    }

    public void setTv_subTitle(String tv_subTitle) {
        this.tv_subTitle = tv_subTitle;
    }
}
