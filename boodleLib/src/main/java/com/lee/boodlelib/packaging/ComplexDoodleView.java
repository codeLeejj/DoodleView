package com.lee.boodlelib.packaging;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lee.boodlelib.doodle.DoodleView;
import com.lee.boodlelib.doodle.IDoodleView;
import com.lee.boodlelib.packaging.define.BitmapCallback;
import com.lee.boodlelib.packaging.define.IComplexDoodleView;
import com.lee.boodlelib.packaging.define.FileCallback;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;


/**
 * @program: DoodleView
 * @description: 复合的涂鸦板, 需要自己去填充功能按钮
 * @author: lee
 * @create: 2020-12-18 16:32
 */
public class ComplexDoodleView extends RelativeLayout implements IComplexDoodleView {

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

    @Override
    public void setClose(int id, final Closeable closeable) {
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * set the recall's view id
     *
     * @param id
     */
    public void setRecall(int id) {
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doodleView.back();
            }
        });
    }

    /**
     * set the clear's view id
     *
     * @param id
     */
    public void setClear(int id) {
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doodleView.clear();
            }
        });
    }

    BitmapCallback bitmapCallback;

    /**
     * set the view's id that can get a bitmap
     */
    public void getBitmap(int id, final BitmapCallback callback) {
        if (callback == null) return;
        bitmapCallback = callback;
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapCallback.getImage(doodleView.getBitmap());
            }
        });
    }

    FileCallback fileCallback;

    /**
     * set the view's id that can get a file
     */
    public void getFile(int id, final FileCallback callback) {
        if (callback == null) return;
        fileCallback = callback;
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ComplexDoodleView.this.getFile();
            }
        });
    }

    private void getFile() {
        new AsyncTask<String, String, File>() {
            @Override
            protected File doInBackground(String... strings) {
                boolean save = doodleView.save(fileCallback.savePath);
                if (save) {
                    return fileCallback.savePath;
                }
                return null;
            }

            @Override
            protected void onPostExecute(File file) {
                fileCallback.getImage(file);
            }
        }.execute();
    }
}
