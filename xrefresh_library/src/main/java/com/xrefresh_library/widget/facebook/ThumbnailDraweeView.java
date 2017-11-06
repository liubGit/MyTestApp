package com.xrefresh_library.widget.facebook;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilderSupplier;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * 缩略图DraweeView（显示对应大小的缩略图）
 * Created by zeda on 15/12/17.
 */
public class ThumbnailDraweeView extends SimpleDraweeView {
    private PipelineDraweeControllerBuilder builder;
    private ResizeOptions options;

    public ThumbnailDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        builder = new PipelineDraweeControllerBuilderSupplier(getContext()).get();
    }

    public void setResizeOptions(ResizeOptions options) {
        this.options = options;
    }

    @Override
    public void setImageURI(Uri uri) {
        if (options != null && uri != null) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(options).build();
            builder.setImageRequest(request);
            setController(builder.build());
        } else
            super.setImageURI(uri);
    }

    public void setImageFilePath(String filePath) {
        if (options != null && !TextUtils.isEmpty(filePath)) {
            Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(filePath).build();
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(options).build();
            builder.setImageRequest(request);
            setController(builder.build());
        } else if (!TextUtils.isEmpty(filePath))
            super.setImageURI(new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(filePath).build());
        else
            super.setImageURI(null);
    }
}
