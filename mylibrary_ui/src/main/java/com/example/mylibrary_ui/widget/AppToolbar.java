package com.example.mylibrary_ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mylibrary_ui.R;
import com.example.mylibrary_ui.base.NoDoubleClickListener;

/**
 * Created by liub on 2017/12/27.
 */

public class AppToolbar extends LinearLayout {

    private Context context;
    private ImageButton mBackButton, mMenuLeftButton, mMenuRightButton;
    private TextView mBackContent, mTitleText, mMenuRightText, mMenuLeftText;
    private LinearLayout mMenuTextLin, mMenuImgLin;

    private String mTitleStr;
    private CharSequence mMenuStr;

    private boolean isBackEnable;

    private View.OnClickListener onClickListener;
    private OnTextMenuClickListener onTextMenuClickListener;

    public AppToolbar(Context context) {
        super(context);
        init(context, null);
    }

    public AppToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AppToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LinearLayout.inflate(context, R.layout.layout_ui_actionbar, this);
        initView(this);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppToolbar, 0, 0);
        CharSequence sequence = typedArray.getText(R.styleable.AppToolbar_tb_title);
        isBackEnable = typedArray.getBoolean(R.styleable.AppToolbar_tb_enable_back, true);
        CharSequence menuChar = typedArray.getText(R.styleable.AppToolbar_tb_menue_title);
        if (!TextUtils.isEmpty(sequence)) {
            mTitleStr = sequence.toString();
        }
        if (!TextUtils.isEmpty(menuChar)) {
            mMenuStr = menuChar.toString();
        }
        typedArray.recycle();
        setTitle(mTitleStr);
        setBackButtonEnable(isBackEnable);
        setMenuTitle(mMenuStr);
    }

    private void initView(View appToolbar) {
        mBackButton = (ImageButton) appToolbar.findViewById(R.id.ui_toolbar_back);
        mBackContent = (TextView) appToolbar.findViewById(R.id.ui_toolbar_back_content);
        mTitleText = (TextView) appToolbar.findViewById(R.id.ui_toolbar_title);
        mMenuImgLin = (LinearLayout) appToolbar.findViewById(R.id.ui_toolbar_img_lin);
        mMenuLeftButton = (ImageButton) appToolbar.findViewById(R.id.ui_toolbar_img_left);
        mMenuRightButton = (ImageButton) appToolbar.findViewById(R.id.ui_toolbar_img_right);
        mMenuTextLin = (LinearLayout) appToolbar.findViewById(R.id.ui_toolbar_text_lin);
        mMenuLeftText = (TextView) appToolbar.findViewById(R.id.ui_toolbar_text_left);
        mMenuRightText = (TextView) appToolbar.findViewById(R.id.ui_toolbar_text_right);
    }

    private NoDoubleClickListener onclickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View view) {
            int i = view.getId();
            if (i == R.id.ui_toolbar_back) {
                onClickListener.onClick(view);
            } else if (i == R.id.ui_toolbar_text_left) {
                if (onTextMenuClickListener != null) {
                    onTextMenuClickListener.onClick(mMenuLeftText.getText().toString());
                }
            }
        }
    };

    private void setTitle(String mTitleStr) {
        if (mTitleStr == null) {
            mTitleStr = "";
        }
        mTitleText.setText(mTitleStr);

    }

    private void setBackButtonEnable(boolean enable) {
        if (enable) {
            isBackEnable = true;
            mBackButton.setVisibility(VISIBLE);
            mBackButton.setOnClickListener(onclickListener);
        } else {
            isBackEnable = false;
            mBackButton.setVisibility(GONE);
            mBackButton.setOnClickListener(onclickListener);
        }
    }

    private void setMenuTitle(CharSequence mMenuString) {
        if (!TextUtils.isEmpty(mMenuString)) {
            mMenuStr = mMenuString;
            mMenuLeftText.setVisibility(VISIBLE);
            mMenuTextLin.setVisibility(VISIBLE);
            mMenuLeftText.setText(mMenuStr);
            mMenuLeftText.setOnClickListener(onclickListener);
        } else {
            mMenuTextLin.setVisibility(GONE);
            mMenuLeftText.setVisibility(GONE);
        }
    }


    public void setTextMenu(CharSequence text, OnTextMenuClickListener listener) {
        setMenuTitle(text);
        this.setTextMenuAction(listener);
    }


    public void setTextMenuAction(OnTextMenuClickListener listener) {
        this.onTextMenuClickListener = listener;
    }


    public interface OnTextMenuClickListener {
        void onClick(String var1);
    }
}
