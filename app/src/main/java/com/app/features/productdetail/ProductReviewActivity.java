package com.app.features.productdetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.app.R;
import com.app.features.home.model.Product;

import java.util.ArrayList;

public class ProductReviewActivity extends AppCompatActivity {

    View rootView;
    RecyclerView rv_review;
    ArrayList<Product> productReviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_review);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init(){
        rv_review = (RecyclerView) findViewById(R.id.rv_review);
        productReviewsList = new ArrayList<>();
        if (getIntent() != null) {
            productReviewsList = (ArrayList<Product>) getIntent().getSerializableExtra("List");

            ProductReviewAdapter adapter = new ProductReviewAdapter(productReviewsList);
            rv_review.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                //finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
