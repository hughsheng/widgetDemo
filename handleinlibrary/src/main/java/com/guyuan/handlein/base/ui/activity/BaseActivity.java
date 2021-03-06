package com.guyuan.handlein.base.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import com.example.mvvmlibrary.base.activity.BaseDataBindingActivity;
import com.guyuan.handlein.base.R;
import com.guyuan.handlein.base.util.AlertDialogUtils;
import com.guyuan.handlein.base.util.SnackBarUtil;
import com.guyuan.handlein.base.util.ToastUtil;
import com.example.mvvmlibrary.base.data.BaseViewModel;
import com.example.mvvmlibrary.util.ActivityUtils;

/**
 * created by com.tl
 * created at 2020/8/18
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends
        BaseDataBindingActivity<V, VM> {

    private static final int REQUEST_CODE_PERMISSION = 9527;
    private AlertDialog loadingDialog;
    protected FragmentManager fragmentManager;
    private AlertDialog alertDialog;

    @Override
    protected void initData(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        ActivityUtils.addActivity(this.getClass().getSimpleName(), this);
        init(savedInstanceState);
    }

    protected View getRootView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }


    protected abstract void init(Bundle savedInstanceState);


    /**
     * app字体不跟随系统设置改变而改变
     *
     * @param newConfig config
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
        {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 去掉手机状态栏
     */
    protected void setFullScreen() {
        getRootView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    /**
     * 某些功能需要确保先已经获得用户授权才可以实现，可以使用{@link BaseActivity#checkPermissions(String...)}
     * 然后才在这个回调中启动这些功能。
     */
    public void onAllPermissionsGranted() {

    }

    public void checkPermissions(String... permissions) {
        ArrayList<String> request = new ArrayList<>();
        for (String per : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int result = checkSelfPermission(per);
                if (PackageManager.PERMISSION_GRANTED != result) {
                    request.add(per);
                }
            }
        }
        if (!request.isEmpty()) {
            String[] list = new String[request.size()];
            for (int i = 0; i < request.size(); i++) {
                list[i] = request.get(i);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(list, REQUEST_CODE_PERMISSION);
            }
        } else {
            onAllPermissionsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = true;
        final String[] rational = new String[grantResults.length];
        int index = 0;
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissions[i])) {
                            rational[index++] = permissions[i];
                        } else {
                            showToastTip("权限被限制，请到APP设置中授权。");
                            onBackPressed();
                            return;
                        }
                    }
                }
            }
        }
        if (isGranted) {
            onAllPermissionsGranted();
        } else {
            if (index > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.request_permission));
                StringBuilder sb = new StringBuilder();
                sb.append("为了使用此界面的功能，需要获得以下授权：\n");
                for (String temp : rational) {
                    sb.append(temp).append("\n");
                }
                sb.append("点击确定重新授权，点击取消退出此界面。");
                builder.setMessage(sb.toString());
                builder.setPositiveButton(getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(rational, REQUEST_CODE_PERMISSION);
                                }
                            }
                        });
                builder.setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                                onBackPressed();
                            }
                        });
                builder.setCancelable(false);
                alertDialog = builder.create();
                alertDialog.show();
            } else {
                showToastTip("权限被限制，请到APP设置中授权。");
                onBackPressed();
            }
        }
    }

    //当键盘弹出来的时候，点击屏幕空白处，将受收起键盘 by leo
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isToHide(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isToHide(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText || v instanceof AppCompatEditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    public void showToastTip(int resId) {
        ToastUtil.showToastTip(this, resId);
    }

    @Override
    public void showToastTip(String message) {
        ToastUtil.showToastTip(this, message);
    }

    public void showToastTip(String tip, int gravity) {
        ToastUtil.showToastTip(this, tip, gravity);
    }


    public void showSnackBarTip(String message) {
        SnackBarUtil.showSnackBarTip(getRootView(), message);
    }

    public void showSnackBarTip(int resId) {
        SnackBarUtil.showSnackBarTip(getRootView(), resId);
    }

    public void showLoading(FragmentManager manager) {
        hideLoading();
        loadingDialog = AlertDialogUtils.showLoading(this, null, null);
    }

    @Override
    public void showLoadingWithTip(FragmentManager manager, String status) {
        hideLoading();
        loadingDialog = AlertDialogUtils.showLoading(this, null, status);
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
