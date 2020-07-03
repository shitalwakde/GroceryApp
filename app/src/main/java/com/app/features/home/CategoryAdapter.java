package com.app.features.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.callback.HomeClickLisener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    List<Category> mdata;
    private final CategoryListener lisener;

    public CategoryAdapter(CategoryListener lisener, List<Category> mdata) {
        this.lisener = lisener;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = mdata.get(position);
        holder.iv_category.setImageResource(category.getIv_category());
        holder.tv_category_name.setText(category.getTv_category_name());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_category;
        TextView tv_category_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_category = (ImageView)itemView.findViewById(R.id.iv_category);
            tv_category_name = (TextView)itemView.findViewById(R.id.tv_category_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.categoryClickLisener(mdata.get(getAdapterPosition()));
                }
            });
        }
    }
}
