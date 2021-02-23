package com.lee.boodlelib.helper;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

/**
 * 作为悬浮窗的管理帮助类
 * 使用单例的方式,可以统一管理全局的悬浮框
 */
public class SuspensionHelper {

    Context mContext;
    WindowManager windowManager;
    static SuspensionHelper helper;
    ISuspension suspension;

    private SuspensionHelper(Context context) {
        mContext = context;
        if (windowManager == null)
            // 获取WindowManager服务
            windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
    }

    public static SuspensionHelper getInstance(Context context) {
        if (helper == null) {
            helper = new SuspensionHelper(context);
        } else {
            helper.mContext = context;
        }
        return helper;
    }


    /**
     * 根据 {@suspension} 创建窗口
     *
     * @param suspension 悬浮窗的定义¬
     */
    public void show(ISuspension suspension) {
        this.suspension = suspension;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(mContext)) {
                create();
            }
        } else {
            create();
        }
    }

    List<View> showingViews;

    private void create() {
        if (suspension == null) return;
        if (showingViews == null) {
            showingViews = new ArrayList<>();
        } else if (showingViews.size() > 0) {
            //当前是作为单个view 过度到多个,所以先简单处理
            closeAll();
        }
        // 新建悬浮窗控件
        View showingView = suspension.createView();
        showingViews.add(showingView);
        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(showingView, suspension.getLayoutParams());
    }

    /**
     * 是否有正在显示的悬浮窗
     *
     * @return
     */
    public boolean isShowing() {
        return showingViews != null && showingViews.size() > 0;
    }

    /**
     * 关闭窗口
     */
    public void closeAll() {
        if (showingViews != null) {
            for (View showingView : showingViews) {
                close(showingView);
            }
            showingViews.clear();
        }
    }

    /**
     * 关闭窗口
     */
    public void close(View view) {
        windowManager.removeView(view);
    }
}
