package com.cn.smart.carsmart.components.storage;

import android.content.Context;

import com.cn.smart.baselib.util.SettingPrefUtil;
import com.cn.smart.carsmart.model.local.User;

/**
 * author：leo on 2017/2/7 11:15
 * email： leocheung4ever@gmail.com
 * description: 用户相关属性存储对象
 * what & why is modified:
 */

public class UserStorage {

    private Context mContext;

    public UserStorage(Context context) {
        this.mContext = context;
    }

    private User user;
    private String cookie;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCookie() {
        if (isLogin()) {
            return user.getCookie();
        }
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getToken() {
        if (isLogin()) {
            return user.getToken();
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        if (isLogin()) {
            return user.getUid();
        }
        return "";
    }

    public boolean isLogin() {
        return user != null && SettingPrefUtil.getLoginUid(mContext).equals(user.getUid());
    }
}
