package com.app.features.order;

import java.io.Serializable;

public class OrderDetailList implements Serializable {
    private String orderDetailId;
    private String userId;
    private String orderId;
    private String productId;
    private String quantity;
    private String productRate;
    private String productDiscount;
    private String productGrossAmount;
    private String productFinalAmount;
    private String amount;
    private String status;
    private String productName;
    private String productQuantity;
    private String productImage;

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductRate() {
        return productRate;
    }

    public void setProductRate(String productRate) {
        this.productRate = productRate;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductGrossAmount() {
        return productGrossAmount;
    }

    public void setProductGrossAmount(String productGrossAmount) {
        this.productGrossAmount = productGrossAmount;
    }

    public String getProductFinalAmount() {
        return productFinalAmount;
    }

    public void setProductFinalAmount(String productFinalAmount) {
        this.productFinalAmount = productFinalAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
