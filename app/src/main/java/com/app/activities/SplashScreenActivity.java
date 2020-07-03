package com.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.app.R;
import com.app.features.login.ModLogin;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        NavCategoryFragment.categories = new ArrayList<>();
        NavCategoryFragment.categorySubcat = new HashMap<>();

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    //todo change this tym
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();

        getCategorySubCategory();
    }

    private void getCategorySubCategory() {

        try {
            JsonObject jsonObject = new JsonObject();

            new RestClient().getApiService().getCategories(jsonObject, new Callback<ModCategory>() {
                @Override
                public void success(ModCategory loginModel, Response response) {
                    if (loginModel.getSuccess().equals("1")) {
                        if (loginModel.getCategories().size() > 0) {
                            NavCategoryFragment.categories=loginModel.getCategories();
                            for(int i=0;i< NavCategoryFragment.categories.size();i++){
                                NavCategoryFragment.categorySubcat.put( NavCategoryFragment.categories.get(i),  NavCategoryFragment.categories.get(i).getSubcategory());
                            }
                        }

                    } else {
                        Toast.makeText(SplashScreenActivity.this, loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(SplashScreenActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }
    }

}
