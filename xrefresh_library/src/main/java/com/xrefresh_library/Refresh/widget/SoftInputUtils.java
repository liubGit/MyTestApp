package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by liub on 2017/11/6 .
 */

public class SoftInputUtils {
    SoftInputUtils() {
    }

    static void openSoftInput(Context context, final View focusView) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
        focusView.post(new Runnable() {
            public void run() {
                focusView.requestFocus();
                imm.showSoftInput(focusView, 2);
            }
        });
    }

    static void closeSoftInput(Context context, View focusView) {
        if (focusView != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            focusView.clearFocus();
        }
    }
}
