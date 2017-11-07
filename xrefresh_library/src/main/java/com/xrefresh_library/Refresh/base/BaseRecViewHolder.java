package com.xrefresh_library.Refresh.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
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
import com.xrefresh_library.Refresh.widget.ThumbnailDraweeView;
import com.xrefresh_library.widget.SquareCheckedText;

/**
 * Created by luokj on 2015/11/18.
 */
public class BaseRecViewHolder extends ViewHolder {
    private SparseArray<View> viewHolder;
    private View view;

    public BaseRecViewHolder(View view) {
        super(view);
        this.view = view;
        this.viewHolder = new SparseArray();
    }

    public <T extends View> T get(int id) {
        View childView = (View)this.viewHolder.get(id);
        if(childView == null) {
            childView = this.view.findViewById(id);
            this.viewHolder.put(id, childView);
        }

        return (T) childView;
    }

    public View getConvertView() {
        return this.view;
    }

    public View getView(int id) {
        return this.get(id);
    }

    public TextView getTextView(int id) {
        return (TextView)this.get(id);
    }

    public Button getButton(int id) {
        return (Button)this.get(id);
    }

    public ImageView getImageView(int id) {
        return (ImageView)this.get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        this.getTextView(id).setText(charSequence);
    }

    public SimpleDraweeView getSimpleDraweeView(int id) {
        return (SimpleDraweeView)this.get(id);
    }

    public ThumbnailDraweeView getThumbnailDraweeView(int id) {
        return (ThumbnailDraweeView)this.get(id);
    }

    public ThumbnailDraweeView getThumbnailDraweeView(int id, ResizeOptions options) {
        if(options == null) {
            return this.getThumbnailDraweeView(id);
        } else {
            View childView = (View)this.viewHolder.get(id);
            if(childView == null) {
                childView = this.view.findViewById(id);
                ((ThumbnailDraweeView)childView).setResizeOptions(options);
                this.viewHolder.put(id, childView);
            }

            return (ThumbnailDraweeView)childView;
        }
    }

    public RecyclerView getRecyclerView(int id) {
        return (RecyclerView)this.get(id);
    }

    public RatingBar getRattingBar(int id) {
        return (RatingBar)this.get(id);
    }

    public RelativeLayout getRelativeLayout(int id) {
        return (RelativeLayout)this.get(id);
    }

    public CheckBox getCheckBox(int id) {
        return (CheckBox)this.get(id);
    }

    public ImageButton getImageButton(int id) {
        return (ImageButton)this.get(id);
    }

    public SquareCheckedText getSquareCheckedText(int id) {
        return (SquareCheckedText)this.get(id);
    }

    public LinearLayout getLinearLayout(int id) {
        return (LinearLayout)this.get(id);
    }

    public GridView getGridView(int id) {
        return (GridView)this.get(id);
    }

//    public HalfWidthDraweeView getHalfWidthDraweeView(int id) {
//        return (HalfWidthDraweeView)this.get(id);
//    }
//
//    public InputsView getInputsView(int id) {
//        return (InputsView)this.get(id);
//    }
}
