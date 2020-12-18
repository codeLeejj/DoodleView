package com.lee.doodleview.doodle;

import android.graphics.Bitmap;

import java.io.File;

public interface IDoodleView {

    void back();

    void clear();

    Bitmap getBitmap();

    boolean save(String path);

    boolean save(File destFile);
}
