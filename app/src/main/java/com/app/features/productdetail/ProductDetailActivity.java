package com.app.features.productdetail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.R;
import com.app.activities.BaseActivity;
import com.app.activities.MainActivity;
import com.app.callback.HomeClickLisener;
import com.app.features.home.Category;
import com.app.features.home.HealthAdapter;
import com.app.features.product.ProductFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.activities.MainActivity.appBarContainer;

public class ProductDetailActivity extends BaseActivity implements HomeClickLisener {

    ImageView iv_product, iv_unwish;
    TextView tv_price, tv_rate_product, tv_add;
    RelativeLayout rl_like;
    LinearLayout ll_quantity;
    FragmentManager fragmentManager;
    public static TextView tv;
    RecyclerView rv_related_img, rv_related_img1;
    List<Category> bestSellingList;
    public static int productContainer;
    boolean flag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        click();

        productContainer = R.id.productContainer;
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        bestSellingList = new ArrayList<>();
        iv_product = (ImageView) findViewById(R.id.iv_product);
        iv_unwish = (ImageView) findViewById(R.id.iv_unwish);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_rate_product = (TextView) findViewById(R.id.tv_rate_product);
        tv_add = (TextView) findViewById(R.id.tv_add);
        rv_related_img = (RecyclerView)findViewById(R.id.rv_related_img);
        rv_related_img1 = (RecyclerView)findViewById(R.id.rv_related_img1);
        rl_like = (RelativeLayout)findViewById(R.id.rl_like);
        ll_quantity = (LinearLayout)findViewById(R.id.ll_quantity);
    }

    private void click() {

        tv_price.setPaintFlags(tv_price.getPaintFlags()
                | Paint.STRIKE_THRU_TEXT_FLAG);

        Category cate = new Category();
        cate.setIv_best(R.drawable.aata);
        cate.setTv_pr_name("Fortune");
        cate.setTv_pr_sub_name("Sunlife");

        Category cate1 = new Category();
        cate1.setIv_best(R.drawable.soup);
        cate1.setTv_pr_name("Soup");
        cate1.setTv_pr_sub_name("Manchow Veg");

        bestSellingList.add(cate);
        bestSellingList.add(cate1);
        bestSellingList.add(cate1);
        bestSellingList.add(cate);

        HealthAdapter adapter2 = new HealthAdapter(this, bestSellingList);
        rv_related_img.setAdapter(adapter2);
        rv_related_img1.setAdapter(adapter2);

        tv_rate_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        rl_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==false){
                    flag=true;
                    iv_unwish.setImageResource(R.drawable.ic_heart_red);
                }else{
                    flag=false;
                    iv_unwish.setImageResource(R.drawable.ic_heart);
                }
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_quantity.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
            }
        });
    }


    public void showRatingDialog(){
        final Dialog dialog1 = new Dialog(ProductDetailActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_rating);
        ImageView iv_cross = (ImageView)dialog1.findViewById(R.id.iv_cross);
        TextView tv_submit = (TextView)dialog1.findViewById(R.id.tv_submit);
        final RatingBar ratingBar = (RatingBar)dialog1.findViewById(R.id.ratingBar);
        final EditText et_review = (EditText)dialog1.findViewById(R.id.et_review);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                //saveRatings(String.valueOf(ratingBar.getRating()), et_review.getText().toString(), dialog1);
            }
        });

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void categoryClickLisener(Category category) {

    }

    @Override
    public void productClickLisener(Category category) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void orderClickLisener(Category category) {

    }
}
