package gzdx.com.mytestapp.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gzdx.com.mytestapp.R;
import gzdx.com.mytestapp.ui.home.HomeUiFragment;
import gzdx.com.mytestapp.ui.me.MeFragment;

public class MainContorlActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.rg_tab)
    RadioGroup mRgTab;

    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contorl);
        ButterKnife.bind(this);
        initView();

        checkChanged(R.id.rb_tab_ui);
    }

    private void initView() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeUiFragment());
        fragmentList.add(new MeFragment());
        fragmentList.add(new MeFragment());
        fragmentList.add(new MeFragment());

        mViewpager.setAdapter(fragmentPageAdapter);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageChecked(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
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
        mRgTab.check(checkedId);
        mViewpager.setCurrentItem(currentId);
    }

    private void pageChecked(int position) {
        switch (position) {
            case 0:
                mRgTab.check(R.id.rb_tab_ui);
                break;
            case 1:
                mRgTab.check(R.id.rb_tab_frame);
                break;
            case 2:
                mRgTab.check(R.id.rb_tab_find);
                break;
            case 3:
                mRgTab.check(R.id.rb_tab_me);
                break;
            default:
                mRgTab.check(R.id.rb_tab_ui);
                break;
        }
    }

    public FragmentPagerAdapter fragmentPageAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    };
}

