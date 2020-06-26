package com.app.activities;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.R;
import com.app.callback.DrawerItemClickLisener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NavMenuAdapter extends RecyclerView.Adapter<NavMenuAdapter.MyViewHolder> {
    private final DrawerItemClickLisener lisener;
    ArrayList<NavMenu> navMenus;

    public NavMenuAdapter(DrawerItemClickLisener lisener, ArrayList<NavMenu> navMenus) {
        this.lisener = lisener;
        this.navMenus = navMenus;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_menu_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final NavMenu menu = navMenus.get(position);

        holder.nma_tv_menu.setText(menu.getTitle());
        holder.iv_menu.setImageResource(menu.getIcon());

        if (menu.getTitle().equals("Shop by Category")) {
            holder.iv_forword_arrow.setVisibility(View.VISIBLE);
        }else if (menu.getTitle().equals("Offers")) {
            holder.iv_forword_arrow.setVisibility(View.VISIBLE);
        }else {
            holder.iv_forword_arrow.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return navMenus.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nma_tv_menu, tvDiscountProduct, tvspecialoffers;
        ImageView iv_menu,iv_forword_arrow, iv_up_arrow, iv_down_arrow;
        View view;
        private LinearLayout llContent, llBelowMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nma_tv_menu = (TextView) itemView.findViewById(R.id.nma_tv_menu);
            tvDiscountProduct = (TextView) itemView.findViewById(R.id.tvDiscountProduct);
            tvspecialoffers = (TextView) itemView.findViewById(R.id.tvspecialoffers);
            iv_menu = (ImageView) itemView.findViewById(R.id.iv_menu);
            iv_forword_arrow = (ImageView) itemView.findViewById(R.id.iv_forword_arrow);
            iv_up_arrow = (ImageView) itemView.findViewById(R.id.iv_up_arrow);
            iv_down_arrow = (ImageView) itemView.findViewById(R.id.iv_down_arrow);
            view = (View) itemView.findViewById(R.id.view);
            llContent = itemView.findViewById(R.id.nma_ll_content);
            llBelowMenu = itemView.findViewById(R.id.llBelowMenu);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.drawerItemClickLisener(navMenus.get(getAdapterPosition()));

                    if(navMenus.get(getAdapterPosition()).getTitle().equals("Offers")){
                        if(llBelowMenu.getVisibility()==View.VISIBLE){
                            llBelowMenu.setVisibility(View.GONE);
                            iv_forword_arrow.setVisibility(View.GONE);
                            iv_down_arrow.setVisibility(View.GONE);
                            iv_up_arrow.setVisibility(View.VISIBLE);
                        }else{
                            llBelowMenu.setVisibility(View.VISIBLE);
                            iv_down_arrow.setVisibility(View.VISIBLE);
                            iv_forword_arrow.setVisibility(View.GONE);
                            iv_up_arrow.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }
}
