package com.app.features.address;

import java.util.ArrayList;

public class AddressModel {
    private String success;
    private String message;
    private String name;
    private String mobile;
    private String email;
    private String houseNo;
    private String area;
    private String state;
    private String city;
    private String landmark;
    private String pincode;
    private String isDefault="No";
    private String status;
    private String deliveryId;
    private String cardId;
    private String totalSum;
    private String deliveryCharges;
    private String gst;
    private String totalFinalAmount;
    private String cartCount;

    ArrayList<AddressModel> cartList;
    private String orderId;
    ArrayList<AddressModel> deliveryLocationList;
    private String userId;

    public ArrayList<AddressModel> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<AddressModel> cartList) {
        this.cartList = cartList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public ArrayList<AddressModel> getDeliveryLocationList() {
        return deliveryLocationList;
    }

    public void setDeliveryLocationList(ArrayList<AddressModel> deliveryLocationList) {
        this.deliveryLocationList = deliveryLocationList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getTotalFinalAmount() {
        return totalFinalAmount;
    }

    public void setTotalFinalAmount(String totalFinalAmount) {
        this.totalFinalAmount = totalFinalAmount;
    }

    public String getCartCount() {
        return cartCount;
    }

    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

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
}
