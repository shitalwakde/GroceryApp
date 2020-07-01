package com.app.constant;

import com.app.R;
import com.app.activities.NavMenu;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {

    public static final String PREF_USER_ID="userid";
    public static final String PREF_IS_LOGGED_IN="isLoggedIn";
    public static final String PREF_USER_NAME = "userfullname";
    public static final String PREF_USER_DATA = "user_detail";

    public static ArrayList<NavMenu> getNavMenuItems(){
        ArrayList<NavMenu> navMenus= new ArrayList<>();
        navMenus.add(new NavMenu(MenuType.Home, "Home", R.drawable.ic_home));
        navMenus.add(new NavMenu(MenuType.ShopByCategory,"Shop by Category",  R.drawable.ic_format));
        navMenus.add(new NavMenu(MenuType.Offers,"Offers",  R.drawable.ic_user));
        navMenus.add(new NavMenu(MenuType.MyOrders,"My Orders",  R.drawable.ic_format));
        navMenus.add(new NavMenu(MenuType.MyWishlist,"My Wishlist",  R.drawable.ic_heart));
        navMenus.add(new NavMenu(MenuType.HelpCenter,"Wallet",  R.drawable.ic_wallet));
        navMenus.add(new NavMenu(MenuType.MyNotifications,"My Notifications",  R.drawable.ic_bell));
        navMenus.add(new NavMenu(MenuType.FAQs,"FAQs",  R.drawable.ic_format));
        navMenus.add(new NavMenu(MenuType.Logout,"Logout",  R.drawable.ic_logout));
        return navMenus;
    }
}
