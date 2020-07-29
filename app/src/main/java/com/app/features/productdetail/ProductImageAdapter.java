package com.app.features.productdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.R;
import com.app.callback.ProductListener;
import com.app.features.home.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.MyViewHolder> {
    ArrayList<Product> mdata;
    ProductListener lisener;

    public ProductImageAdapter(ProductListener lisener, ArrayList<Product> mdata) {
        this.lisener = lisener;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product productImageList = mdata.get(position);
        Picasso.with(holder.itemView.getContext()).load(productImageList.getImage()).into(holder.iv_prd_img);
        if(productImageList.isHighlight()){
            productImageList.setHighlight(false);
            holder.iv_prd_img.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_highlight));
        }else{
            holder.iv_prd_img.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_border));
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_prd_img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_prd_img = (ImageView)itemView.findViewById(R.id.iv_prd_img);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.productImageClickLisener(mdata.get(getAdapterPosition()));
                    mdata.get(getAdapterPosition()).setHighlight(true);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

    }
}
