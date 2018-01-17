package com.example.library1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class TestLibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library1_main);
        String msg = getIntent().getStringExtra("num1");
        TextView textView = (TextView) findViewById(R.id.tv_model1);
        textView.setText(msg);
        Toast.makeText(this, "library1:" + TestLibraryActivity.class.getName(), Toast.LENGTH_LONG).show();
    }
}
