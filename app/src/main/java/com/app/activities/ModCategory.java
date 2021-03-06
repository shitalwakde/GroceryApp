package com.app.activities;

import com.app.features.home.adapter.Banner;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Category;
import com.app.features.home.model.Product;

import java.util.ArrayList;

public class ModCategory {
    private String success;
    private String message;

    ArrayList<Category> categories;

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

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
