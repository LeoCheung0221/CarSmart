package com.cn.smart.baselib.router.route;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

/**
 * author：leo on 2016/11/18 16:10
 * email： leocheung4ever@gmail.com
 * description: 全局路由回调接口
 * what & why is modified:
 */

public interface RouteCallback {

    /**
     * 当通过uri打开activity是否需拦截
     *
     * @param uri     要打开的uri
     * @param context 上下文环境
     * @param extras  附加的数据
     * @return 是否拦截
     */
    boolean interceptOpen(Uri uri, Context context, ActivityRouteBundleExtras extras);

    /**
     * 未找到有两种类型的异常
     * 1) 路由规则找不到
     * 2) 跳转页面找不到
     *
     * @param uri 跳转不到的uri
     * @param e   异常
     */
    void notFound(Uri uri, Resources.NotFoundException e);

    /**
     * 用来提示通过路由已打开一个界面的回调方法
     *
     * @param uri     跳转的Uri
     * @param clzName 跳转的活动页面类名
     */
    void onOpenSuccess(Uri uri, String clzName);

    /**
     * 用来提示路由未打开界面的回调方法
     *
     * @param uri 跳转的uri
     * @param e   跳转异常
     */
    void onOpenFailed(Uri uri, Exception e);
}
