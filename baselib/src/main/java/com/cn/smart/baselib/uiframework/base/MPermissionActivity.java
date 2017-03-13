package com.cn.smart.baselib.uiframework.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cn.smart.baselib.uiframework.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * author：leo on 2016/11/17 12:01
 * email： leocheung4ever@gmail.com
 * description: 封装6.0权限管理界面
 * what & why is modified:
 */

public class MPermissionActivity extends AppCompatActivity {

    private static final String TAG = MPermissionActivity.class.getSimpleName();

    private int REQUEST_CODE_PERMISSION = 0x00099;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
            } else {
                permissionFail(REQUEST_CODE_PERMISSION);
                showTipsDialog();
            }
        }
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("提示信息")
                .setContentText("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权")
                .setCancelText("        取消      ")
                .setConfirmText("       确定      ")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        startAppSettings();
                    }
                })
                .show();
    }

    /**
     * 启动设置界面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults 授予权限结果
     * @return 结果
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions 权限数组
     * @return 权限列表
     */
    private List<String> getDeniedPermission(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 获取权限成功
     *
     * @param requestCode 权限请求码
     */
    public void permissionSuccess(int requestCode) {
        Log.d(TAG, "获取权限成功=" + requestCode);
    }

    /**
     * 获取权限失败
     *
     * @param requestCode 权限请求码
     */
    private void permissionFail(int requestCode) {
        Log.d(TAG, "获取权限失败=" + requestCode);
    }

    /**
     * 检测所有权限是否都已授权
     *
     * @param permissions 权限数组
     * @return 结果
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * 请求权限
     *
     * @param permissions 请求的权限数组
     * @param requestCode 请求权限的请求码
     */
    protected void requestPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION);
        } else {
            List<String> needPermissions = getDeniedPermission(permissions);

            assert needPermissions != null;
            ActivityCompat.requestPermissions(
                    this,
                    needPermissions.toArray(new String[needPermissions.size()]),
                    REQUEST_CODE_PERMISSION
            );
        }
    }
}
