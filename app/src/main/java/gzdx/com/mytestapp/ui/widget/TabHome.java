package gzdx.com.mytestapp.ui.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import gzdx.com.mytestapp.R;

/**
 * Created by liub on 2017/11/29.
 * 底部组件tab
 */

public class TabHome extends LinearLayout {


    @BindView(R.id.rg_tab)
    RadioGroup mRgTabLayout;

    private onCheckChange listener;

    public void OnCheckedChangeListener(onCheckChange onCheckChange){
        this.listener=onCheckChange;
    }

    interface onCheckChange{
        void showFragmentId(int id);
    }

    public TabHome(Context context) {
        super(context);
        init(context, null);
    }

    public TabHome(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabHome(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View driver = new View(context);
        driver.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        driver.setBackgroundColor(0xffffffff);
        addView(driver);
        View rootview = LayoutInflater.from(context).inflate(R.layout.layout_tab, this, false);
        addView(rootview);
        ButterKnife.bind(this,rootview);

        mRgTabLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                mRgTabLayout.check(checkedId);
                checkChanged(checkedId);
            }
        });
    }
    private void checkChanged(int checkedId) {
        int currentId = 0;
        switch (checkedId) {
            case R.id.rb_tab_ui:
                currentId = 0;
                break;
            case R.id.rb_tab_frame:
                currentId = 1;
                break;
            case R.id.rb_tab_find:
                currentId = 2;
                break;
            case R.id.rb_tab_me:
                currentId = 3;
                break;
        }
        listener.showFragmentId(currentId);
    }
}
