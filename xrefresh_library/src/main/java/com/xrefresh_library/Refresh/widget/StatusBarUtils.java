package com.xrefresh_library.Refresh.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.xrefresh_library.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by liub on 2017/11/6 .
 */

class StatusBarUtils {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 0;

    public StatusBarUtils() {
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

//    public static void setWhiteStatusBar(Activity activity) {
//        if(VERSION.SDK_INT >= 21) {
//            activity.getWindow().addFlags(-2147483648);
//            activity.getWindow().clearFlags(67108864);
//            if(setStatusBarLightMode(activity) == 0) {
//                activity.getWindow().setStatusBarColor(-16777216);
//            } else {
//                activity.getWindow().setStatusBarColor(-1);
//            }
//        } else if(VERSION.SDK_INT >= 19) {
//            activity.getWindow().addFlags(67108864);
//            ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
//            int count = decorView.getChildCount();
//            if(count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
//                decorView.getChildAt(count - 1).setBackgroundDrawable(ContextCompat.getDrawable(activity.getApplicationContext(), drawable.shadow_status_bar));
//            } else {
//                StatusBarView statusView = createGradientStatusBarView(activity);
//                decorView.addView(statusView);
//            }
//
//            setRootView(activity);
//        }
//
//    }

    public static void setColor(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(-2147483648);
            activity.getWindow().clearFlags(67108864);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                StatusBarView statusView = createStatusBarView(activity, color, statusBarAlpha);
                decorView.addView(statusView);
            }

//            setRootView(activity);
        }

    }

    public static void setTranslucentForImageView(Activity activity, int statusBarAlpha, View needOffsetView) {
        if (VERSION.SDK_INT >= 19) {
            setTransparentForWindow(activity);
//            addTranslucentView(activity, statusBarAlpha);
            if (needOffsetView != null) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) needOffsetView.getLayoutParams();
                layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
            }

        }
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt((Object) null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }

                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception var8) {
                ;
            }
        }

        return result;
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();

            try {
                boolean darkModeFlag = false;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag1 = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
                if (dark) {
                    extraFlagField.invoke(window, new Object[]{Integer.valueOf(darkModeFlag1), Integer.valueOf(darkModeFlag1)});
                } else {
                    extraFlagField.invoke(window, new Object[]{Integer.valueOf(0), Integer.valueOf(darkModeFlag1)});
                }

                result = true;
            } catch (Exception var8) {
                ;
            }
        }

        return result;
    }

    public static int setStatusBarLightMode(Activity activity) {
        byte result = 0;
        if (VERSION.SDK_INT >= 19) {
            if (VERSION.SDK_INT >= 23) {
                activity.getWindow().addFlags(-2147483648);
                activity.getWindow().getDecorView().setSystemUiVisibility(8192);
                MIUISetStatusBarLightMode(activity.getWindow(), true);
                result = 3;
            } else if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            }
        }

        return result;
    }

    public static void setTransparentForWindow(Activity activity) {
        if (VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(0);
            if (VERSION.SDK_INT >= 23) {
                activity.getWindow().getDecorView().setSystemUiVisibility(9472);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(1280);
            }
        } else if (VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(67108864, 67108864);
        }

    }

//    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
//        if (VERSION.SDK_INT < 23) {
//            ViewGroup contentView = (ViewGroup) activity.findViewById(16908290);
//            if (contentView.getChildCount() > 1) {
//                contentView.getChildAt(1).setBackgroundDrawable(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.shadow_status_bar));
//            } else {
//                contentView.addView(createGradientStatusBarView(activity));
//            }
//
//        }
//    }

    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

//    private static void setRootView(Activity activity) {
//        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(16908290)).getChildAt(0);
//        rootView.setFitsSystemWindows(true);
//        rootView.setClipToPadding(true);
//    }

    private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        StatusBarView statusBarView = new StatusBarView(activity);
        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    private static StatusBarView createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        StatusBarView statusBarView = new StatusBarView(activity);
        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    private static StatusBarView createGradientStatusBarView(Activity activity) {
        StatusBarView statusBarView = new StatusBarView(activity);
        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundDrawable(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.shadow_status_bar));
        return statusBarView;
    }
}