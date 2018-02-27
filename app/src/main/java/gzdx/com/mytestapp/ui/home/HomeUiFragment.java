package gzdx.com.mytestapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library2.TestLibrary2Activity;
import com.example.mylibrary_ui.widget.AppToolbar;

import gzdx.com.mytestapp.R;
import gzdx.com.mytestapp.ui.bluetooth.BluetoothActivity;
import gzdx.com.mytestapp.ui.widget.base.BaseFragement;

/**
 * Created by liub on 2017/11/29.
 */

public class HomeUiFragment extends BaseFragement {

    @Override
    public int setContextView() {
        return R.layout.layout_home_ui;
    }

    @Override
    protected void bindView(View view, Bundle savedInstanceState) {

        view.findViewById(R.id.btn_tablayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BluetoothActivity.class));
            }
        });

        view.findViewById(R.id.btn_window_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestLibrary2Activity.class));
            }
        });
        AppToolbar toolbar = (AppToolbar) view.findViewById(R.id.toolbar);

        toolbar.setTextMenu("点我", new AppToolbar.OnTextMenuClickListener() {
            @Override
            public void onClick(String var1) {
                Toast.makeText(getContext(), "点我了！！！", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
