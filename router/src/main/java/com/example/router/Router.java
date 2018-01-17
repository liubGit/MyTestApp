package com.example.router;

import android.content.Context;
import android.net.Uri;

/**
 * Created by liub on 2018/1/16.
 */

public class Router {
    public static Class defaultClassUrl;
    public static boolean DEBUG=false;
    private static String host="testApp://testApp:cn";

    public Router() {
    }
    public static Packet prepare(Context context,String url){
        return new Packet(context,url);
    }

    public static void addActivity(String path, Class activityClass) {
        UrlConfigMap.put(path, activityClass);
    }

    public static void setDefaultWebPage(Class webPageClazz) {
        defaultClassUrl = webPageClazz;
    }

//    public static EventBus eventBus() {
//        return EventBus.getDefault();
//    }

    public static void setHost(String host) {
        Router.host = host;
        if(host != null) {
            Packet.SCHEME = Uri.parse(host).getScheme();
        }

    }

    public static String getHost() {
        return host;
    }
}
