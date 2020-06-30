package com.app.features.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.home.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {
    List<Category> mdata;
    HomeClickLisener lisener;

    public SubCatAdapter(HomeClickLisener lisener, List<Category> mdata) {
        this.lisener = lisener;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcat_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = mdata.get(position);
        holder.subCat.setText(category.getSubCat());
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
                    lisener.categoryClickLisener(mdata.get(getAdapterPosition()));
                }
            });
        }
    }
}
