package com.lee.doodleview.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class PathAction extends DrawAction {
    Path mPath;

    public PathAction(float x, float y, int size, int color) {
        super(x, y, size, color);
        mPath = new Path();
        mPath.moveTo(x, y);

        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    public void moveTo(float touchX, float touchY) {
        mPath.quadTo(this.startX, this.startY, (touchX + startX) / 2, (touchY + startY) / 2);
        this.startX = touchX;
        this.startY = touchY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, paint);
    }
}
