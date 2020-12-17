package com.lee.doodleview.doodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.lee.doodleview.doodle.action.DrawAction;
import com.lee.doodleview.doodle.action.DrawActionType;
import com.lee.doodleview.doodle.action.PathAction;

import java.util.ArrayList;
import java.util.List;

public class DoodleView extends View {
    List<DrawAction> drawActionList = new ArrayList<>();
    DrawAction currentAction;
    DrawActionType drawActionType = DrawActionType.Path;

    public DoodleView(Context context) {
        super(context);
    }

    public DoodleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    int size = 2;
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

                break;
            case MotionEvent.ACTION_UP:

                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    private void createAction() {
        switch (drawActionType) {
            case Path:
                currentAction = new PathAction(startX, startY, size, color);
                break;
        }


    }
}
