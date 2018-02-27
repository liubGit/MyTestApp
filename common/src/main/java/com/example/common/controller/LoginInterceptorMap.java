package com.example.common.controller;

import android.support.v4.util.ArrayMap;

/**
 * 路由需要登录的记录
 */

public class LoginInterceptorMap {
    private static ArrayMap<String, Boolean> interceptorMap = new ArrayMap<>();

    public static void put(String path) {
        interceptorMap.put(path, true);
    }

    public static boolean needLogin(String path) {
        return interceptorMap.get(path) != null && interceptorMap.get(path);
    }
}
