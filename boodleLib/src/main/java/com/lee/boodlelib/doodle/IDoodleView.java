package com.lee.boodlelib.doodle;

import android.graphics.Bitmap;

import com.lee.boodlelib.packaging.define.FileCallback;

public interface IDoodleView {

    boolean isEmpty();

    void back();

    void clear();

    Bitmap getBitmap();
}
