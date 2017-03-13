package com.cn.smart.carsmart.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cn.smart.baselib.util.ResourceUtil;
import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.base.fragment.ProgressBarCircularIndeterminate;
import com.cn.smart.carsmart.base.fragment.ProgressFragment;
import com.cn.smart.carsmart.injector.HasComponent;

/**
 * author：leo on 2016/11/25 15:44
 * email： leocheung4ever@gmail.com
 * description: Fragment基类
 * what & why is modified:
 */

public abstract class BaseFragment extends ProgressFragment {

    private TextView tvEmpty, tvError, tvLoading;
    private Button btnReload;

    /**
     * 初始化注入器
     */
    protected abstract void initInjector();

    /**
     * 获取Activity传递的值
     */
    protected abstract void getBundle(Bundle bundle);

    /**
     * 初始化控件
     */
    protected abstract void initUI(View view);

    /**
     * 在监听器之前把数据准备好
     */
    protected abstract void initData();

    /**
     * 内容视图值
     */
    protected abstract int initContentView();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInjector();
        getBundle(getArguments());
        initUI(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(initContentView(), null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateContentEmptyView(LayoutInflater inflater) {
        View empty = inflater.inflate(R.layout.view_empty_layout, null);
        tvEmpty = (TextView) empty.findViewById(R.id.tvEmpty);
        btnReload = (Button) empty.findViewById(R.id.btnReload);
        empty.findViewById(R.id.btnReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReloadClicked();
            }
        });
        return empty;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateContentErrorView(LayoutInflater inflater) {
        View error = inflater.inflate(R.layout.view_error_layout, null);
        tvError = (TextView) error.findViewById(R.id.tvError);
        error.findViewById(R.id.btnReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReloadClicked();
            }
        });
        return error;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateProgressView(LayoutInflater inflater) {
        View loading = inflater.inflate(R.layout.view_loading_layout, null);
        tvLoading = (TextView) loading.findViewById(R.id.tvLoading);
        ProgressBarCircularIndeterminate progressBar =
                (ProgressBarCircularIndeterminate) loading.findViewById(R.id.progress_view);
        progressBar.setBackgroundColor(ResourceUtil.getThemeColor(getActivity()));
        return loading;
    }

    //Override this method to reload
    public void onReloadClicked() {
    }

    public void setEmptyText(String text) {
        this.tvEmpty.setText(text);
    }

    public void setEmptyText(int textResId) {
        this.tvEmpty.setText(getString(textResId));
    }

    public void setErrorText(String text) {
        this.tvError.setText(text);
    }

    public void setErrorText(int textResId) {
        this.tvError.setText(getString(textResId));
    }

    public void setLoadingText(String text) {
        this.tvLoading.setText(text);
    }

    public void setLoadingText(int textResId) {
        this.tvLoading.setText(getString(textResId));
    }

    public void setEmptyButtonVisible(int visible) {
        btnReload.setVisibility(visible);
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
