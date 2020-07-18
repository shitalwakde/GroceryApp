package com.app.features.home.model;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
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
    private float grossAmount;
    private float finalAmount;
    private String deliveryCharges;
    private String offerName;
    private String status;
    private String image;
    private String brandName;
    //private String isInCard;
    private String cartQuantity;
    private String isInWishList;
    private String isVarient;

    ArrayList<Product> recentProduct;
    ArrayList<Product> similarProduct;

    //====addToCart
    private String success;
    private String msg;
    private String Msg;
    private String message;
    private String tempUserId;
    private String cartCount;

    //====cartList
    private String cartId;
    private String cartDate;
    private String cartProduct;
    private String productImage;
    private String productFinalAmount;
    private String productCartAmount;

    //====addToWishList
    private String userId;

    //====wishList

    private String wishListId;
    private String wishListDate;
    private String productName;
    private String productQuantity;

    //======progress
    private boolean isLoading;

    //======varient
    ArrayList<Product> varientList;
    private String productVarientId;
    private int weightSelected;

    public int getWeightSelected() {
        return weightSelected;
    }

    public void setWeightSelected(int weightSelected) {
        this.weightSelected = weightSelected;
    }

    public String getIsVarient() {
        return isVarient;
    }

    public void setIsVarient(String isVarient) {
        this.isVarient = isVarient;
    }

    public ArrayList<Product> getVarientList() {
        return varientList;
    }

    public void setVarientList(ArrayList<Product> varientList) {
        this.varientList = varientList;
    }

    public String getProductVarientId() {
        return productVarientId;
    }

    public void setProductVarientId(String productVarientId) {
        this.productVarientId = productVarientId;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public int getCartQuantityInteger(){
        if(!TextUtils.isEmpty(cartQuantity))
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

    public String getWishListId() {
        return wishListId;
    }

    public void setWishListId(String wishListId) {
        this.wishListId = wishListId;
    }

    public String getWishListDate() {
        return wishListDate;
    }

    public void setWishListDate(String wishListDate) {
        this.wishListDate = wishListDate;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    /*public String getCartQuantity() {
        return cartQuantity;
    }

    public int getCartQuantityInteger(){
        if(cartQuantity!=null)
            return Integer.parseInt(cartQuantity);
        return 0;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }*/

    public String getCartDate() {
        return cartDate;
    }

    public void setCartDate(String cartDate) {
        this.cartDate = cartDate;
    }

    public String getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(String cartProduct) {
        this.cartProduct = cartProduct;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductFinalAmount() {
        return productFinalAmount;
    }

    public void setProductFinalAmount(String productFinalAmount) {
        this.productFinalAmount = productFinalAmount;
    }

    public String getProductCartAmount() {
        return productCartAmount;
    }

    public void setProductCartAmount(String productCartAmount) {
        this.productCartAmount = productCartAmount;
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



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public float getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(float grossAmount) {
        this.grossAmount = grossAmount;
    }

    public float getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(float finalAmount) {
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

    public String getTempUserId() {
        return tempUserId;
    }

    public void setTempUserId(String tempUserId) {
        this.tempUserId = tempUserId;
    }

    public String getCartCount() {
        return cartCount;
    }

    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }
}
