package com.app.callback;

import com.app.features.home.model.Category;
import com.app.features.home.model.SubCategory;

public interface CategoryListener extends HomeClickLisener{
    void categoryClickLisener(Category category);

    void subcategoryClickLisener(Category category, SubCategory subCategories);
}
