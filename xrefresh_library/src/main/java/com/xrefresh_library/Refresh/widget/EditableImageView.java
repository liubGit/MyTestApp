package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.facebook.imagepipeline.common.ResizeOptions;
import com.xrefresh_library.R;
import com.xrefresh_library.base.ConstantValue;


/**
 * Created by liub on 2017/11/7 .
 */

class EditableImageView extends LinearLayout {
    private EditableImageView.OnImageDeleteListener onImageDeleteListener;
    private EditableImageView.OnImageClickListener onImageClickListener;
    private ThumbnailDraweeView imageView;
    private ImageButton deleteView;
    private Uri uri;

    public EditableImageView(Context context) {
        super(context);
        this.init(context);
    }

    public EditableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public EditableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context) {
        this.setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.layout_editable_image, this, true);
        this.imageView = (ThumbnailDraweeView)this.findViewById(R.id.image);
        this.imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(EditableImageView.this.onImageClickListener != null) {
                    EditableImageView.this.onImageClickListener.onClick(EditableImageView.this.getTag(), EditableImageView.this.uri);
                }

            }
        });
        this.deleteView = (ImageButton)this.findViewById(R.id.delete);
        this.deleteView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(EditableImageView.this.onImageDeleteListener != null) {
                    EditableImageView.this.onImageDeleteListener.onClick(EditableImageView.this.getTag(), EditableImageView.this.uri);
                }

            }
        });
    }

    public void setResizeOptions(ResizeOptions options) {
        this.imageView.setResizeOptions(options);
    }

    public void setImageURI(Uri uri) {
        if(uri == null) {
            this.deleteImage();
        } else {
            this.uri = uri;
            this.imageView.setImageURI(uri);
            this.deleteView.setVisibility(VISIBLE);
        }
    }

    public void deleteImage() {
        this.uri = null;
        this.imageView.setImageURI("res:///" + R.mipmap.bg_add_img);
        this.deleteView.setVisibility(GONE);
    }

    public Uri getImageURI() {
        return this.uri;
    }

    public void setOnImageClickListener(EditableImageView.OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public void setOnImageDeleteListener(EditableImageView.OnImageDeleteListener onImageDeleteListener) {
        this.onImageDeleteListener = onImageDeleteListener;
    }

    public abstract static class OnImageClickListener {
        private long lastClickTime = 0L;

        public OnImageClickListener() {
        }

        public synchronized void onClick(Object tag, Uri uri) {
            long time = System.currentTimeMillis();
            if(time - this.lastClickTime >= ConstantValue.CLICK_SPACE) {
                this.lastClickTime = time;
                this.noDoubleClick(tag, uri);
            }
        }

        public abstract void noDoubleClick(Object var1, Uri var2);
    }

    public abstract static class OnImageDeleteListener {
        private long lastClickTime = 0L;

        public OnImageDeleteListener() {
        }

        public synchronized void onClick(Object tag, Uri uri) {
            long time = System.currentTimeMillis();
            if(time - this.lastClickTime >= ConstantValue.CLICK_SPACE) {
                this.lastClickTime = time;
                this.onDelete(tag, uri);
            }
        }

        public abstract void onDelete(Object var1, Uri var2);
    }
}
