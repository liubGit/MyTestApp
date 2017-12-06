package gzdx.com.mytestapp.ui.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

    public static final int TYPE_TAB_HOME = 0;
    public static final int TYPE_TAB_MEOWCUN = 1;
    public static final int TYPE_TAB_DISCOVER = 2;
    public static final int TYPE_TAB_ME = 3;

    @BindView(R.id.rg_tab)
    RadioGroup mRgTabLayout;
    @BindView(R.id.rb_tab_ui)
    RadioButton mRbTabUi;
    @BindView(R.id.rb_tab_frame)
    RadioButton mRbTabFrame;
    @BindView(R.id.rb_tab_find)
    RadioButton mRbTabFind;
    @BindView(R.id.rb_tab_me)
    RadioButton mRbTabMe;

    private onCheckChange listener;
    private SparseArray<RadioButton> buttonSparseArray;

    public void setOnCheckedChangeListener(onCheckChange onCheckChange) {
        this.listener = onCheckChange;
    }

    public interface onCheckChange {
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
        ButterKnife.bind(this, rootview);

        buttonSparseArray = new SparseArray<>();
        buttonSparseArray.put(TYPE_TAB_HOME, mRbTabUi);
        buttonSparseArray.put(TYPE_TAB_MEOWCUN, mRbTabFrame);
        buttonSparseArray.put(TYPE_TAB_DISCOVER, mRbTabFind);
        buttonSparseArray.put(TYPE_TAB_ME, mRbTabMe);

        mRbTabUi.setTag(TYPE_TAB_HOME);
        mRbTabFrame.setTag(TYPE_TAB_MEOWCUN);
        mRbTabFind.setTag(TYPE_TAB_DISCOVER);
        mRbTabMe.setTag(TYPE_TAB_ME);

        mRgTabLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                if (radioButton != null) {
                    int tagType = Integer.parseInt(radioButton.getTag().toString());
                    onCheck(tagType);
                    listener.showFragmentId(tagType);
                }
            }
        });
    }

    public void onCheck(int checkId) {
        RadioButton radioButton = buttonSparseArray.get(checkId);
        radioButton.setChecked(true);
    }
}
