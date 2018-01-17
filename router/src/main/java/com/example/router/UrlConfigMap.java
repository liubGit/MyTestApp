package com.example.router;

import android.support.v4.util.ArrayMap;

import java.util.Map;

/**
 * Created by liub on 2018/1/17.
 */

class UrlConfigMap {
    private static Map<String, Class> classMap = new ArrayMap();

    UrlConfigMap() {
    }

    public static void put(String path, Class activityClass) {
        classMap.put(path, activityClass);
    }

    public static Class get(String path) {
        return (Class)classMap.get(path);
    }
}
