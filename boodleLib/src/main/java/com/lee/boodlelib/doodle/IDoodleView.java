package com.lee.boodlelib.doodle;

import android.graphics.Bitmap;

import com.lee.boodlelib.doodle.config.DoodleConfig;
import com.lee.boodlelib.packaging.define.FileCallback;

public interface IDoodleView {

    void setConfig(DoodleConfig config);

    boolean isEmpty();

    void back();

    void clear();

    Bitmap getBitmap();
}
