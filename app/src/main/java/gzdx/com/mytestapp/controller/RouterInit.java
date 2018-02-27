package gzdx.com.mytestapp.controller;

import com.example.common.controller.LoginInterceptorMap;
import com.example.library1.TestLibraryActivity;
import com.example.router.Router;

/**
 * Created by liub on 2018/1/17.
 */

public class RouterInit {

    static {
        LoginInterceptorMap.put("/library1/test1");
    }

    public static void init(){
        Router.addActivity("/library1/test1", TestLibraryActivity.class);
    }
}
