package com.example.router;

import android.net.Uri;
import android.text.TextUtils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liub on 2018/1/17.
 */

public class URLUtils {
    URLUtils() {
    }

    public static boolean checkUrl(String url){
        String match="[a-zA-z]+://[^\\s]*";
        Pattern pattern=Pattern.compile(match);
        Matcher matcher=pattern.matcher(url);
        return matcher.matches();
    }
    public static String getScheme(String url) {
        if(TextUtils.isEmpty(url)) {
            return null;
        } else {
            Uri uri = Uri.parse(url);
            return uri.getScheme();
        }
    }

    public static String getHost(String url) {
        if(TextUtils.isEmpty(url)) {
            return null;
        } else {
            Uri uri = Uri.parse(url);
            return uri.getHost();
        }
    }

    public static String getPath(String url) {
        if(TextUtils.isEmpty(url)) {
            return null;
        } else {
            Uri uri = Uri.parse(url);
            return uri.getPath();
        }
    }

    public static String getURLParamString(String url, String paramsKey) {
        if(!TextUtils.isEmpty(url) && !TextUtils.isEmpty(paramsKey)) {
            Uri parse = Uri.parse(url);
            Set queryParameterNames = parse.getQueryParameterNames();
            return queryParameterNames != null && !queryParameterNames.isEmpty()?parse.getQueryParameter(paramsKey):null;
        } else {
            return null;
        }
    }

    public static String removeParamsInUrl(String url) {
        boolean contains = url.contains("?params=");
        String webUrl;
        String[] split;
        if(contains) {
            split = url.split("\\?params=");
            webUrl = split[0];
            if(split[1].contains("&")) {
                webUrl = webUrl + "?";
                String[] paramIndex = split[1].split("&");

                for(int i = 1; i < paramIndex.length; ++i) {
                    webUrl = webUrl + (i == 1?"":"&") + paramIndex[i];
                }
            }
        } else if(url.contains("&params=")) {
            split = url.split("&params=");
            if(split[1].contains("&")) {
                int var6 = split[1].indexOf("&");
                webUrl = split[0] + split[1].substring(var6);
            } else {
                webUrl = split[0];
            }
        } else {
            webUrl = url;
        }

        return webUrl;
    }

}
