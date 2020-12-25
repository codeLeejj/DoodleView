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
    public static String bitmap2Base64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

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

    public void save(final Bitmap bitmap, final FileCallback callback) {
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
                    bitmap.compress(Bitmap.CompressFormat.PNG, callback.mQuality, bos);
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
     * 对Bitmap 质量进行压缩
     *
     * @param quality
     * @param bitmap
     * @return
     */
    public Bitmap compress(@IntRange(from = 1, to = 100) int quality, Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);   //留下10%，压缩90%
            return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
