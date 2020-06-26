package com.app.activities;

import com.app.constant.MenuType;

public class NavMenu {
    private MenuType menu;
    private String title;
    private int icon;

    public NavMenu() {
    }

    public NavMenu(MenuType menu, String title, int icon) {
        this.menu = menu;
        this.title = title;
        this.icon = icon;
    }

    public MenuType getMenu() {
        return menu;
    }

    public void setMenu(MenuType menu) {
        this.menu = menu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
