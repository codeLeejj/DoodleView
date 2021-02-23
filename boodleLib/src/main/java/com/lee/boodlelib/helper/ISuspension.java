package com.lee.boodlelib.helper;

import android.view.View;
import android.view.WindowManager;

/**
 * @program: DoodleView
 * @description: Describe the floating view and control how it is displayed
 * @author: lee
 * @create: 2020-12-21 15:00
 */
public interface ISuspension {
    /**
     * example:
     * WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
     * layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
     * } else {
     * layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
     * }
     * layoutParams.format = PixelFormat.RGBA_8888;
     * layoutParams.width = 500;
     * layoutParams.height = 500;
     * layoutParams.x = 100;
     * layoutParams.y = 100;
     *
     * @return
     */
    WindowManager.LayoutParams getLayoutParams();

    /**
     * draw one view to show
     *
     * @return view to show
     */
    View getView();
}
