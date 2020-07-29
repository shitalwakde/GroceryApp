package com.app.callback;

import com.app.features.home.model.Product;

public interface ProductListener {

    void productClickLisener(Product product);
    void updateCartCount(String cartCount);
    void productImageClickLisener(Product product);
    void productVieMoreClickLisener(Product product, String type);
}
