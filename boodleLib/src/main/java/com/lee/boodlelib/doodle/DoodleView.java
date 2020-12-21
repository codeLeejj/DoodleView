package com.lee.boodlelib.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lee.boodlelib.doodle.action.DrawAction;
import com.lee.boodlelib.doodle.action.DrawActionType;
import com.lee.boodlelib.doodle.action.PathAction;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DoodleView extends View implements IDoodleView {
    List<DrawAction> drawActionList = new ArrayList<>();
    DrawAction currentAction;
    DrawActionType drawActionType = DrawActionType.Path;

    public DoodleView(Context context) {
        super(context);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int size = 3;
    int color = Color.BLACK;
    float startX, startY;

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
        switch (drawActionType) {
            case Path:
                currentAction = new PathAction(startX, startY, size, color);
                break;
        }
        drawActionList.add(currentAction);
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

    @Override
    public boolean save(String path) {
        File dest = new File(path);
        return save(dest);
    }

    @Override
    public boolean save(File destFile) {
        if (destFile == null)
            return false;
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdir();
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(destFile));
            getBitmap().compress(Bitmap.CompressFormat.PNG, 50, bos);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
