package com.xrefresh_library.Refresh.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xrefresh_library.R;
import com.xrefresh_library.Refresh.base.BaseRecViewHolder;
import com.xrefresh_library.Refresh.base.BaseRecyclerAdapter;
import com.xrefresh_library.base.NoDoubleClickListener;
import com.xrefresh_library.utils.RecyclerViewInitHelper;

import java.util.Arrays;
import java.util.List;


/**
 * Created by liub on 2017/11/6 .
 */

public class AppToolbar extends LinearLayout {
    public static final int NEW_STANDARD_BACKGROUND_COLOR = -1;
    public static int NEW_STANDARD_TEXT_COLOR;
    private String mTitleString = null;
    private String mSubTitleString = null;
    private String mLocationString = null;
    private CharSequence mTextMenuString = null;
    private Drawable mMenuDrawableFir;
    private Drawable mMenuDrawableSec;
    private int[] mMultiMenuResource;
    private boolean isTitleShow = true;
    private boolean isCustomTitleViewShow = false;
    private boolean isLocationShow = false;
    private boolean isAllowBack = true;
    private boolean isTextMenuShow = false;
    private boolean isDrawableMenuShow = false;
    private AppToolbar.OnMenuItemClickListener listener1;
    private AppToolbar.OnMenuItemClickListener listener2;
    private View.OnClickListener mLocationClickListener;
    private View.OnClickListener mBackClickListener;
    private AppToolbar.OnTextMenuClickListener mTextMenuClickListener;
    private AdapterView.OnItemClickListener mDropdownItemListener;
    private PopupWindow mDropDownMenu;
    private PopupWindow mNavigationPop;
    private Context context;
    private String[] mDropdownItems;
    private View mSearchBarLayout;
    private EditText mSearchBar;
    private boolean isLastTitleStyleCustom = false;
    private View mLastCustomTitleView;
    private TextView mSearchPopEdit;
    private RecyclerView mTabRecycler;
    private AppToolbar.TitleTabAdapter titleTabAdapter;
    private int backgroundColor;
    private boolean isBackGroundTransparent = false;
    private int imgMenuPadding;
    private int imgMenuLayoutPadding;
    ImageButton mBackButton;
    TextView mSubtitleView;
    RelativeLayout mBackLayout;
    TextView mLocationText;
    ImageView mLocationIcon;
    RelativeLayout mLocationLayout;
    RelativeLayout mToolbarLayout;
    FrameLayout mNavigationLayout;
    TextView mTitleView;
    LinearLayout mCustomViewLayout;
    LinearLayout mTextMenuLayout;
    TextView mTextMenuView;
    TextView mTextMenuViewRight;
    ImageButton mMenuOne;
    ImageButton mMenuMore;
    LinearLayout mImgMenusLayout;
    FrameLayout mMenusLayout;
    View mBottomStroke;
    View mFakeStatusBar;
    private int tintColor = 2147483647;
    private AppToolbar.OnSearchListener onSearchListener;
    private TextWatcher onTextChangedListener;
    private String mSearchHint;

    public AppToolbar(Context context) {
        super(context);
        this.init(context, (AttributeSet) null);
    }

    public AppToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public AppToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.lib_ui_actionbar, this);
        this.initView(this);
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.AppToolbar, 0, 0);
        CharSequence mTitleSequence = a.getText(R.styleable.AppToolbar_tb_title);
        if (!TextUtils.isEmpty(mTitleSequence)) {
            this.mTitleString = mTitleSequence.toString();
        }

        CharSequence mTextMenuSequence = a.getText(R.styleable.AppToolbar_tb_text_menu_string);
        if (!TextUtils.isEmpty(mTextMenuSequence)) {
            this.mTextMenuString = mTextMenuSequence.toString();
        }

        this.mMenuDrawableFir = a.getDrawable(R.styleable.AppToolbar_tb_src_menu_fir);
        this.mMenuDrawableSec = a.getDrawable(R.styleable.AppToolbar_tb_src_menu_sec);
        this.isAllowBack = a.getBoolean(R.styleable.AppToolbar_tb_enable_back, true);
        this.imgMenuPadding = a.getDimensionPixelSize(R.styleable.AppToolbar_tb_src_menu_padding, this.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin));
        this.imgMenuLayoutPadding = a.getDimensionPixelSize(R.styleable.AppToolbar_tb_src_menu_layout_padding, 0);
        boolean transparent = a.getBoolean(R.styleable.AppToolbar_tb_transparent, false);
        a.recycle();
        this.setTitle(this.mTitleString);
        this.setTextMenu(this.mTextMenuString);
        this.setupMenuItems(this.mMenuDrawableFir, (AppToolbar.OnMenuItemClickListener) null, this.mMenuDrawableSec, (AppToolbar.OnMenuItemClickListener) null, this.imgMenuPadding, this.imgMenuLayoutPadding);
        this.setBackButtonEnable(this.isAllowBack);
        this.backgroundColor = ContextCompat.getColor(this.getContext(), R.color.toolbar_color);
        this.setNewStandard();
        NEW_STANDARD_TEXT_COLOR = ContextCompat.getColor(this.getContext(), R.color.text_content_dark);
        if (transparent) {
            this.setBackGroundTransparent();
        }

    }

    private void initView(View rootView) {
        this.mBackButton = (ImageButton) rootView.findViewById(R.id.btn_toolbar_back);
        this.mSubtitleView = (TextView) rootView.findViewById(R.id.tv_toolbar_subtitle);
        this.mBackLayout = (RelativeLayout) rootView.findViewById(R.id.rl_back_layout);
        this.mLocationText = (TextView) rootView.findViewById(R.id.tv_toolbar_location);
        this.mLocationLayout = (RelativeLayout) rootView.findViewById(R.id.rl_toolbar_location);
        this.mToolbarLayout = (RelativeLayout) rootView.findViewById(R.id.toolbar_layout);
        this.mNavigationLayout = (FrameLayout) rootView.findViewById(R.id.fl_toolbar_navigation);
        this.mTitleView = (TextView) rootView.findViewById(R.id.tv_toolbar_title);
        this.mCustomViewLayout = (LinearLayout) rootView.findViewById(R.id.fl_custom_title);
        this.mTextMenuLayout = (LinearLayout) rootView.findViewById(R.id.layout_text_menu);
        this.mTextMenuView = (TextView) rootView.findViewById(R.id.btn_text_menu);
        this.mTextMenuViewRight = (TextView) rootView.findViewById(R.id.btn_text_menu_2);
        this.mLocationIcon = (ImageView) rootView.findViewById(R.id.iv_location);
        this.mMenuOne = (ImageButton) rootView.findViewById(R.id.menu_one);
        this.mMenuMore = (ImageButton) rootView.findViewById(R.id.menu_more);
        this.mImgMenusLayout = (LinearLayout) rootView.findViewById(R.id.ll_menu_layout);
        this.mMenusLayout = (FrameLayout) rootView.findViewById(R.id.fl_menu_layout);
        this.mBottomStroke = rootView.findViewById(R.id.divider_toolbar);
        this.mFakeStatusBar = rootView.findViewById(R.id.status_bar_view);
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        public void noDoubleClick(View v) {
            int i = v.getId();
            if (i == R.id.menu_one) {
                if (AppToolbar.this.listener1 != null) {
                    AppToolbar.this.listener1.onClick(true);
                }
            } else if (i == R.id.menu_more) {
                if (AppToolbar.this.listener2 != null) {
                    AppToolbar.this.listener2.onClick(false);
                }

                if (AppToolbar.this.mDropdownItemListener != null) {
                    AppToolbar.this.showOrHideDropdownMenu();
                }
            } else if (i == R.id.rl_toolbar_location) {
                if (AppToolbar.this.mLocationClickListener != null) {
                    AppToolbar.this.mLocationClickListener.onClick(v);
                }
            } else if (i == R.id.btn_toolbar_back) {
                if (AppToolbar.this.mBackClickListener != null) {
                    AppToolbar.this.mBackClickListener.onClick(v);
                }
            } else if (i == R.id.btn_text_menu) {
                if (AppToolbar.this.mTextMenuClickListener != null) {
                    AppToolbar.this.mTextMenuClickListener.onClick(AppToolbar.this.mTextMenuView.getText().toString());
                }
            } else if (i == R.id.btn_text_menu_2 && AppToolbar.this.mTextMenuClickListener != null) {
                AppToolbar.this.mTextMenuClickListener.onClick(AppToolbar.this.mTextMenuViewRight.getText().toString());
            }

        }
    };

    public void setTitle(String mTitleString) {
        if (mTitleString == null) {
            mTitleString = "";
        }

        this.mTitleView.setText(mTitleString);
        if (!this.isTitleShow) {
            this.mTitleView.setVisibility(VISIBLE);
        }

        this.isTitleShow = true;
        if (this.isCustomTitleViewShow) {
            this.mCustomViewLayout.setVisibility(GONE);
            this.isCustomTitleViewShow = false;
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setNewStandard() {
        this.setNormalBackgroundColor(-1);
        this.mTitleView.setTextColor(this.getResources().getColor(R.color.text_title));
        this.mTitleView.setTextSize(2, 18.0F);
        this.mTitleView.setEms(10);
        this.mTitleView.getPaint().setFakeBoldText(false);
        this.mSubtitleView.setTextColor(NEW_STANDARD_TEXT_COLOR);
        this.mTextMenuView.setTextColor(ContextCompat.getColorStateList(this.getContext(), R.color.text_content_dark));
        this.mTextMenuViewRight.setTextColor(ContextCompat.getColorStateList(this.getContext(), R.color.text_content_dark));
        this.mBottomStroke.setVisibility(VISIBLE);
    }

    public TextView getTextMenu() {
        return this.mTextMenuView;
    }

    public TextView getTitleView() {
        return this.mTitleView;
    }

    public ImageButton getDrawableMenuLeft() {
        return this.mMenuOne;
    }

    public ImageButton getDrawableMenuRight() {
        return this.mMenuMore;
    }

    public void setBottomStrokeVisibility(int visibility) {
        this.mBottomStroke.setVisibility(visibility);
    }

    public int getCaculateHeight() {
        if (this.getHeight() != 0) {
            return this.getHeight();
        } else {
            int toolbarHeight = this.getResources().getDimensionPixelSize(R.dimen.toolbar_height);
            return this.mFakeStatusBar.getVisibility() != GONE ? toolbarHeight + this.getResources().getDimensionPixelSize(R.dimen.status_bar_height) : toolbarHeight;
        }
    }

    public void revertFromTransparentStyle() {
        this.setBackGroundAlpha(-1);
    }

    public void reset() {
        this.setTitle(this.mTitleString);
        this.setTextMenu(this.mTextMenuString);
        this.setTextMenuEnable(true);
        this.setupMenuItems(this.mMenuDrawableFir, (AppToolbar.OnMenuItemClickListener) null, this.mMenuDrawableSec, (AppToolbar.OnMenuItemClickListener) null, this.imgMenuPadding, this.imgMenuLayoutPadding);
        this.setBackButtonEnable(this.isAllowBack);
    }

    public boolean isBackGroundTransparent() {
        return this.isBackGroundTransparent;
    }

    public boolean isTitleShow() {
        return this.isTitleShow;
    }

    public boolean isCustomTitleViewShow() {
        return this.isCustomTitleViewShow;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setNormalBackgroundColor(int backgroundColor) {
        this.mToolbarLayout.setBackgroundColor(backgroundColor);
        this.mFakeStatusBar.setBackgroundColor(backgroundColor);
        this.backgroundColor = backgroundColor;
    }

    public void setBackResource(@DrawableRes int backRes) {
        this.mBackButton.setImageResource(backRes);
    }

    public void setBackgroundColor(int backgroundColor) {
        super.setBackgroundColor(backgroundColor);
        this.mToolbarLayout.setBackgroundColor(backgroundColor);
        this.mFakeStatusBar.setBackgroundColor(backgroundColor);
    }

    public void setDrawableColor(int color) {
        this.changeImageColor(this.mBackButton, color);
        this.changeImageColor(this.mMenuMore, color);
        this.changeImageColor(this.mMenuOne, color);
        this.changeImageColor(this.mLocationIcon, color);
    }

    private void changeImageColor(ImageView view, int color) {
        Drawable drawable = view.getDrawable();
        if (drawable != null) {
            drawable = drawable.mutate();
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, color);
            view.setImageDrawable(wrappedDrawable);
            view.invalidate();
        }
    }

    public void setTextColor(int color) {
        this.mTitleView.setTextColor(color);
        this.mTextMenuView.setTextColor(color);
        this.mTextMenuViewRight.setTextColor(color);
        this.mSubtitleView.setTextColor(color);
        this.mLocationText.setTextColor(color);
    }

    public void setTintOnScroll(int color) {
        this.setTextColor(color);
        this.setDrawableColor(color);
        this.setTextDrawableColor(color);
        this.tintColor = color;
    }

    private void setTextDrawableColor(int color) {
        this.setDrawableLeftTint(this.mTextMenuView, color);
        this.setDrawableLeftTint(this.mTextMenuViewRight, color);
    }

    private void setDrawableLeftTint(TextView textView, int color) {
        Drawable drawable = textView.getCompoundDrawables()[0];
        if (drawable != null) {
            drawable = drawable.mutate();
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, color);
            textView.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            textView.invalidate();
        }
    }

    public void showFakeStatusBarWhenTransparent() {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mFakeStatusBar.setVisibility(VISIBLE);
        } else {
            Log.e("toolbar", "版本低于19，设置不生效");
        }

    }

    public void setTextMenu(CharSequence mTextMenuString) {
        if (!TextUtils.isEmpty(mTextMenuString)) {
            this.mTextMenuString = mTextMenuString;
            this.mTextMenuView.setText(mTextMenuString);
            if (!this.isTextMenuShow) {
                this.setTextMenuLayoutVisible(true);
                this.mTextMenuView.setVisibility(VISIBLE);
            }

            this.isTextMenuShow = true;
            this.mTextMenuView.setOnClickListener(this.clickListener);
            this.mTextMenuViewRight.setOnClickListener(this.clickListener);
            if (this.isDrawableMenuShow) {
                this.mImgMenusLayout.setVisibility(GONE);
                this.isDrawableMenuShow = false;
            }
        } else {
            this.mTextMenuString = "";
            this.mTextMenuView.setVisibility(GONE);
            this.mTextMenuView.setText("");
            this.isTextMenuShow = false;
        }

    }

    public void setTextMenu(AppToolbar.TextMenuSetting settingLeft, AppToolbar.TextMenuSetting settingRight, AppToolbar.OnTextMenuClickListener listener) {
        if (!this.isTextMenuShow) {
            this.setTextMenuLayoutVisible(true);
        }

        this.isTextMenuShow = true;
        this.mTextMenuView.setOnClickListener(this.clickListener);
        this.mTextMenuViewRight.setOnClickListener(this.clickListener);
        this.setTextMenuAction(listener);
        if (this.isDrawableMenuShow) {
            this.mImgMenusLayout.setVisibility(GONE);
            this.isDrawableMenuShow = false;
        }

        this.configTextMenu(this.mTextMenuView, settingLeft);
        this.configTextMenu(this.mTextMenuViewRight, settingRight);
    }

    public void setupMenuItems(Drawable mMenuDrawable1, AppToolbar.OnMenuItemClickListener listener1, Drawable mMenuDrawable2, AppToolbar.OnMenuItemClickListener listener2) {
        this.setupMenuItems(mMenuDrawable1, listener1, mMenuDrawable2, listener2, this.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin), 0);
    }

    public void setupMenuItems(Drawable mMenuDrawable1, AppToolbar.OnMenuItemClickListener listener1, Drawable mMenuDrawable2, AppToolbar.OnMenuItemClickListener listener2, int imgMenuPadding, int imgMenuLayoutPadding) {
        if (mMenuDrawable1 == null && mMenuDrawable2 == null) {
            this.mImgMenusLayout.setVisibility(GONE);
        } else {
            this.isDrawableMenuShow = true;
            this.mImgMenusLayout.setVisibility(VISIBLE);
            this.mImgMenusLayout.setPadding(imgMenuLayoutPadding, 0, imgMenuLayoutPadding, 0);
            if (mMenuDrawable1 != null) {
                this.listener1 = listener1;
                this.mMenuOne.setVisibility(VISIBLE);
                this.mMenuOne.setOnClickListener(this.clickListener);
                this.mMenuOne.setImageDrawable(mMenuDrawable1);
                this.mMenuOne.setPadding(imgMenuPadding, 0, imgMenuPadding, 0);
            } else {
                this.mMenuOne.setVisibility(GONE);
            }

            if (mMenuDrawable2 != null) {
                this.listener2 = listener2;
                this.mDropdownItemListener = null;
                this.mMenuMore.setVisibility(VISIBLE);
                this.mMenuMore.setOnClickListener(this.clickListener);
                this.mMenuMore.setImageDrawable(mMenuDrawable2);
                this.mMenuMore.setPadding(imgMenuPadding, 0, imgMenuPadding, 0);
            } else {
                this.mMenuMore.setVisibility(GONE);
            }

            if (this.isTextMenuShow) {
                this.setTextMenuLayoutVisible(false);
                this.isTextMenuShow = false;
            }
        }

    }

    public void setSingleMenu(Drawable mMenuDrawableFir, AppToolbar.OnMenuItemClickListener listener) {
        this.listener1 = listener;
        if (mMenuDrawableFir != null) {
            this.isDrawableMenuShow = true;
            this.mMenuOne.setVisibility(VISIBLE);
            this.mImgMenusLayout.setVisibility(VISIBLE);
            this.mMenuOne.setOnClickListener(this.clickListener);
            this.mMenuOne.setImageDrawable(mMenuDrawableFir);
            this.mMenuMore.setVisibility(GONE);
            if (this.isTextMenuShow) {
                this.setTextMenuLayoutVisible(false);
                this.isTextMenuShow = false;
            }
        } else {
            this.mMenuOne.setVisibility(INVISIBLE);
            this.mMenuOne.setOnClickListener((View.OnClickListener) null);
        }

    }

    public void setSingleMenu(int iconRes, AppToolbar.OnMenuItemClickListener listener) {
        if (iconRes != 0) {
            this.setSingleMenu(this.context.getResources().getDrawable(iconRes), listener);
        } else {
            this.setSingleMenu((Drawable) null, listener);
        }

    }

    public void setLocationText(String location) {
        if (!TextUtils.isEmpty(location)) {
            this.mLocationText.setText(location);
            if (!this.isLocationShow) {
                this.isLocationShow = true;
                this.mLocationLayout.setVisibility(VISIBLE);
                this.mLocationLayout.setOnClickListener(this.clickListener);
            }

            if (this.isAllowBack) {
                this.isAllowBack = false;
                this.mBackLayout.setVisibility(GONE);
            }
        } else {
            Log.w("AppToolbar", "location is null, make it gone");
            this.mLocationText.setText("");
            this.mLocationLayout.setVisibility(GONE);
            this.isLocationShow = false;
        }

    }

    public String getLocationText() {
        return this.mLocationText.getText().toString();
    }

    public void showSearchView(String hint, AppToolbar.OnSearchListener listener) {
        this.showSearchView(hint, listener, (TextWatcher) null);
    }

    public void showSearchView(final String hint, final boolean searchPop, AppToolbar.OnSearchListener listener, TextWatcher onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
        this.onSearchListener = listener;
        this.mSearchHint = hint;
        this.setSingleMenu(this.getResources().getDrawable(R.drawable.ic_search_black), new AppToolbar.OnMenuItemClickListener() {
            public void onClick(boolean first) {
                AppToolbar.this.openSearchView(hint, searchPop);
            }
        });
    }

    public void showSearchView(String hint, AppToolbar.OnSearchListener listener, TextWatcher onTextChangedListener) {
        this.showSearchView(hint, true, listener, onTextChangedListener);
    }

    public void openSearchView(final String hint, final boolean needSearchPop) {
        if (this.mSearchBarLayout == null) {
            this.mSearchBarLayout = LayoutInflater.from(this.context).inflate(R.layout.lib_toolbar_search_new, this.mCustomViewLayout, false);
            ((MarginLayoutParams) this.mCustomViewLayout.getLayoutParams()).leftMargin = this.getResources().getDimensionPixelSize(R.dimen.w_10);
            this.mCustomViewLayout.requestLayout();
            this.mSearchBar = (EditText) this.mSearchBarLayout.findViewById(R.id.edit_search);
            final View searchPop = this.mSearchBarLayout.findViewById(R.id.btn_edit_clear);
            this.mSearchBar.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)) {
                        searchPop.setVisibility(GONE);
                    } else {
                        searchPop.setVisibility(VISIBLE);
                    }

                }
            });
            searchPop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AppToolbar.this.mSearchBar.setText("");
                }
            });
            final PopupWindow searchPop1 = this.initSearchPop(needSearchPop);
            this.mSearchBarLayout.setTag(searchPop1);
            this.mSearchBar.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (AppToolbar.this.onTextChangedListener != null) {
                        AppToolbar.this.onTextChangedListener.beforeTextChanged(s, start, count, after);
                    }

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (AppToolbar.this.onTextChangedListener != null) {
                        AppToolbar.this.onTextChangedListener.onTextChanged(s, start, before, count);
                    }

                }

                public void afterTextChanged(Editable s) {
                    AppToolbar.this.mSearchPopEdit.setText("搜索\"" + s + "\"");
                    if (TextUtils.isEmpty(s.toString())) {
                        ((View) AppToolbar.this.mSearchPopEdit.getParent()).setVisibility(GONE);
                    } else {
                        ((View) AppToolbar.this.mSearchPopEdit.getParent()).setVisibility(VISIBLE);
                    }

                    if (AppToolbar.this.onTextChangedListener != null) {
                        AppToolbar.this.onTextChangedListener.afterTextChanged(s);
                    }

                }
            });
            this.mSearchBar.setHint(hint);
            this.mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == 3 || event != null && event.getKeyCode() == 66) {
                        searchPop1.dismiss();
                        String content = AppToolbar.this.mSearchBar.getText().toString();
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(AppToolbar.this.getContext(), hint, Toast.LENGTH_SHORT).show();
                        } else {
                            SoftInputUtils.closeSoftInput(AppToolbar.this.context, AppToolbar.this.mSearchBar);
                            if (AppToolbar.this.onSearchListener != null) {
                                AppToolbar.this.onSearchListener.onSearch(content);
                            }
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }

        PopupWindow searchPop2 = (PopupWindow) this.mSearchBarLayout.getTag();
        if (!searchPop2.isShowing() && needSearchPop) {
            this.showPopupWindow(searchPop2);
        }

        this.mBackLayout.setVisibility(GONE);
        this.isLastTitleStyleCustom = this.isCustomTitleViewShow;
        this.setCustomTitleView(this.mSearchBarLayout);
        SoftInputUtils.openSoftInput(this.context, this.mSearchBar);
        if (this.onSearchListener != null) {
            this.onSearchListener.onOpen();
        }

        this.setTextMenu(this.getResources().getString(R.string.cancel), new AppToolbar.OnTextMenuClickListener() {
            public void onClick(String mMenuText) {
                PopupWindow searchPop = (PopupWindow) AppToolbar.this.mSearchBarLayout.getTag();
                if (searchPop != null && searchPop.isShowing()) {
                    searchPop.dismiss();
                } else {
                    if (AppToolbar.this.mSearchBarLayout != null && AppToolbar.this.mSearchBarLayout.isShown()) {
                        AppToolbar.this.hideSearchView();
                    }

                    AppToolbar.this.showSearchView(AppToolbar.this.mSearchHint, needSearchPop, AppToolbar.this.onSearchListener, AppToolbar.this.onTextChangedListener);
                }

            }
        });
    }

    private PopupWindow initSearchPop(final boolean needSearchPop) {
        View searchPopLayout = LayoutInflater.from(this.getContext()).inflate(R.layout.pop_search, this, false);
        this.mSearchPopEdit = (TextView) searchPopLayout.findViewById(R.id.text_search_content);
        ImageView searchImg = (ImageView) searchPopLayout.findViewById(R.id.image_search);
        searchImg.setColorFilter(this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        final PopupWindow searchPop = new PopupWindow(searchPopLayout, -1, -1);
        searchPop.setInputMethodMode(1);
        searchPop.setSoftInputMode(16);
        searchPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                AppToolbar.this.hideSearchView();
                AppToolbar.this.showSearchView(AppToolbar.this.mSearchHint, needSearchPop, AppToolbar.this.onSearchListener, AppToolbar.this.onTextChangedListener);
            }
        });
        searchPopLayout.findViewById(R.id.layout_search).setOnClickListener(new NoDoubleClickListener() {
            public void noDoubleClick(View v) {
                if (AppToolbar.this.onSearchListener != null) {
                    AppToolbar.this.onSearchListener.onSearch(AppToolbar.this.mSearchBar.getText().toString().trim());
                }

                searchPop.dismiss();
            }
        });
        searchPopLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (searchPop != null && searchPop.isShowing()) {
                    searchPop.dismiss();
                }

                return false;
            }
        });
        return searchPop;
    }

    public void hideSearchView() {
        if (this.mSearchBarLayout != null) {
            SoftInputUtils.closeSoftInput(this.context, this.mSearchBar);
            if (this.isLastTitleStyleCustom) {
                this.setCustomTitleView(this.mLastCustomTitleView);
            } else {
                this.setTitle(this.mTitleView.getText().toString());
            }

            if (this.onSearchListener != null) {
                this.onSearchListener.onClose();
            }

            this.mBackLayout.setVisibility(VISIBLE);
        }
    }

    public void setSearchViewAlways(final String hint, final AppToolbar.OnSearchListener listener) {
        this.mSearchBarLayout = LayoutInflater.from(this.context).inflate(R.layout.lib_toolbar_search_new, this.mCustomViewLayout, false);
        this.mSearchBarLayout.findViewById(R.id.btn_edit_clear).setOnClickListener(new NoDoubleClickListener() {
            public void noDoubleClick(View v) {
                if (AppToolbar.this.mSearchBar != null) {
                    AppToolbar.this.mSearchBar.setText("");
                }

            }
        });
        this.mSearchBar = (EditText) this.mSearchBarLayout.findViewById(R.id.edit_search);
        this.mSearchBar.setHint(hint);
        final View clearButton = this.mSearchBarLayout.findViewById(R.id.btn_edit_clear);
        this.mSearchBar.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    clearButton.setVisibility(GONE);
                } else {
                    clearButton.setVisibility(VISIBLE);
                }

            }
        });
        this.mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 3 || event != null && event.getKeyCode() == 66) {
                    String content = AppToolbar.this.mSearchBar.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(AppToolbar.this.getContext(), hint, Toast.LENGTH_SHORT).show();
                    } else {
                        SoftInputUtils.closeSoftInput(AppToolbar.this.context, AppToolbar.this.mSearchBar);
                        listener.onSearch(content);
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
        this.setCustomTitleView(this.mSearchBarLayout);
        this.setSingleMenu(this.getResources().getDrawable(R.drawable.ic_search_black), new AppToolbar.OnMenuItemClickListener() {
            public void onClick(boolean first) {
                String content = AppToolbar.this.mSearchBar.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(AppToolbar.this.getContext(), hint, Toast.LENGTH_SHORT).show();
                } else {
                    listener.onSearch(AppToolbar.this.mSearchBar.getText().toString());
                }

            }
        });
    }

    public void setOnLocationClickListener(View.OnClickListener listener) {
        this.mLocationClickListener = listener;
    }

    public void setOnBackAction(View.OnClickListener listener) {
        this.mBackClickListener = listener;
    }

    public void setBackButtonEnable(boolean enable) {
        if (enable) {
            this.isAllowBack = true;
            this.mBackButton.setVisibility(VISIBLE);
            this.mBackButton.setOnClickListener(this.clickListener);
            this.mBackLayout.setVisibility(VISIBLE);
            if (this.isLocationShow) {
                this.isLocationShow = false;
                this.mLocationLayout.setVisibility(GONE);
            }
        } else {
            this.isAllowBack = false;
            this.mBackButton.setVisibility(GONE);
            this.mBackButton.setOnClickListener(this.clickListener);
            this.mBackLayout.setVisibility(GONE);
        }

    }

    public void setSubTitle(String subTitleStr) {
        if (!TextUtils.isEmpty(subTitleStr)) {
            this.mSubtitleView.setText(subTitleStr);
            this.mSubtitleView.setVisibility(VISIBLE);
            if (!this.isAllowBack) {
                this.isAllowBack = true;
                this.mBackButton.setVisibility(GONE);
                this.mBackLayout.setVisibility(GONE);
            }

            if (this.isLocationShow) {
                this.isLocationShow = false;
                this.mLocationLayout.setVisibility(GONE);
            }
        }

    }

    public void setMultiMenus(String[] items, AdapterView.OnItemClickListener listener) {
        if (items != null) {
            this.mDropdownItemListener = listener;
            this.mDropdownItems = items;
            if (this.isTextMenuShow) {
                this.isTextMenuShow = false;
                this.setTextMenuLayoutVisible(false);
            }

            this.isDrawableMenuShow = true;
            if (this.mMenuMore.getDrawable() == null) {
                this.mMenuMore.setImageResource(R.drawable.ic_more_black);
            }

            this.mImgMenusLayout.setVisibility(VISIBLE);
            this.mMenuMore.setVisibility(VISIBLE);
            this.mMenuMore.setOnClickListener(this.clickListener);
            this.listener2 = null;
        } else {
            this.mMenuMore.setVisibility(GONE);
            this.mDropdownItemListener = null;
        }

    }

    public void setMultiMenus(int iconRes, String[] items, AdapterView.OnItemClickListener listener) {
        this.setMultiMenu(iconRes, items, (int[]) null, listener);
    }

    public void setMultiMenu(int iconRes, String[] items, int[] drawables, AdapterView.OnItemClickListener listener) {
        if (items != null) {
            this.mDropdownItemListener = listener;
            this.mDropdownItems = items;
            if (this.isTextMenuShow) {
                this.isTextMenuShow = false;
                this.setTextMenuLayoutVisible(false);
            }

            this.mMultiMenuResource = drawables;
            this.isDrawableMenuShow = true;
            this.mMenuMore.setImageResource(iconRes);
            this.mImgMenusLayout.setVisibility(VISIBLE);
            this.mMenuMore.setVisibility(VISIBLE);
            this.mMenuMore.setOnClickListener(this.clickListener);
            this.listener2 = null;
        } else {
            this.mMenuMore.setVisibility(GONE);
            this.mDropdownItemListener = null;
        }

    }

    public void setTextMenuAction(AppToolbar.OnTextMenuClickListener listener) {
        this.mTextMenuClickListener = listener;
    }

    public void setTextMenu(CharSequence text, AppToolbar.OnTextMenuClickListener listener) {
        this.setTextMenu(text);
        this.setTextMenuAction(listener);
    }

    public void setTextMenu(CharSequence text, int padding, AppToolbar.OnTextMenuClickListener listener) {
        this.setTextMenu(text);
        this.mTextMenuView.setPadding(padding, 0, padding, 0);
        this.setTextMenuAction(listener);
    }

    public void setTextMenuColor(@ColorInt int color) {
        this.mTextMenuView.setTextColor(new ColorStateList(new int[][]{{16842910}, {-16842910}}, new int[]{color, ContextCompat.getColor(this.getContext(), R.color.text_content_light)}));
    }

    public void setTextMenuEnable(boolean enable) {
        this.mTextMenuView.setEnabled(enable);
    }

    public void setTextMenuRightEnable(boolean enable) {
        this.mTextMenuViewRight.setEnabled(enable);
    }

    public void showWebViewCloseButton(View.OnClickListener listener) {
        this.mSubtitleView.setText("关闭");
        this.mSubtitleView.setVisibility(VISIBLE);
        this.mSubtitleView.setOnClickListener(listener);
        this.mTitleView.setEms(8);
    }

    public void closeWebViewCloseButton() {
        this.mSubtitleView.setVisibility(GONE);
    }

    public void setCustomTitleView(View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        if (this.isLastTitleStyleCustom) {
            this.mLastCustomTitleView = this.mCustomViewLayout.getChildAt(0);
        }

        this.mCustomViewLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        this.mCustomViewLayout.addView(view, new android.support.v7.widget.LinearLayoutCompat.LayoutParams(-1, layoutParams == null ? -2 : layoutParams.height));
        this.isCustomTitleViewShow = true;
        this.mCustomViewLayout.setVisibility(VISIBLE);
        this.isTitleShow = false;
        this.mTitleView.setVisibility(GONE);
    }

    public void setBackGroundTransparent() {
        this.setBackGroundTransparent(false);
    }

    public void setBackGroundTransparent(boolean titleTransparent) {
        StatusBarUtils.setStatusBarLightMode((Activity) this.getContext());
//        StatusBarUtils.setTranslucentForImageView((Activity) this.getContext(), 0, (View) null);
        if (titleTransparent) {
            this.mTitleView.setVisibility(INVISIBLE);
        }

        this.mBottomStroke.setVisibility(GONE);
        this.isBackGroundTransparent = true;
        this.setBackgroundDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.shadow_status_bar));
        this.mToolbarLayout.setBackgroundColor(0);
        this.mFakeStatusBar.setBackgroundColor(0);
        this.mFakeStatusBar.setVisibility(VISIBLE);
        this.setTintOnScroll(-1);
    }

    public void setBackGroundAlpha(int color) {
        this.mTitleView.setVisibility(VISIBLE);
        this.mToolbarLayout.setBackgroundColor(color);
        this.mFakeStatusBar.setBackgroundColor(color);
        if (color == this.backgroundColor) {
            this.mBottomStroke.setVisibility(VISIBLE);
            this.setTintOnScroll(NEW_STANDARD_TEXT_COLOR);
            this.mTitleView.setTextColor(this.getResources().getColor(R.color.text_title));
            this.setBackgroundDrawable((Drawable) null);
        } else {
            this.mBottomStroke.setVisibility(GONE);
        }

    }

    public void setDrawableTitle(Drawable drawableTitle) {
        ImageView titleView = new ImageView(this.getContext());
        titleView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
        titleView.setPadding(0, this.getResources().getDimensionPixelSize(R.dimen.w_7_5), 0, this.getResources().getDimensionPixelSize(R.dimen.w_7_5));
        titleView.setImageDrawable(drawableTitle);
        this.setCustomTitleView(titleView);
    }

    public void showMultiTitleTab(ViewPager linkPager, final BaseRecyclerAdapter.OnItemClickListener onItemClickListener, String... tabs) {
        View inflate = LayoutInflater.from(this.getContext()).inflate(R.layout.recyclerview, this.mCustomViewLayout, false);
        this.mTabRecycler = (RecyclerView) inflate.findViewById(R.id.recycler);
        RecyclerViewInitHelper.initWrapContentHorizontalLayout(this.mTabRecycler);
        this.titleTabAdapter = new AppToolbar.TitleTabAdapter(this.getContext(), R.layout.lib_item_new_title_tab, Arrays.asList(tabs));
        ((MarginLayoutParams) this.mCustomViewLayout.getLayoutParams()).leftMargin = this.getResources().getDimensionPixelSize(R.dimen.w_40);
        ((MarginLayoutParams) this.mCustomViewLayout.getLayoutParams()).rightMargin = this.getResources().getDimensionPixelSize(R.dimen.w_40);
        this.mCustomViewLayout.requestLayout();
        this.mTabRecycler.setAdapter(this.titleTabAdapter);
        titleTabAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecViewHolder var1, int var2) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(var1, var2);
                }
            }
        });

        if (linkPager != null) {
            linkPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    AppToolbar.this.titleTabAdapter.setSelectPosition(position);
                }

                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        this.setCustomTitleView(this.mTabRecycler);
    }

    public void switchTab(int position) {
        if (this.mTabRecycler != null) {
            this.titleTabAdapter.setSelectPosition(position);
        }

    }

    public void showNavigationTitle(final String[] items, int selection, final android.content.DialogInterface.OnClickListener listener) {
        View navigationLayout = LayoutInflater.from(this.getContext()).inflate(R.layout.lib_layout_navigation_black, (ViewGroup) null);
        final CheckBox navigationBox = (CheckBox) navigationLayout.findViewById(R.id.navigation_box);
        final View attach = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_navigation_drop, (ViewGroup) null);
        WrapContentListView navigationList = (WrapContentListView) attach.findViewById(R.id.list_navigation);
        final AppToolbar.NavigationTitleAdapter adapter = new AppToolbar.NavigationTitleAdapter(this.getContext(), selection, Arrays.asList(items));
        navigationList.setAdapter(adapter);
        attach.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (AppToolbar.this.mNavigationPop != null && AppToolbar.this.mNavigationPop.isShowing()) {
                    AppToolbar.this.mNavigationPop.dismiss();
                }

            }
        });
        adapter.setOnItemClickListener(new BaseListAdapter.OnItemClickListener<String>() {
            public void onItemClick(BaseListHolder holder, String data, int position) {
                if (listener != null) {
                    listener.onClick((DialogInterface) null, position);
                }

                AppToolbar.this.mNavigationPop.dismiss();
                navigationBox.setChecked(false);
                navigationBox.setText(items[position]);
                adapter.setSelectPosition(position);
            }
        });
        navigationBox.setText(items[selection]);
        navigationBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (AppToolbar.this.mNavigationPop == null) {
                        AppToolbar.this.mNavigationPop = new PopupWindow(attach, -1, -1);
                        AppToolbar.this.mNavigationPop.update();
                        AppToolbar.this.mNavigationPop.setFocusable(true);
                        AppToolbar.this.mNavigationPop.setBackgroundDrawable(new BitmapDrawable());
                        AppToolbar.this.mNavigationPop.setOutsideTouchable(true);
                        AppToolbar.this.showPopupWindow(AppToolbar.this.mNavigationPop);
                        AppToolbar.this.mNavigationPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            public void onDismiss() {
                                navigationBox.setChecked(false);
                            }
                        });
                        navigationBox.setChecked(true);
                    } else if (AppToolbar.this.mNavigationPop.isShowing()) {
                        AppToolbar.this.mNavigationPop.dismiss();
                        navigationBox.setChecked(false);
                    } else {
                        AppToolbar.this.mNavigationPop.update();
                        AppToolbar.this.showPopupWindow(AppToolbar.this.mNavigationPop);
                        navigationBox.setChecked(true);
                    }
                }

            }
        });
        this.setCustomTitleView(navigationLayout);
    }

    public void showNavigationTitle(String[] items, android.content.DialogInterface.OnClickListener listener) {
        this.showNavigationTitle(items, 0, listener);
    }

    public void setMenuNoticeSign(int where, boolean add, String num, int topMargin, int rightMargin) {
        this.removeRedPoint(this.getNoticeView(where));
        if (add) {
            this.addRedPoint(this.getNoticeView(where), num, topMargin, rightMargin);
        }

    }

    public void clearMenuNoticeSign() {
        this.removeRedPoint(this.mMenuOne);
        this.removeRedPoint(this.mMenuMore);
        this.removeRedPoint(this.mTextMenuView);
    }

    public void setTitleTabNoticeSign(int position, boolean add) {
        if (this.titleTabAdapter != null) {
            if (add) {
                this.titleTabAdapter.setNotice(position);
            } else {
                this.titleTabAdapter.cleanNotice();
            }

        }
    }

    public void clearTitleTabNoticeSign() {
        if (this.titleTabAdapter != null) {
            this.titleTabAdapter.cleanNotice();
        }
    }

    private void setTextMenuLayoutVisible(boolean visible) {
        this.mTextMenuViewRight.setVisibility(!visible ? GONE : this.mTextMenuViewRight.getVisibility());
        this.mTextMenuView.setVisibility(!visible ? GONE : this.mTextMenuViewRight.getVisibility());
        this.mTextMenuLayout.setVisibility(visible ? VISIBLE : GONE);
    }

    private View getNoticeView(int where) {
        switch (where) {
            case 0:
                return this.mMenuOne;
            case 1:
                return this.mMenuMore;
            case 2:
                return this.mTextMenuView;
            default:
                return this.mMenuOne;
        }
    }

    private void addRedPoint(View addedView, String num, int topMargin, int rightMargin) {
        if (addedView.getParent() != null) {
            int position = 0;
            ViewGroup parent = (ViewGroup) addedView.getParent();

            for (int redPointParent = 0; redPointParent < parent.getChildCount(); ++redPointParent) {
                if (parent.getChildAt(redPointParent) == addedView) {
                    position = redPointParent;
                    break;
                }
            }

            parent.removeView(addedView);
            FrameLayout var10 = new FrameLayout(this.getContext());
            android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(-2, -2);
            params.gravity = 5;
            params.rightMargin = (int) ((float) rightMargin * this.getResources().getDisplayMetrics().density);
            params.topMargin = (int) ((float) topMargin * this.getResources().getDisplayMetrics().density);
            var10.addView(addedView);
            RedPointView pointView = new RedPointView(this.getContext());
            pointView.setText(num);
            var10.addView(pointView, params);
            parent.addView(var10, position, addedView.getLayoutParams());
        }
    }

    private void removeRedPoint(View removeView) {
        if (removeView.getParent() != null) {
            if (removeView.getParent() instanceof FrameLayout) {
                FrameLayout parent = (FrameLayout) removeView.getParent();
                if (parent.getChildCount() == 2) {
                    if (parent.getChildAt(1) instanceof RedPointView) {
                        int position = 0;
                        ViewGroup finalParent = (ViewGroup) parent.getParent();

                        for (int i = 0; i < finalParent.getChildCount(); ++i) {
                            if (finalParent.getChildAt(i) == parent) {
                                position = i;
                                break;
                            }
                        }

                        parent.removeView(removeView);
                        finalParent.removeView(parent);
                        finalParent.addView(removeView, position, removeView.getLayoutParams());
                    }
                }
            }
        }
    }

    private void configTextMenu(TextView textView, AppToolbar.TextMenuSetting setting) {
        textView.setVisibility(setting == null ? GONE : VISIBLE);
        if (setting != null) {
            textView.setText(setting.string);
            if (setting.drawable != null) {
                setting.drawable.setBounds(setting.rect);
                textView.setCompoundDrawables(setting.drawable, (Drawable) null, (Drawable) null, (Drawable) null);
                textView.setCompoundDrawablePadding(0);
                if (this.tintColor != 2147483647) {
                    this.setDrawableLeftTint(textView, this.tintColor);
                }
            } else {
                textView.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
            }

            if (textView.getId() != R.id.btn_text_menu) {
                textView.setPadding((int) (this.getResources().getDisplayMetrics().density * setting.padding), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
            } else {
                textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), (int) (this.getResources().getDisplayMetrics().density * setting.padding), textView.getPaddingBottom());
            }

            textView.setTextColor(new ColorStateList(new int[][]{{16842910}, {-16842910}}, new int[]{setting.textColor, ContextCompat.getColor(this.getContext(), R.color.text_content_light)}));
        }
    }

    private void showOrHideDropdownMenu() {
        if (this.mDropDownMenu != null && this.mDropDownMenu.isShowing()) {
            if (this.mDropDownMenu.isShowing()) {
                this.mDropDownMenu.dismiss();
            } else {
                this.mDropDownMenu.update();
                this.mDropDownMenu.showAsDropDown(this, this.getResources().getDisplayMetrics().widthPixels - this.getResources().getDimensionPixelSize(R.dimen.w_160), 10);
            }
        } else {
            View inflate = LayoutInflater.from(this.getContext()).inflate(R.layout.dropdown_list, (ViewGroup) null);
            RecyclerView mDropDownList = (RecyclerView) inflate.findViewById(R.id.hotalk_menu_listview);
            mDropDownList.setBackgroundDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.bg_new_drop_down));
            mDropDownList.setPadding(0, 10, 0, 0);
            this.initDropdownList(mDropDownList);
            this.mDropDownMenu = new PopupWindow(inflate, this.getResources().getDimensionPixelSize(R.dimen.w_160), -2);
            this.mDropDownMenu.update();
            this.mDropDownMenu.setFocusable(true);
            this.mDropDownMenu.setBackgroundDrawable(new BitmapDrawable());
            this.mDropDownMenu.setOutsideTouchable(true);
            this.mDropDownMenu.showAsDropDown(this, this.getResources().getDisplayMetrics().widthPixels - this.getResources().getDimensionPixelSize(R.dimen.w_160), 10);
        }

    }

    private void initDropdownList(RecyclerView mDropDownList) {
        RecyclerViewInitHelper.initWrapContentVerticalLayout(mDropDownList);
        mDropDownList.addItemDecoration(new VerticalItemDecoration(1, 1375731711));
        final int itemRes = R.layout.lib_item_dropdown_black;
        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(getContext(), itemRes, Arrays.asList(mDropdownItems)) {
            @Override
            public void onBindViewHolder(BaseRecViewHolder holder, int position) {
                TextView menuText = holder.getTextView(R.id.tv_more_menu);
                menuText.setText(mDropdownItems[position]);
                if (mMultiMenuResource != null) {
                    Drawable drawable = mContext.getResources().getDrawable(mMultiMenuResource[position]);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    menuText.setCompoundDrawables(drawable, null, null, null);
                }
            }
        };
        mDropDownList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            public void onItemClick(BaseRecViewHolder holder, int position) {
                AppToolbar.this.mDropDownMenu.dismiss();
                if (AppToolbar.this.mDropdownItemListener != null) {
                    AppToolbar.this.mDropdownItemListener.onItemClick((AdapterView) null, holder.getConvertView(), position, 0L);
                }

            }
        });
    }

    private void showPopupWindow(PopupWindow popupWindow) {
        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAsDropDown(this);
        } else {
            int[] location = new int[2];
            this.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            popupWindow.showAtLocation(this, 0, 0, y + this.getHeight());
        }

    }

    private class TitleTabAdapter extends BaseRecyclerAdapter<String> {
        SparseBooleanArray noticeMap = new SparseBooleanArray();
        int selectPosition = 0;

        public TitleTabAdapter(Context context, int itemLayout, List<String> list) {
            super(context, itemLayout, list);
        }

        protected void changeViewSize(View view) {
            android.support.v7.widget.RecyclerView.LayoutParams params = new android.support.v7.widget.RecyclerView.LayoutParams(-2, -1);
            view.setLayoutParams(params);
            if (this.list.size() == 2) {
                int padding = AppToolbar.this.getContext().getResources().getDimensionPixelOffset(R.dimen.w_20);
                view.setPadding(padding, 0, padding, 0);
            }

        }

        void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
            this.notifyDataSetChanged();
        }

        void setNotice(int position) {
            this.noticeMap.put(position, true);
            this.notifyDataSetChanged();
        }

        void clearNotice(int selectPosition) {
            this.noticeMap.put(selectPosition, false);
            this.notifyDataSetChanged();
        }

        void cleanNotice() {
            this.noticeMap.clear();
            this.notifyDataSetChanged();
        }

        protected void onClickHook(BaseRecViewHolder holder, int adapterPosition) {
            this.selectPosition = adapterPosition;
            this.notifyDataSetChanged();
        }

        public void onBindViewHolder(BaseRecViewHolder holder, int position) {
            TextView convertView = holder.getTextView(R.id.tv_title_tab);
            if (position == this.selectPosition) {
                convertView.setTextColor(ContextCompat.getColor(AppToolbar.this.getContext(), R.color.colorPrimary3_0));
            } else {
                convertView.setTextColor(ContextCompat.getColor(AppToolbar.this.getContext(), R.color.text_secondary));
            }

            convertView.setText((CharSequence) this.list.get(position));
            if (this.noticeMap.get(position, false)) {
                holder.get(R.id.red_sign).setVisibility(VISIBLE);
            } else {
                holder.get(R.id.red_sign).setVisibility(INVISIBLE);
            }

        }
    }

    private class NavigationTitleAdapter extends BaseListAdapter<String> {
        int selectPosition;

        public NavigationTitleAdapter(Context var1, int context, List<String> selection) {
            super(var1, R.layout.lib_item_navigation, selection);
            this.selectPosition = context;
        }

        public void onBindViewHolder(BaseListHolder holder, int position) {
            TextView textView = holder.getTextView(R.id.tv_navigation);
            textView.setText((CharSequence) this.list.get(position));
            if (position == this.selectPosition) {
                textView.setTextColor(ContextCompat.getColor(AppToolbar.this.getContext(), R.color.colorPrimary));
                holder.getView(R.id.iv_navigation_tag).setVisibility(VISIBLE);
            } else {
                textView.setTextColor(ContextCompat.getColor(AppToolbar.this.getContext(), R.color.new_text_title_color));
                holder.getView(R.id.iv_navigation_tag).setVisibility(GONE);
            }

        }

        void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
        }
    }

    public static class TextMenuSetting {
        private int textColor;
        private CharSequence string;
        private Drawable drawable;
        private Rect rect;
        private float padding = 12.5F;

        public TextMenuSetting(Context context, Drawable drawable, Rect rect, float padding) {
            this.drawable = drawable;
            if (rect != null) {
                this.rect = rect;
            } else {
                this.rect = new Rect(0, 0, (int) (context.getResources().getDisplayMetrics().density * 20.0F), (int) (context.getResources().getDisplayMetrics().density * 20.0F));
            }

            if (padding != 0.0F) {
                this.padding = padding;
            }

        }

        public TextMenuSetting(Context context, Drawable drawable, int size, float padding) {
            this.drawable = drawable;
            if (size == 0) {
                this.rect = new Rect(0, 0, (int) (context.getResources().getDisplayMetrics().density * 20.0F), (int) (context.getResources().getDisplayMetrics().density * 20.0F));
            } else {
                this.rect = new Rect(0, 0, (int) (context.getResources().getDisplayMetrics().density * (float) size), (int) (context.getResources().getDisplayMetrics().density * (float) size));
            }

            if (padding != 0.0F) {
                this.padding = padding;
            }

        }

        public TextMenuSetting(int textColor, CharSequence string) {
            this.textColor = textColor;
            this.string = string;
        }
    }

    public interface OnTextMenuClickListener {
        void onClick(String var1);
    }

    public abstract static class OnSearchListener {
        public OnSearchListener() {
        }

        public abstract void onSearch(String var1);

        protected void onOpen() {
        }

        protected void onClose() {
        }
    }

    public interface OnMenuItemClickListener {
        void onClick(boolean var1);
    }
}