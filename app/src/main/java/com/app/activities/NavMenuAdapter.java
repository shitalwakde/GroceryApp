package com.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.R;
import com.app.callback.DrawerItemClickLisener;
import com.app.features.navmenu.OfferActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.activities.MainActivity.drawerLayout;

public class NavMenuAdapter extends RecyclerView.Adapter<NavMenuAdapter.MyViewHolder> {
    Context mcontext;
    private final DrawerItemClickLisener lisener;
    ArrayList<NavMenu> navMenus;

    public NavMenuAdapter(Context mcontext, DrawerItemClickLisener lisener, ArrayList<NavMenu> navMenus) {
        this.mcontext = mcontext;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final NavMenu menu = navMenus.get(position);

        holder.nma_tv_menu.setText(menu.getTitle());
        holder.iv_menu.setImageResource(menu.getIcon());

        if(menu.isHighlight()){
            menu.setHighlight(false);
            holder.ll_name.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimaryDark));
            holder.nma_tv_menu.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWhite));
            holder.iv_menu.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWhite));
        }else{
            holder.ll_name.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.color.colorWhite));
        }

        if (menu.getTitle().equals("Shop by Category")) {
            holder.iv_forword_arrow.setVisibility(View.VISIBLE);
        }else if (menu.getTitle().equals("Offers")) {
            holder.iv_forword_arrow.setVisibility(View.VISIBLE);
        }else {
            holder.iv_forword_arrow.setVisibility(View.GONE);
        }

        holder.tvDiscountProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    holder.llBelowMenu.setVisibility(View.GONE);
                    holder.iv_down_arrow.setVisibility(View.GONE);
                    holder.iv_forword_arrow.setVisibility(View.VISIBLE);
                }
                Intent intent=new Intent(mcontext, OfferActivity.class);
                intent.putExtra("product","discount");
                mcontext.startActivity(intent);
            }
        });

        holder.tvspecialoffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    holder.llBelowMenu.setVisibility(View.GONE);
                    holder.iv_down_arrow.setVisibility(View.GONE);
                    holder.iv_forword_arrow.setVisibility(View.VISIBLE);
                }
                Intent intent=new Intent(mcontext,OfferActivity.class);
                intent.putExtra("product","offer");
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return navMenus.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_name;
        TextView nma_tv_menu, tvDiscountProduct, tvspecialoffers;
        ImageView iv_menu,iv_forword_arrow, iv_up_arrow, iv_down_arrow;
        View view;
        private LinearLayout llContent, llBelowMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_name = (LinearLayout) itemView.findViewById(R.id.nma_ll_content);
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
                    navMenus.get(getAdapterPosition()).setHighlight(true);
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
