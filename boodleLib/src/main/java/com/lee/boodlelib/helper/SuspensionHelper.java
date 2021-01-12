package com.lee.boodlelib.helper;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 *
 */
public class SuspensionHelper {

    Activity mActivity;
    ISuspension suspension;

    public SuspensionHelper(Activity activity) {
        mActivity = activity;
    }

    private WindowManager getWindowManager() {
        if (windowManager == null)
            // 获取WindowManager服务
            windowManager = (WindowManager) mActivity.getSystemService(WINDOW_SERVICE);
        return windowManager;
    }

    /**
     * 根据 {@suspension} 创建窗口
     *
     * @param suspension 悬浮窗的定义¬
     */
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

    WindowManager windowManager;
    View showingView;


    private void create() {
        if (suspension == null) return;
        if (showingView != null) {
            close();
        }
        // 新建悬浮窗控件
        showingView = suspension.createView();
        // 将悬浮窗控件添加到WindowManager
        getWindowManager().addView(showingView, suspension.getLayoutParams());
    }

    /**
     * 是否有正在显示的悬浮窗
     *
     * @return
     */
    public boolean isShowing() {
        return showingView != null;
    }

    /**
     * 关闭窗口
     */
    public void close() {
        if (showingView != null) {
            getWindowManager().removeView(showingView);
            showingView = null;
        }
    }
}
