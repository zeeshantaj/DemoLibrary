package com.example.dialog;
import android.util.Log;

import com.example.dialog.BuildConfig;

public class WebConst {

//        // Development
        public String BASE_URL = "https://jumbilin-bkd-development.azurewebsites.net/api/";
        public String MENU_BOARD_SOCKET = "https://jumbilinsocketdev-e8ce5f945195.herokuapp.com/";
        public  String registerMenu = "https://jumbilin-bkd-development.azurewebsites.net/api/v3/MenuBoard";
        public  String menuProducts = "https://jumbilin-bkd-development.azurewebsites.net/api/v2/MenuBoard?screenId=";
        public  String RegisterUrlNew = "https://jumbilin-bkd-development.azurewebsites.net/api/v4/local/MenuBoard";
        public static String internetStatus = "https://jumbilin-bkd-development.azurewebsites.net/api/DeviceManagement";
//


    //    // Production
//        public String BASE_URL = "http://jumbilinapis0343434343.azurewebsites.net/api/";
//        public String MENU_BOARD_SOCKET = "https://jumbilinsocket.herokuapp.com/";
//        public  String registerMenu = "http://jumbilinapis0343434343.azurewebsites.net/api/v3/MenuBoard";
//        public  String menuProducts = "http://jumbilinapis0343434343.azurewebsites.net/api/v2/MenuBoard?screenId=";
//        public  String RegisterUrlNew = "http://jumbilinapis0343434343.azurewebsites.net/api/v4/local/MenuBoard";
//        public static String internetStatus = "http://jumbilinapis0343434343.azurewebsites.net/api/DeviceManagement";
    public void checkForDevelopmentUrls() {
        // for reminder to change url from development to production

//        if (BuildConfig.CHECK_DEVELOPMENT_URLS){
//            Log.d("MyApp","development");
//            if (BASE_URL.contains("development") ||
//                    MENU_BOARD_SOCKET.contains("development") ||
//                    registerMenu.contains("development") ||
//                    menuProducts.contains("development") ||
//                    RegisterUrlNew.contains("development") ||
//                    internetStatus.contains("development")) {
//                throw new IllegalStateException("You're using development URLs in a production build!");
//            }
//        }else {
//            Log.d("MyApp","development");
//        }

    }
}
