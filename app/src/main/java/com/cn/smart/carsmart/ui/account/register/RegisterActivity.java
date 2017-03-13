package com.cn.smart.carsmart.ui.account.register;

import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cn.smart.baselib.uiframework.bootstrap.AwesomeTextView;
import com.cn.smart.baselib.uiframework.bootstrap.BootstrapButton;
import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.base.BaseSwipeBackActivity;
import com.cn.smart.carsmart.widget.NoUnderlineSpan;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：leo on 2017/3/7 14:22
 * email： leocheung4ever@gmail.com
 * description: 注册 UI
 * what & why is modified:
 */

public class RegisterActivity extends BaseSwipeBackActivity {

    @BindView(R.id.btnRegister)
    BootstrapButton btnRegister;
    @BindView(R.id.btnVerify)
    BootstrapButton btnVerify;
    @BindView(R.id.atvRegisterNote)
    AwesomeTextView atvRegisterNote;

    @Override
    protected void requestPermission(String[] permissions, int requestCode) {
        super.requestPermission(permissions, requestCode);
    }

    private RegisterComponent mRegisterComponent;

    @Override
    protected int initContentView() {
        return R.layout.activity_register;
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
    protected void initInjector() {
        mRegisterComponent = DaggerRegisterComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .registerModule(new RegisterModule(this))
                .build();
        mRegisterComponent.inject(this);
    }

    @Override
    protected void initUiAndListener() {
        initView();
        verifyInput();
    }

    @OnClick({R.id.btnRegister, R.id.btnVerify})
    void click(View view) {
        switch (view.getId()) {
            case R.id.btnVerify:

                break;
            case R.id.btnRegister:

                break;
        }
    }

    private void verifyInput() {

    }

    private void initView() {
        SpannableStringBuilder regTipBuilder = new SpannableStringBuilder(atvRegisterNote.getText().toString());
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_2895ca));
        regTipBuilder.setSpan(blueSpan, 12, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        BackgroundColorSpan whiteSpan = new BackgroundColorSpan(ContextCompat.getColor(this, R.color.register_bg_color));
        regTipBuilder.setSpan(whiteSpan, 12, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(28);
        regTipBuilder.setSpan(sizeSpan, 12, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        NoUnderlineSpan clickSpan = new NoUnderlineSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(new WeakReference<>(RegisterActivity.this).get(), "请不要点我", Toast.LENGTH_SHORT).show();
            }
        };
        regTipBuilder.setSpan(clickSpan, 12, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        atvRegisterNote.setText(regTipBuilder);
        atvRegisterNote.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
