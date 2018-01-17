package com.example.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liub on 2018/1/17.
 */

public class Packet {

    public static final int URL_TYPE_INNER = 0;
    public static final int URL_TYPE_WEB = 1;
    public static final int URL_TYPE_INNER_WEB = 4;
    public static final int URL_TYPE_OUTSIDE = 2;
    public static final int URL_TYPE_ERROR = 3;
    public static String SCHEME = "testApp";
    private Context context;
    private int URL_TYPE;
    private String url;
    private String scheme;
    private String path;
    private String host;
    private Params params;
    private Bundle intentParam = new Bundle();
    private List<Interceptor> interceptors;
    private IOpenWeb webAction;

    public Packet(Context context, String url) {
        this.context = context;
        this.url = url;
        this.interceptors = new ArrayList<>();
        this.parseUrl(url);
    }

    public Packet addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public int getUrlType() {
        return this.URL_TYPE;
    }

    public Packet setOpenWebAction(IOpenWeb webAction) {
        webAction = webAction;
        return this;
    }

    public String getWebUrl() {
        return URL_TYPE == URL_TYPE_WEB?url:(URL_TYPE == URL_TYPE_INNER_WEB?params.get("URL"):null);
    }

    public Packet put(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public Intent getForwardIntent() {
        Class activityClazz = UrlConfigMap.get(this.path);
        if (activityClazz != null) {
            return (new Intent(this.context, activityClazz)).putExtras(this.params.toBundle()).putExtras(this.intentParam);
        } else {
            if (Router.DEBUG) {
                Log.i("router", "path not config:url:" + this.url + ", path:" + this.path);
            }

            return null;
        }
    }

    public Packet putInt(String key, int value) {
        intentParam.putInt(key, value);
        return this;
    }

    public Packet putFloat(String key, float value) {
        intentParam.putFloat(key, value);
        return this;
    }

    public Packet putString(String key, String value) {
        intentParam.putString(key, value);
        return this;
    }

    public Packet putBoolean(String key, boolean value) {
        intentParam.putBoolean(key, value);
        return this;
    }

    public void go() {
        ensureParamChange();
        switch (this.URL_TYPE) {
            case URL_TYPE_INNER:
                if (Router.DEBUG) {
                    Log.i("router", "url: " + this.url);
                }

                this.goInner();
                break;

            case URL_TYPE_WEB:
            case URL_TYPE_INNER_WEB:
                String webUrl = getWebUrl();
                if(TextUtils.isEmpty(webUrl)) {
                    return;
                }

                webUrl = URLUtils.removeParamsInUrl(webUrl);
                String paramJson = this.params.getParamJson();
                RequestStartViewInfo startInfo;
                if(TextUtils.isEmpty(paramJson)) {
                    startInfo = new RequestStartViewInfo();
                    startInfo.setUrl(webUrl);
                } else {
                    startInfo = (RequestStartViewInfo)(new Gson()).fromJson(paramJson, RequestStartViewInfo.class);
                    startInfo.setUrl(webUrl);
                }

                if(webAction != null) {
                    webAction.openWeb(startInfo);
                } else if(Router.defaultClassUrl != null) {
                    if(URL_TYPE == 1) {
                        params.put("URL", url);
                    }

                    Bundle extras = params.toBundle();
                    extras.putParcelable("infos", startInfo);
                    extras.putAll(intentParam);
                    context.startActivity((new Intent(context, Router.defaultClassUrl)).putExtras(extras).addFlags(268435456));
                } else if(Router.DEBUG) {
                    Log.e("router", "no define action for web url: " + this.url);
                }
                break;
            case URL_TYPE_OUTSIDE:
                this.context.startActivity((new Intent("android.intent.action.VIEW")).setData(Uri.parse(this.url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case URL_TYPE_ERROR:
                if(Router.DEBUG) {
                    Log.e("router", "url error:" + url);
                }

        }
    }

    private void goInner() {
        Iterator activityClazz = interceptors.iterator();
        Interceptor extras;
        do {
            if (!activityClazz.hasNext()) {
                Class activityClazz1 = UrlConfigMap.get(path);
                if (activityClazz1 != null) {
                    Bundle extras1 = params.toBundle();
                    extras1.putAll(intentParam);
                    Intent intent = (new Intent(context, activityClazz1)).putExtras(extras1);
                    if (!(context instanceof Activity)) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(intent);
                } else if (Router.DEBUG) {
                    Log.i("router", "path not config:url:" + url + ", path:" + path);
                }

                return;
            }

            extras = (Interceptor) activityClazz.next();
        } while (!extras.intercept(path));

        if (Router.DEBUG) {
            Log.d("router", "interceptor中断跳转");
        }

    }

    private void ensureParamChange() {
        if (params.isParamChange()) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            String[] paramPath = url.split("params=");
            if (paramPath.length == 0) {
                return;
            }
            if (paramPath.length == 1) {
                url = url + "params=" + params.toEncodeString();
                return;
            }
            if (paramPath.length == 2) {
                String paramString = paramPath[1];
                String[] paramList = paramString.split("&");
                if (paramList.length == 1) {
                    url = paramPath[0] + "params=" + params.toEncodeString();
                    return;
                }
                if (paramList.length > 1) {
                    url = paramPath[0] + "params=" + params.toEncodeString();
                    for (int i = 1; i < paramList.length; ++i) {
                        url = url + "&" + paramList[i];
                    }
                }
            }
        }
    }

    private void parseUrl(String url) {
        if (!URLUtils.checkUrl(url)) {
            scheme = url;
            path = url;
            host = url;
            URL_TYPE = URL_TYPE_ERROR;
            params = new Params(null);
        } else {
            scheme = URLUtils.getScheme(url);
            host = URLUtils.getHost(url);
            path = URLUtils.getPath(url);
            URL_TYPE = getUrlType(this.scheme, this.host, this.path);
            params = new Params(URLUtils.getURLParamString(url, "params"));
        }
    }

    int getUrlType(String scheme, String host, String path) {
        return TextUtils.isEmpty(scheme) ? URL_TYPE_ERROR : (
                !scheme.equals("http") && !scheme.equals("https")
                        ? (scheme.equals(SCHEME) ? (
                        !host.equals("testApp.cn") ? URL_TYPE_ERROR : (
                                path.equals("/Browser/Web") ? URL_TYPE_INNER_WEB : URL_TYPE_INNER
                        )) : URL_TYPE_OUTSIDE) : URL_TYPE_WEB);
    }
}
