package com.xrefresh_library.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.xrefresh_library.widget.SquareCheckedText;
import com.xrefresh_library.widget.facebook.ThumbnailDraweeView;

/**
 * Created by luokj on 2015/11/18.
 */
public class BaseRecViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> viewHolder;
    private View view;

    public BaseRecViewHolder(View view) {
        super(view);
        this.view = view;
        viewHolder = new SparseArray<>();
    }

    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public View getView(int id) {
        return get(id);
    }

    public TextView getTextView(int id) {

        return get(id);
    }

    public Button getButton(int id) {

        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }

    public SimpleDraweeView getSimpleDraweeView(int id) {
        return get(id);
    }

    public ThumbnailDraweeView getThumbnailDraweeView(int id) {
        return get(id);
    }

    public ThumbnailDraweeView getThumbnailDraweeView(int id, ResizeOptions options) {
        if (options == null)
            return getThumbnailDraweeView(id);

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            ((ThumbnailDraweeView) childView).setResizeOptions(options);
            viewHolder.put(id, childView);
        }
        return (ThumbnailDraweeView) childView;
    }

    public RecyclerView getRecyclerView(int id) {
        return get(id);
    }

    public RatingBar getRattingBar(int id) {
        return get(id);
    }

    public RelativeLayout getRelativeLayout(int id) {
        return get(id);
    }

    public CheckBox getCheckBox(int id) {
        return get(id);
    }

    public ImageButton getImageButton(int id) {
        return get(id);
    }

    public SquareCheckedText getSquareCheckedText(int id){
        return get(id);
    }

    public LinearLayout getLinearLayout(int id){
        return get(id);
    }

    public GridView getGridView(int id) {
        return get(id);
    }
}
