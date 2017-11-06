package gzdx.com.mytestapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xrefresh_library.widget.OnXRefreshViewListener;
import com.xrefresh_library.widget.XRefreshListView;

public class MainActivity extends AppCompatActivity {

    private XRefreshListView xRefreshListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xRefreshListView.setOnXRefreshViewListener(new OnXRefreshViewListener(){
            @Override
            public void onRefresh() {
                super.onRefresh();
            }
        });

    }
}
