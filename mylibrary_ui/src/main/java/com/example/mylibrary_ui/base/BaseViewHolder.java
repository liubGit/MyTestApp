package com.example.mylibrary_ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by liub on 2017/12/15.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> viewholder;
    private View view;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        this.viewholder = new SparseArray<>();
    }

    public <T extends View> T get(int viewId) {
        View view = viewholder.get(viewId);
        if (view == null) {
            view = view.findViewById(viewId);
            viewholder.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int id) {
        return get(id);
    }

    public TextView getTextView(int id) {
        return get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }

    public Button getButton(int id) {
        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public RecyclerView getRecyclerView(int id) {
        return get(id);
    }

    public RatingBar getRattingBar(int id) {
        return get(id);
    }

    public CheckBox getCheckBox(int id) {
        return get(id);
    }

    public RadioButton getRadioButton(int id) {
        return get(id);
    }

    public ImageButton getImageButton(int id) {
        return get(id);
    }

    public LinearLayout getLinearLayout(int id) {
        return get(id);
    }

    public RelativeLayout getRelativeLayout(int id) {
        return get(id);
    }

    public GridView getGridView(int id) {
        return get(id);
    }
}
