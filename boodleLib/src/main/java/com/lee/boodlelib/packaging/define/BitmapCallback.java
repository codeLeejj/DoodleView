package com.lee.boodlelib.packaging.define;

import android.graphics.Bitmap;

/**
 * @program: DoodleView
 * @description: 手写完成, 获取Bitmap
 * @author: lee
 * @create: 2020-12-21 09:24
 */
public interface BitmapCallback extends ImageCallback {
    void getImage(Bitmap bitmap);
}
