package com.lee.boodlelib.packaging.define;

import java.io.Closeable;

/**
 * @program: DoodleView
 * @description: 设置复合涂鸦板的控制类
 * @author: lee
 * @create: 2020-12-21 14:29
 */
public interface IComplexDoodleView {
    /**
     * @param id
     */
    void setClose(int id, Closeable closeable);

    void setRecall(int id);

    void setClear(int id);

    void getBitmap(int id, BitmapCallback callback);
}
