package com.app.features.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.constant.AppConstant;
import com.app.features.home.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private static final String TAG = "CategoryAdapter";
    List<Category> mdata;
    private int source;
    private final CategoryListener lisener;

    public CategoryAdapter(CategoryListener lisener, List<Category> mdata,int source) {
        this.lisener = lisener;
        this.mdata = mdata;
        this.source = source;
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
        //Log.d(TAG, "onBindViewHolder: "+category.getImage());

        if(category!=null) {
            Picasso.with(holder.itemView.getContext()).load(category.getImage()).into(holder.iv_category);
            holder.tv_category_name.setText(category.getCategoryName());
        }
    }

    @Override
    public int getItemCount() {
        if(source== AppConstant.FROM_HOME_CATEGORY_PRODUCT)
                return  6;
        else
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
