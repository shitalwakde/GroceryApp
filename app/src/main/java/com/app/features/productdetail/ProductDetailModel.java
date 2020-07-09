package com.app.features.productdetail;

import com.app.features.home.model.Product;

import java.util.ArrayList;

public class ProductDetailModel {
    private String success;
    private String msg;
    private String productId;
    private String categoryId;
    private String subCatrgoryId;
    private String brandId;
    private String name;
    private String description;
    private String productType;
    private String rate;
    private String discount;
    private String stockAvailable;
    private String quantity;
    private String grossAmount;
    private String finalAmount;
    private String deliveryCharges;
    private String offerName;
    private String status;
    private String image;
    private String brandName;
    private String isInCard;
    private String cartQuantity;
    private String isInWishList;
    private String cartCount;

    ArrayList<Product> recentProduct;
    ArrayList<Product> similarProduct;

    public String getCartCount() {
        return cartCount;
    }

    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }

    public String getIsInCard() {
        if(isInCard==null)
            isInCard="";
        return isInCard;
    }

    public void setIsInCard(String isInCard) {
        this.isInCard = isInCard;
    }

    public String getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public int getCartQuantityInteger(){
        if(cartQuantity!=null)
            return Integer.parseInt(cartQuantity);
        return 0;
    }

    public void setCartQuantity(int cardQuantity) {
        this.cartQuantity = String.valueOf(cardQuantity);
    }

    public String getIsInWishList() {
        if(isInWishList==null)
            isInWishList="";
        return isInWishList;
    }

    public void setIsInWishList(String isInWishList) {
        this.isInWishList = isInWishList;
    }

    public ArrayList<Product> getRecentProduct() {
        return recentProduct;
    }

    public void setRecentProduct(ArrayList<Product> recentProduct) {
        this.recentProduct = recentProduct;
    }

    public ArrayList<Product> getSimilarProduct() {
        return similarProduct;
    }

    public void setSimilarProduct(ArrayList<Product> similarProduct) {
        this.similarProduct = similarProduct;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCatrgoryId() {
        return subCatrgoryId;
    }

    public void setSubCatrgoryId(String subCatrgoryId) {
        this.subCatrgoryId = subCatrgoryId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(String stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
