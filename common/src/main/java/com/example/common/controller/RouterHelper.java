package com.example.common.controller;

import android.content.Context;

import com.example.router.Router;

/**
 * Created by liub on 2018/1/17.
 */

@SuppressWarnings("ALL")
public class RouterHelper {
    public static final String HM_HOST = "testApp://testApp.cn";

    public static void test(Context context,String msg) {
        Router.prepare(context, HM_HOST + "/library1/test1").putString("num1",msg).go();
    }
}
