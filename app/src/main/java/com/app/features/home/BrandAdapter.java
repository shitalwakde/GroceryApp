package com.app.features.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.callback.HomeClickLisener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {
    List<Category> mdata;
    HomeClickLisener lisener;

    public BrandAdapter(HomeClickLisener lisener, List<Category> mdata) {
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
        Category category = mdata.get(position);
        holder.iv_brand.setImageResource(category.getIv_brand());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_brand;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_brand = (ImageView)itemView.findViewById(R.id.iv_brand);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lisener.categoryClickLisener(mdata.get(getAdapterPosition()));
                }
            });
        }
    }
}
