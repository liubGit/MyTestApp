package com.example.library2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.common.controller.LoginInterceptorMap;
import com.example.common.controller.RouterHelper;
import com.example.router.Interceptor;
import com.example.router.Router;

public class TestLibrary2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library2_main);
        Button button = findViewById(com.example.library2.R.id.bt_library1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.prepare(getApplicationContext(),"testApp://testApp.cn/library1/test1").addInterceptor(new Interceptor() {
                        @Override
                        public boolean intercept(String var) {
                            if (LoginInterceptorMap.needLogin("library1/test")){
                                Toast.makeText(getApplicationContext(), "拦截了需要登陆！！！", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            return false;
                        }
                }).go();
            }
        });
        Toast.makeText(this, "library2:" + TestLibrary2Activity.class.getName(), Toast.LENGTH_SHORT).show();
    }
}
