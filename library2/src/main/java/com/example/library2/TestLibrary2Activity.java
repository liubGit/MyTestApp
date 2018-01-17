package com.example.library2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.common.controller.RouterHelper;

public class TestLibrary2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library2_main);
        Button button = findViewById(com.example.library2.R.id.bt_library1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterHelper.test(getApplicationContext(), TestLibrary2Activity.class.getName());
            }
        });
        Toast.makeText(this, "library2:" + TestLibrary2Activity.class.getName(), Toast.LENGTH_SHORT).show();
    }
}
