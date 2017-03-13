package com.cn.smart.carsmart.ui.account.login;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.cn.smart.baselib.uiframework.bootstrap.AwesomeTextView;
import com.cn.smart.baselib.uiframework.bootstrap.BootstrapButton;
import com.cn.smart.baselib.util.StatusBarUtil;
import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.base.BaseActivity;
import com.cn.smart.carsmart.ui.account.register.RegisterActivity;
import com.cn.smart.carsmart.ui.account.retrieve.FindMeActivity;
import com.cn.smart.carsmart.ui.main.MainActivity;
import com.cn.smart.carsmart.widget.NoUnderlineSpan;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：leo on 2017/2/8 10:24
 * email： leocheung4ever@gmail.com
 * description: Login ui
 * what & why is modified:
 */

public class LoginActivity extends BaseActivity {

    private LoginComponent mLoginComponent;

    @BindView(R.id.btnLogin)
    BootstrapButton btnLogin;
    @BindView(R.id.atvFindMe)
    AwesomeTextView atvFindMe;
    @BindView(R.id.atvSignUp)
    AwesomeTextView atvSignUp;
    @BindView(R.id.atvLook)
    AwesomeTextView atvLook;

    @Override
    protected int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, null);
    }

    @Override
    protected void initInjector() {
        mLoginComponent = DaggerLoginComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .loginModule(new LoginModule(this))
                .build();
        mLoginComponent.inject(this);
    }

    @Override
    protected void initUiAndListener() {
        initView();
//        initData();
    }

    private void initView() {
        SpannableStringBuilder findMeBuilder = new SpannableStringBuilder(atvFindMe.getText().toString());
        SpannableStringBuilder signUpBuilder = new SpannableStringBuilder(atvSignUp.getText().toString());
        SpannableStringBuilder lookBuilder = new SpannableStringBuilder(atvLook.getText().toString());

        ForegroundColorSpan blueSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_2895ca));
        BackgroundColorSpan whiteSpan = new BackgroundColorSpan(ContextCompat.getColor(this, R.color.white));

        NoUnderlineSpan signUpClickSpan = new NoUnderlineSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(new WeakReference<>(LoginActivity.this).get(), RegisterActivity.class));
            }
        };

        findMeBuilder.setSpan(blueSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpBuilder.setSpan(blueSpan, 6, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpBuilder.setSpan(signUpClickSpan, 6, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpBuilder.setSpan(whiteSpan, 6, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lookBuilder.setSpan(blueSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        atvFindMe.setText(findMeBuilder);
        atvSignUp.setText(signUpBuilder);
        atvSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        atvLook.setText(lookBuilder);
    }

    @OnClick({R.id.atvFindMe, R.id.atvLook, R.id.btnLogin})
    void click(View view) {
        switch (view.getId()) {
            case R.id.atvFindMe: //忘记密码
                startActivity(new Intent(LoginActivity.this, FindMeActivity.class));
                break;
            case R.id.btnLogin: //登录
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.atvLook: //随便看看
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
        }
    }
}
