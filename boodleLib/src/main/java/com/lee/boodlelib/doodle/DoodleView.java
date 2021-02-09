package com.lee.boodlelib.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lee.boodlelib.doodle.action.DrawAction;
import com.lee.boodlelib.doodle.action.DrawActionType;
import com.lee.boodlelib.doodle.action.PathAction;
import com.lee.boodlelib.doodle.config.DoodleConfig;
import com.lee.boodlelib.packaging.define.FileCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 画板
 */
public class DoodleView extends View implements IDoodleView {
    List<DrawAction> drawActionList = new ArrayList<>();
    DrawAction currentAction;
    DrawActionType drawActionType = DrawActionType.Path;

    public DoodleView(Context context) {
        super(context);
    }

    float startX, startY;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    DoodleConfig config;

    private DoodleConfig getConfig() {
        if (config == null) {
            config = DoodleConfig.getDefaultConfig();
        }
        return config;
    }

    public void setConfig(DoodleConfig config) {
        this.config = config;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = touchX;
                startY = touchY;
                createAction();
                break;
            case MotionEvent.ACTION_MOVE:
                currentAction.moveTo(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                currentAction = null;
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (DrawAction item : drawActionList) {
            item.draw(canvas);
        }
    }

    private void createAction() {
        if (drawActionType == DrawActionType.Path) {
            currentAction = new PathAction(startX, startY, getConfig().getPaintSize(), getConfig().getPaintColor());
        }
        drawActionList.add(currentAction);
    }

    @Override
    public boolean isEmpty() {
        return drawActionList == null || drawActionList.isEmpty();
    }

    @Override
    public void back() {
        if (drawActionList.size() == 0) {
            return;
        }
        drawActionList.remove(drawActionList.size() - 1);
        invalidate();
    }

    @Override
    public void clear() {
        drawActionList.clear();
        invalidate();
    }

    @Override
    public Bitmap getBitmap() {
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenshot);
        //我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        canvas.translate(-getScrollX(), -getScrollY());
        draw(canvas);// 将 view 画到画布上
        return screenshot;
    }
}
