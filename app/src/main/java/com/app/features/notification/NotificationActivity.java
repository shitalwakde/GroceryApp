package com.app.features.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.app.R;
import com.app.features.home.model.Category;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView rv_notification;
    List<Category> notifyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
        click();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init(){
        notifyList = new ArrayList<>();
        rv_notification =(RecyclerView)findViewById(R.id.rv_notification);
    }

    private void click(){
        Category cat = new Category();
        cat.setTv_title("Order Placed Successfully");
        cat.setTv_subTitle("Order will be delivered within 2-3 days");

        notifyList.add(cat);
        notifyList.add(cat);
        notifyList.add(cat);
        notifyList.add(cat);
        notifyList.add(cat);
        notifyList.add(cat);

        NotificationAdapter adapter = new NotificationAdapter(notifyList);
        rv_notification.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
