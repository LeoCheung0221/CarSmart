package com.cn.smart.carsmart.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.cn.smart.baselib.logcat.ZLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * author：leo on 2017/2/20 12:27
 * email： leocheung4ever@gmail.com
 * description: 持久化cookie仓库
 * what & why is modified:
 */

public class PersistentCookieStore {

    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "Cookies_Prefs";

    private final SharedPreferences cookiePrefs;
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;

    public PersistentCookieStore(Context context) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
        cookies = new HashMap<>();

        //cookie持久化到内存中
        Map<String, ?> prefsMap = cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
            for (String name : cookieNames) {
                String encodedCookie = cookiePrefs.getString(name, null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null) {
                        if (!cookies.containsKey(entry.getKey())) {
                            cookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                        }
                        cookies.get(entry.getKey()).put(name, decodedCookie);
                    }
                }
            }
        }
    }

    /**
     * 将字符串反序列化成cookies
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableOkHttpCookies) objectInputStream.readObject()).getCookies();
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
        }

        return cookie;
    }

    /**
     * cookies序列化成字符串
     */
    protected String encodeCookie(SerializableOkHttpCookies cookie) {
        if (cookie == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e);
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * 二进制数组转换为十六进制字符串
     */
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 16进制字符串转换为字节数组
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 获取cookie token
     */
    protected String getCookieToken(Cookie cookie) {
        ZLog.e("cookie token: " + cookie.name() + "@" + cookie.domain());
        return cookie.name() + "@" + cookie.domain();
    }

    /**
     * 添加url至cookie
     */
    public void add(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);

        //将cookies缓存到内存中 如果缓存过期 重置此cookie
        ZLog.e("debug: expire==>" + !cookie.persistent());
        if (!cookie.persistent()) {
            if (!cookies.containsKey(url.host())) {
                cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
            }
            cookies.get(url.host()).put(name, cookie);
        } else {
            if (cookies.containsKey(url.host())) {
                cookies.get(url.host()).remove(name);
            }
        }

        //cookies持久化本地
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString(url.host(), TextUtils.join(",", cookies.get(url.host()).keySet()));
        prefsWriter.putString(name, encodeCookie(new SerializableOkHttpCookies(cookie)));
        prefsWriter.apply();
    }

    public List<Cookie> get(HttpUrl url) {
        ArrayList<Cookie> ret = new ArrayList<>();
        if (cookies.containsKey(url.host())) {
            ret.addAll(cookies.get(url.host()).values());
        }
        return ret;
    }

    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        cookies.clear();
        return true;
    }

    public boolean remove(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);

        if (cookies.containsKey(url.host()) && cookies.get(url.host()).containsKey(name)) {
            cookies.get(url.host()).remove(name);

            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
            if (cookiePrefs.contains(name)) {
                prefsWriter.remove(name);
            }
            prefsWriter.putString(url.host(), TextUtils.join(",", cookies.get(url.host()).keySet()));
            prefsWriter.apply();

            return true;
        } else {
            return false;
        }
    }

    public List<Cookie> getCookies() {
        ArrayList<Cookie> ret = new ArrayList<>();
        for (String key : cookies.keySet())
            ret.addAll(cookies.get(key).values());
        return ret;
    }

}
