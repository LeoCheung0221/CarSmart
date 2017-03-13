package com.cn.smart.baselib.uiframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.cn.smart.baselib.R;

/**
 * author：leo on 2016/11/17 23:26
 * email： leocheung4ever@gmail.com
 * description: 进度读取处理类
 * what & why is modified:
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Dialog mDialog;
    private Context mContext;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener cancelListener, boolean cancelable) {
        super();
        this.mContext = context;
        this.mProgressCancelListener = cancelListener;
        this.cancelable = cancelable;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

    private void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private void initProgressDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.progress_dialog);
            mDialog.setCancelable(cancelable);
            mDialog.setContentView(R.layout.progress_dialog_loading);
            mDialog.setCancelable(true);
            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) mDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("加载中，请稍后...");
            if (cancelable) {
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }

            if (!mDialog.isShowing())
                mDialog.show();
        }
    }
}
