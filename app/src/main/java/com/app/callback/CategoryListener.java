package com.app.callback;

import com.app.features.home.Category;
import com.app.features.home.SubCategory;

public interface CategoryListener extends HomeClickLisener{
    void categoryClickLisener(Category category);

    void subcategoryClickLisener(Category category, SubCategory subCategories);
}
