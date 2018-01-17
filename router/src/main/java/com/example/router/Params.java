package com.example.router;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by liub on 2018/1/17.
 */

public class Params {
    private String paramJsonString;
    private HashMap<String, Object> hashMap;
    private Gson gson;
    private boolean paramChange;

    public Params(String paramJsonString) {
        this.paramJsonString = paramJsonString;
        this.gson = new Gson();
        this.parseParams(paramJsonString);
    }

    public void put(String key, Object value) {
        if (value == null) {
            Log.e("param:", key + "对应的值为null");
        } else {
            hashMap.put(key, value);
            paramChange = true;
        }
    }

    public String get(String key) {
        return TextUtils.isEmpty(key) ? null : (hashMap != null ? hashMap.get(key).toString() : null);
    }

    public String getParamJson(){
        return paramJsonString;
    }

    public String toJsonString() {
        return this.gson.toJson(this.hashMap);
    }

    public String toEncodeString() {
        return URLEncoder.encode(this.gson.toJson(this.hashMap));
    }

    public Bundle toBundle() {
        Bundle params = new Bundle();
        if(this.hashMap != null) {
            Iterator var2 = this.hashMap.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry entry = (Map.Entry)var2.next();
                params.putString((String)entry.getKey(), entry.getValue().toString());
            }
        }

        return params;
    }

    private void parseParams(String paramJsonString) {
        hashMap = gson.fromJson(paramJsonString, HashMap.class);
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
    }

    public boolean isParamChange() {
        return paramChange;
    }

    public void setParamChange(boolean paramChange) {
        this.paramChange = paramChange;
    }
}
