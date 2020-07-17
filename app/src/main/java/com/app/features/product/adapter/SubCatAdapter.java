package com.app.features.product.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.features.home.model.Category;
import com.app.features.home.model.SubCategory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {
    private Category category;
    ArrayList<SubCategory> mdata;
    CategoryListener lisener;
    String subCategoryName="";

    public SubCatAdapter(CategoryListener lisener,Category category, ArrayList<SubCategory> mdata, String subCategoryName) {
        this.lisener = lisener;
        this.category = category;
        this.mdata = mdata;
        this.subCategoryName = subCategoryName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcat_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubCategory subCategory = mdata.get(position);
        holder.subCat.setText(subCategory.getName());
        if(subCategory.isHighlight()){
            subCategory.setHighlight(false);
            holder.subCat.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_highlight));
        }else{
            holder.subCat.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_border));
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView subCat;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subCat = (TextView)itemView.findViewById(R.id.tv_subCat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.subcategoryClickLisener(category,mdata.get(getAdapterPosition()));
                    mdata.get(getAdapterPosition()).setHighlight(true);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
