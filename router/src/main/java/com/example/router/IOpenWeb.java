package com.example.router;

/**
 * Created by liub on 2018/1/17.
 */

class IOpenWeb {
    public IOpenWeb() {
    }

    /** @deprecated */
    @Deprecated
    public void openWeb(String url, int style, boolean hideButton) {
    }

    public void openWeb(RequestStartViewInfo startViewInfo) {
        this.openWeb(startViewInfo.getUrl(), startViewInfo.getStyle(), startViewInfo.isHideCloseButton());
    }
}
