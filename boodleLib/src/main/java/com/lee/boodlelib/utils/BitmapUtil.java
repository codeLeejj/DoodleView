package com.lee.boodlelib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.IntRange;

import com.lee.boodlelib.packaging.define.FileCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @program: DoodleView
 * @description: Bitmap 工具类
 * @author: lee
 * @create: 2020-12-21 21:01
 */
public class BitmapUtil {
    public static final String TAG = "BitmapUtil";

    /**
     * 将bitmap转义成base64的字符串
     *
     * @param bitmap    被转义的bitmap
     * @param outFormat 输出的格式
     * @return 转义后的base64
     */
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
     * @param callback  保存完bitmap后回调(包含文件保存地址)
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
     * 对Bitmap 大小压缩到指定大小
     *
     * @param bitmap
     * @param height 输出的高度
     * @param width  输出的宽度
     * @return 压缩后的bitmap
     */
    public static Bitmap compress(Bitmap bitmap, int height, int width) {
        if (bitmap == null) {
            return null;
        }

        float wScale = width * 1.0f / bitmap.getWidth();
        float hScale = height * 1.0f / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setScale(wScale, hScale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 对Bitmap 大小压缩到指定文件大小
     *
     * @param bitmap
     * @param kb     输出文件大小
     * @return 压缩后的bitmap
     */
    public static Bitmap compress2FileSize(Bitmap bitmap, Bitmap.CompressFormat format, int kb) {
        if (bitmap == null) {
            return null;
        }
        float fileSize = getFileSize(bitmap, format) / 1024f;
        if (fileSize < kb) {
            return bitmap;
        }
        float v = kb / fileSize;
        double sqrt = Math.sqrt(v);
        float scale = (float) sqrt;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static int getFileSize(Bitmap bitmap) {
        return getFileSize(bitmap, Bitmap.CompressFormat.JPEG);
    }

    /**
     * 获取bitmap的数据大小
     *
     * @param bitmap
     * @param format
     * @return 获取到数据大小(单位 bt)
     */
    public static int getFileSize(Bitmap bitmap, Bitmap.CompressFormat format) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到
            bitmap.compress(format, 100, baos);
            return baos.size();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对Bitmap 大小压缩
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
     * 对Bitmap 大小压缩
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

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }


}
