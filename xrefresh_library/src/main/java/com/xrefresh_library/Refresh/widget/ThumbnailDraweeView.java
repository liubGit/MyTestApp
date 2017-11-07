package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilderSupplier;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * 缩略图DraweeView（显示对应大小的缩略图）
 * Created by zeda on 15/12/17.
 */
public class ThumbnailDraweeView  extends SimpleDraweeView {
    private PipelineDraweeControllerBuilder builder;
    private ResizeOptions options;
    private ControllerListener<? super ImageInfo> controllerListener;

    public ThumbnailDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!this.isInEditMode()) {
            this.builder = (new PipelineDraweeControllerBuilderSupplier(this.getContext())).get();
        }
    }

    public void setResizeOptions(ResizeOptions options) {
        this.options = options;
    }

    public void setImageURI(Uri uri) {
        if(this.options == null) {
            int request = this.getLayoutParams().width;
            if(request == -1) {
                request = this.getResources().getDisplayMetrics().widthPixels / 2;
            }

            int height = this.getLayoutParams().height;
            if(height == -1) {
                height = this.getResources().getDisplayMetrics().heightPixels / 3;
            }

            this.options = new ResizeOptions(request, height);
        }

        if(uri != null) {
            ImageRequest request1 = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(this.options).build();
            this.builder.setImageRequest(request1);
            this.builder.setOldController(this.getController());
            this.builder.setControllerListener(this.controllerListener);
            this.setController(this.builder.build());
        } else {
            super.setImageURI(uri);
        }

    }

    public void setControllerListener(ControllerListener<? super ImageInfo> controllerListener) {
        this.controllerListener = controllerListener;
    }

    public void setImageFilePath(String filePath) {
        if(this.options != null && !TextUtils.isEmpty(filePath)) {
            Uri uri = (new Uri.Builder()).scheme("file").path(filePath).build();
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(this.options).build();
            this.builder.setImageRequest(request);
            this.setController(this.builder.build());
        } else if(!TextUtils.isEmpty(filePath)) {
            super.setImageURI((new Uri.Builder()).scheme("file").path(filePath).build());
        } else {
            super.setImageURI((String)null);
        }

    }
}
