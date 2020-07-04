package com.app.features.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.features.home.model.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    List<Category>mdata;

    public NotificationAdapter(List<Category> mdata) {
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapter, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = mdata.get(position);
        holder.tv_title.setText(category.getTv_title());
        holder.tv_subTitle.setText(category.getTv_subTitle());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title, tv_subTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_subTitle = (TextView)itemView.findViewById(R.id.tv_subTitle);
        }
    }
}
