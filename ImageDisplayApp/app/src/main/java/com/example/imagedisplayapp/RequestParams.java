package com.example.imagedisplayapp;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @File: MainActivity.java
 * @Author: Jatin Gupte, Dheeraj Mirashi
 * @Group: 50
 **/
public class RequestParams {
    private Map<String, String> params;

    public RequestParams() {
        params = new HashMap<>();
    }

    public RequestParams addParams(String key, String value) {
        try {
            params.put(key, URLEncoder.encode(value, "UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();

        for (String key : params.keySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key + "=" + params.get(key));
        }

        return sb.toString();
    }

    public String getEncodedUrl(String url) {
        return url + "?" + getEncodedParams();
    }

    public void encodeRequestBody(HttpURLConnection connection) throws IOException {
        connection.setDoOutput(true);
        OutputStreamWriter w = new OutputStreamWriter(connection.getOutputStream());
        w.write(getEncodedParams());
        w.flush();

    }
}
