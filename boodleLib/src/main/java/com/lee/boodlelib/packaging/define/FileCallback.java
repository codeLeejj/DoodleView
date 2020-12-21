package com.lee.boodlelib.packaging.define;

import java.io.File;

/**
 * @program: DoodleView
 * @description: 手写完成, 保存文件
 * @author: lee
 * @create: 2020-12-21 09:26
 */
public abstract class FileCallback implements ImageCallback {
    public File savePath;

    public FileCallback(File savePath) {
        this.savePath = savePath;
    }

    public abstract void getImage(File bitmap);
}
