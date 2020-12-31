package com.lee.boodlelib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import androidx.annotation.IntRange;

import com.lee.boodlelib.packaging.define.FileCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: DoodleView
 * @description: Bitmap 工具类
 * @author: lee
 * @create: 2020-12-21 21:01
 */
public class BitmapUtil {
    public static String bitmap2Base64(Bitmap bitmap, Bitmap.CompressFormat outFormat) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(outFormat, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base642Bitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 保存到文件
     *
     * @param bitmap    要保存的bitmap
     * @param outFormat 输出的图片格式
     * @param callback  保存完bitmap后回调
     */
    public static void save(final Bitmap bitmap, final Bitmap.CompressFormat outFormat, final FileCallback callback) {
        if (callback == null) {
            return;
        }
        final File destFile = callback.savePath;

        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdir();
        }
        new AsyncTask<String, String, File>() {
            @Override
            protected File doInBackground(String... strings) {
                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(destFile));
                    bitmap.compress(outFormat, callback.mQuality, bos);
                    return destFile;
                } catch (FileNotFoundException e) {
                    return null;
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

            @Override
            protected void onPostExecute(File file) {
                callback.getImage(file);
            }
        }.execute();
    }

    /**
     * 对Bitmap 质量进行质量压缩
     *
     * @param bitmap
     * @param outFormat 输出的图片格式
     * @param quality   压缩质量 取值为 (1-100)
     * @return 压缩后的bitmap
     */
    public static Bitmap compress(Bitmap bitmap, Bitmap.CompressFormat outFormat, @IntRange(from = 1, to = 100) int quality) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(outFormat, quality, baos);
            return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对Bitmap 质量进行大小压缩
     *
     * @param bitmap
     * @param outFormat 输出的图片格式
     * @param maxHeight 最大的高度
     * @param maxWidth  最大的宽度
     * @return 压缩后的bitmap
     */
    public static Bitmap compress(Bitmap bitmap, Bitmap.CompressFormat outFormat, int maxHeight, int maxWidth) {
        if (bitmap == null) {
            return null;
        }
        int srcHeight = bitmap.getHeight();
        int srcWidth = bitmap.getWidth();
        if (srcHeight < maxHeight && srcWidth < maxWidth) {
            return bitmap;
        }
        float expectScale = Math.max(srcHeight * 1.0f / maxHeight, srcWidth * 1.0f / maxWidth);
        return compress4SimpleSize(bitmap, outFormat, (int) expectScale);
    }

    /**
     * 对Bitmap 质量进行大小压缩
     *
     * @param bitmap
     * @param outFormat  输出的图片格式
     * @param simpleSize 简单的压缩比
     * @return 压缩后的bitmap
     */
    public static Bitmap compress4SimpleSize(Bitmap bitmap, Bitmap.CompressFormat outFormat, int simpleSize) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(outFormat, 100, baos);
            byte[] bytes = baos.toByteArray();

            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;

            //获取采样率
            option.inSampleSize = simpleSize;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
            option.inJustDecodeBounds = false;
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
            return bitmap1;
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
