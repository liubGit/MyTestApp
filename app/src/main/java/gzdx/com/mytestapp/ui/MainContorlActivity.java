package gzdx.com.mytestapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gzdx.com.mytestapp.R;
import gzdx.com.mytestapp.ui.home.HomeUiFragment;
import gzdx.com.mytestapp.ui.me.MeFragment;
import gzdx.com.mytestapp.ui.widget.TabHome;

public class MainContorlActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.tabhome)
    TabHome mTabHome;

    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contorl);
        ButterKnife.bind(this);
        initView();

        //初始界面
        checkButton(TabHome.TYPE_TAB_ME);
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
                checkButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabHome.setOnCheckedChangeListener(new TabHome.onCheckChange() {
            @Override
            public void showFragmentId(int id) {
                mViewpager.setCurrentItem(id);
            }
        });
    }

    public void checkButton(int checkId) {
        mTabHome.onCheck(checkId);
        mViewpager.setCurrentItem(checkId);
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

