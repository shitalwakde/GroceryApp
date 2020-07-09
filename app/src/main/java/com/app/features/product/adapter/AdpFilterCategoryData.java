package com.app.features.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.R;
import com.app.features.home.model.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdpFilterCategoryData extends RecyclerView.Adapter<AdpFilterCategoryData.Holder> {

    Context mcontext;
    List<Category> mdata;

    public AdpFilterCategoryData(Context mcontext, List<Category> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_data_item,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        /*Category current = mdata.get(position);
        if(current.isSelected()==true){
            holder.iv_checkbox.setImageResource(R.drawable.ic_correct_red);
            holder.tv_category_name.setTextColor(mcontext.getResources().getColor(R.color.submit));
            holder.tv_category_name.setBackgroundColor(mcontext.getResources().getColor(R.color.colorWhite));
        }else {
            holder.iv_checkbox.setImageResource(R.drawable.ic_square);
            holder.tv_category_name.setTextColor(mcontext.getResources().getColor(R.color.colorBlack));
            holder.tv_category_name.setBackgroundColor(mcontext.getResources().getColor(R.color.colorWhite));
        }*/
    }


    @Override
    public int getItemCount() {
        return 8;
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView iv_checkbox;
        TextView tv_category_name;

        public Holder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
