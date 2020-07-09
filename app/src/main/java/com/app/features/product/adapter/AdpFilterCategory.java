package com.app.features.product.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.app.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdpFilterCategory extends RecyclerView.Adapter<AdpFilterCategory.Holder> {

    public AdpFilterCategory() {

    }

    @NonNull
    @Override
    public  Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
