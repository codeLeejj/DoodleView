package com.lee.doodleview.packaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.IdRes;

import com.lee.doodleview.doodle.DoodleView;
import com.lee.doodleview.doodle.IDoodleView;

import java.io.File;


/**
 * @program: DoodleView
 * @description: 复合的涂鸦板
 * @author: lee
 * @create: 2020-12-18 16:32
 */
public class ComplexDoodleView extends RelativeLayout {
    interface ImageCallback {

    }

    interface BitmapCallback extends ImageCallback {
        void getImage(Bitmap bitmap);
    }

    interface FileCallback extends ImageCallback {
        void getImage(File bitmap);
    }

    public ComplexDoodleView(Context context) {
        super(context);
        init(context);
    }

    public ComplexDoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 涂鸦板
     */
    IDoodleView doodleView;

    private void init(Context context) {
        //1.添加涂鸦板
        doodleView = new DoodleView(context);
        ViewGroup.LayoutParams doodleViewLayoutParams
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView((View) doodleView, doodleViewLayoutParams);
    }

    public void setRecall(@IdRes int id) {
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doodleView.back();
            }
        });
    }

    public void setClear(@IdRes int id) {
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doodleView.clear();
            }
        });
    }

    public void setSave(@IdRes int id, final ImageCallback callback) {
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取bitmap
                if (callback instanceof BitmapCallback) {

                }
                //获取file
                if (callback instanceof FileCallback) {

                }
            }
        });
    }
}
