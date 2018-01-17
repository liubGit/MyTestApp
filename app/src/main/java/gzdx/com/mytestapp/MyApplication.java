package gzdx.com.mytestapp;

import android.app.Application;

import gzdx.com.mytestapp.controller.RouterInit;


/**
 * Created by liub on 2018/1/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RouterInit.init();
    }
}
