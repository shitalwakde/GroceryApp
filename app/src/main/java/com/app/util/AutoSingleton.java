package com.app.util;

public class AutoSingleton {

    private static final AutoSingleton ourInstance = new AutoSingleton();
    public int previousTapVisible=0;

    public static AutoSingleton getInstance(){
        return ourInstance;
    }

    private AutoSingleton() {

    }

        public final String TEST_URL="http://ourwaysolutions.com/demo/grocery/index.php";
}
