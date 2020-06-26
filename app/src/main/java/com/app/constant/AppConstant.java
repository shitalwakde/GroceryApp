package com.app.constant;

import com.app.R;
import com.app.activities.NavMenu;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {

    public static ArrayList<NavMenu> getNavMenuItems(){
        ArrayList<NavMenu> navMenus= new ArrayList<>();
        navMenus.add(new NavMenu(MenuType.Home, "Home", R.drawable.ic_home));
        navMenus.add(new NavMenu(MenuType.ShopByCategory,"Shop by Category",  R.drawable.ic_idea));
        navMenus.add(new NavMenu(MenuType.Offers,"Offers",  R.drawable.ic_user));
        navMenus.add(new NavMenu(MenuType.MyOrders,"My Orders",  R.drawable.ic_video_camera));
        navMenus.add(new NavMenu(MenuType.MyWishlist,"My Wishlist",  R.drawable.ic_heart));
        navMenus.add(new NavMenu(MenuType.MyNotifications,"My Notifications",  R.drawable.ic_user));
        navMenus.add(new NavMenu(MenuType.HelpCenter,"Help Center",  R.drawable.ic_home));
        navMenus.add(new NavMenu(MenuType.FAQs,"FAQs",  R.drawable.ic_idea));
        navMenus.add(new NavMenu(MenuType.Logout,"Logout",  R.drawable.ic_heart));
        return navMenus;
    }
}
