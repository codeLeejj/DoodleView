package com.lee.boodlelib.helper;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

public class SuspensionHelper {

    Activity mActivity;
    ISuspension suspension;

    public SuspensionHelper(Activity activity) {
        mActivity = activity;
    }

    WindowManager windowManager;
    View showingView;

    private WindowManager getWindowManager() {
        if (windowManager == null)
            // 获取WindowManager服务
            windowManager = (WindowManager) mActivity.getSystemService(WINDOW_SERVICE);
        return windowManager;
    }

    public void show(ISuspension suspension) {
        this.suspension = suspension;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(mActivity)) {
                create();
            }
        } else {
            create();
        }
    }

    private void create() {
        // 新建悬浮窗控件
        showingView = suspension.createView();
        WindowManager.LayoutParams layoutParams = suspension.getLayoutParams();
        // 将悬浮窗控件添加到WindowManager
        getWindowManager().addView(showingView, layoutParams);
    }

    public void close() {
        if (showingView != null)
            getWindowManager().removeView(showingView);
    }
}
