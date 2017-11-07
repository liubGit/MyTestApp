package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by liub on 2017/11/7 .
 */

class BaseListHolder {
    private SparseArray<View> mViews;
    public int mPostion;
    private View mConvertView;

    private BaseListHolder(Context context, ViewGroup parent, int layoutId, int postion) {
        this.mPostion = postion;
        this.mViews = new SparseArray();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    public static BaseListHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int postion) {
        if(convertView == null) {
            return new BaseListHolder(context, parent, layoutId, postion);
        } else {
            BaseListHolder holder = (BaseListHolder)convertView.getTag();
            holder.mPostion = postion;
            return holder;
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = (View)this.mViews.get(viewId);
        if(view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }

        return (T) view;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public BaseListHolder setText(int viewId, String text) {
        TextView tv = (TextView)this.getView(viewId);
        if(tv != null) {
            tv.setText(text);
        }

        return this;
    }

    public BaseListHolder setImageResource(int viewId, int resId) {
        ImageView iv = (ImageView)this.getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    public BaseListHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView iv = (ImageView)this.getView(viewId);
        iv.setImageBitmap(bm);
        return this;
    }

    public TextView getTextView(int id) {
        return (TextView)this.getView(id);
    }

    public Button getButton(int id) {
        return (Button)this.getView(id);
    }

    public ImageView getImageView(int id) {
        return (ImageView)this.getView(id);
    }

    public ImageButton getImageButton(int id) {
        return (ImageButton)this.getView(id);
    }

    public SimpleDraweeView getSimpleDraweeView(int id) {
        return (SimpleDraweeView)this.getView(id);
    }

    public CheckedTextView getCheckedTextView(int id) {
        return (CheckedTextView)this.getView(id);
    }

    public CheckBox getCheckBox(int id) {
        return (CheckBox)this.getView(id);
    }

    public RelativeLayout getRelativeLayout(int id) {
        return (RelativeLayout)this.getView(id);
    }

    public GridView getGridView(int id) {
        return (GridView)this.getView(id);
    }

    public EditableImageView getEditImage(int id) {
        return (EditableImageView)this.getView(id);
    }

    public ThumbnailDraweeView getThumbnailDraweeView(int id) {
        return (ThumbnailDraweeView)this.getView(id);
    }

    public SimpleSquareDraweeView getSimpleSquareDraweeView(int id) {
        return (SimpleSquareDraweeView)this.getView(id);
    }
}
