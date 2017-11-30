package gzdx.com.mytestapp.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import gzdx.com.mytestapp.R;
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

    }
}
