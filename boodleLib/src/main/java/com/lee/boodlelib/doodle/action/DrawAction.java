package com.lee.boodlelib.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 定义绘画动作
 * author: lee
 */
public abstract class DrawAction {
    int color;
    int size;
    float startX, startY;

    Paint paint = new Paint();

    public DrawAction(float x, float y, int size, int color) {
        this.startX = x;
        this.startY = y;
        this.size = size;
        this.color = color;
        paint.setStrokeWidth(size);
        paint.setColor(color);
    }

    public abstract void moveTo(float touchX, float touchY);

    public abstract void draw(Canvas canvas);
}
