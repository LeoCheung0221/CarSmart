package com.cn.smart.carsmart.config;

import android.content.Context;

import com.cn.smart.carsmart.Core;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * author：leo on 2017/2/20 11:53
 * email： leocheung4ever@gmail.com
 * description: cookie manager class
 * what & why is modified:
 */

public class AppCookieManager implements CookieJar {

    private static final PersistentCookieStore cookieStore = new PersistentCookieStore(Core.getInstance());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

    /**
     * 清除所有cookie
     */
    public static void clearAllCookie(Context context) {
        if (getCookieValue(context)) {
            cookieStore.removeAll();
        }
    }

    private static boolean getCookieValue(Context context) {
        return new PersistentCookieStore(context).getCookies().size() > 0;
    }
}
