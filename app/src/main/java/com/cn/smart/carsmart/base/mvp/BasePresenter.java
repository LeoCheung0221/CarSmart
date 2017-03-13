package com.cn.smart.carsmart.base.mvp;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;

import com.cn.smart.baselib.uiframework.dialog.ProgressCancelListener;
import com.cn.smart.baselib.uiframework.dialog.ProgressDialogCustomListener;
import com.cn.smart.baselib.uiframework.dialog.ProgressDialogHandler;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * author：leo on 2017/2/8 11:37
 * email： leocheung4ever@gmail.com
 * description: Presenter基类
 * 通过传递BaseView泛型参数拿到View对象,进行界面处理
 * what & why is modified:
 */

public class BasePresenter<V extends IView> implements ProgressCancelListener {

    protected V mView;
    private WeakReference actReference;

    protected ProgressDialogHandler mDialogHandler; //default
    protected ProgressDialogCustomListener mCustomListener; //custom

    private String mToken;
    private CompositeSubscription mCompositeSubscription; //未解除订阅关系的被订阅者组
    private SparseArray<Subscription> mSubs = new SparseArray<>(); //被订阅者则 -> 发送事件流对象

    /**
     * 依附View
     *
     * @param view 注入依附View
     */
    public void attachView(IView view) {
        actReference = new WeakReference(view);
        this.mView = (V) actReference.get();
        if (mView instanceof ProgressDialogCustomListener)
            mCustomListener = (ProgressDialogCustomListener) mView;
        else
            mDialogHandler = new ProgressDialogHandler(mView.getContextForPresenter(), this, true);
    }

    /**
     * 分离View
     */
    public void detachView() {
        if (actReference != null) {
            actReference.clear();
            actReference = null;
        }
        this.mView = null;
        onUnSubscribe(null);
    }

    public V getIView() {
        return (V) actReference.get();
    }

    /**
     * 删除订阅关系 以防OOM
     *
     * @param tag 解除标识 for 解除标识的被订阅者
     */
    protected void onUnSubscribe(String tag) {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        } else {
            int key = tag.hashCode();
            Subscription tempSub = mSubs.get(key);
            mCompositeSubscription.remove(tempSub);
            mSubs.remove(key);
        }
    }

    /**
     * 开始实现业务逻辑
     *
     * @param o   被观察对象
     * @param <T> 泛型参数
     */
    public <T> void start(Observable<T> o, final String tag) {
        addSubscription(null, o, new BaseSubscriber<T>() {
            @Override
            public void onStart() {
                showProgressDialog();
            }

            @Override
            public void onCompleted() {
                mView.onComplete(tag);
                dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.onDataFailed(tag, msg);
                dismissProgressDialog();
            }

            @Override
            public void onNext(T model) {
                mView.onDataSuccess(tag, model);
            }
        });
    }

    /**
     * 添加订阅 - 发送数据流、事件流 and so on
     *
     * @param tag        添加订阅标识
     * @param observable 被观察者对象
     * @param subscriber 订阅者对象
     * @param <T>        泛型参数
     */
    protected <T> void addSubscription(String tag, Observable<T> observable, Subscriber<T> subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        Subscription subscription = observable
                .subscribeOn(Schedulers.io()) //在io线程中订阅数据流
                .observeOn(AndroidSchedulers.mainThread()) //在主线程中观察订阅对象
                .subscribe(subscriber); //订阅者订阅
        mCompositeSubscription.add(subscription);

        if (!TextUtils.isEmpty(tag)) {
            mSubs.put(tag.hashCode(), subscription);
        }
    }

    /**
     * 添加正常网络请求订阅
     */
    protected <T> void addSubscriptionNormal(String tag, Observable<T> observable, Subscriber<T> subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        mCompositeSubscription.add(subscriber);

        if (!TextUtils.isEmpty(tag)) {
            mSubs.put(tag.hashCode(), subscription);
        }
    }

    /**
     * 显示加载
     */
    private void showProgressDialog() {
        if (null != mCustomListener) {
            mCustomListener.needProgressShow();
            return;
        }
        if (mDialogHandler != null) {
            mDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    /**
     * 取消加载
     */
    private void dismissProgressDialog() {
        if (null != mCustomListener) {
            mCustomListener.needProgressDismiss();
            return;
        }
        if (mDialogHandler != null) {
            mDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mDialogHandler = null;
        }
    }

    @Override
    public void onCancelProgress() {
        onUnSubscribe(null);
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }
}
