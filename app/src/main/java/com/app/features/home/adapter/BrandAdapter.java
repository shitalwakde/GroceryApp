package com.app.features.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.R;
import com.app.callback.BrandLisener;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {
    List<Brand> mdata;
    BrandLisener lisener;

    public BrandAdapter(BrandLisener lisener, List<Brand> mdata) {
        this.lisener = lisener;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Brand category = mdata.get(position);
        Picasso.with(holder.itemView.getContext()).load(category.getImage()).into(holder.iv_brand);
        holder.tv_brand_name.setText(category.getBrandName());
    }

    @Override
    public int getItemCount() {
        return mdata.size()>=4? 4:(mdata.size()/2)*2;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_brand;
        TextView tv_brand_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_brand = (ImageView)itemView.findViewById(R.id.iv_brand);
            tv_brand_name = (TextView) itemView.findViewById(R.id.tv_brand_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.brandLisener(mdata.get(getAdapterPosition()));
                }
            });
        }
    }
}
