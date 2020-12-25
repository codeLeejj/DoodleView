package com.lee.doodleview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.lee.boodlelib.helper.ISuspension;
import com.lee.boodlelib.helper.SuspensionHelper;
import com.lee.boodlelib.packaging.ComplexDoodleView;
import com.lee.boodlelib.packaging.define.BitmapCallback;
import com.lee.boodlelib.packaging.define.FileCallback;

import java.io.Closeable;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    ComplexDoodleView complexDoodleView;

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btSuspension).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSetting();
            }
        });

        complexDoodleView = findViewById(R.id.doodleView);

        complexDoodleView.setRecall(R.id.btBack);
        complexDoodleView.setClear(R.id.btReset);
        iv = findViewById(R.id.iv);

        complexDoodleView.getBitmap(R.id.btComplete, new BitmapCallback() {
            @Override
            public void getImage(Bitmap bitmap) {
                iv.setImageBitmap(bitmap);
            }
        });
//        complexDoodleView.getFile(R.id.btCompleteFile,
//                new FileCallback(10,new File(getCacheDir(), "1221.png")) {
//                    @Override
//                    public void getImage(File bitmap) {
//                        Toast.makeText(getBaseContext(), "文件路径:" + bitmap.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    public static final int REQUEST_CODE = 500;
    SuspensionHelper helper;

    private void checkSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_CODE);
            } else {
                show();
            }
        } else {
            show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    show();
                }
            }
        }
    }

    ISuspension suspension;

    private void show() {
        if (helper == null)
            helper = new SuspensionHelper(MainActivity.this);

        if (suspension == null) {
            suspension = new ISuspension() {
                @Override
                public WindowManager.LayoutParams getLayoutParams() {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        layoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
//                        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    } else {
                        //app 内有效
                        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
                    }
                    //整个 系统有效(慎用)
//                        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;

                    //设置没有焦点不能touch,这样其他的界面才可以滑动和操作
                    //如果想独占触摸事件就关闭这句话
//                    layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

                    layoutParams.format = PixelFormat.RGBA_8888;

                    //获取屏幕大小
                    Display display = getWindowManager().getDefaultDisplay();
                    Point outSize = new Point();
                    display.getSize(outSize);
                    int width = outSize.x;
                    int height = outSize.y;

                    //根据需要设置位置大小
                    layoutParams.width = width;
                    layoutParams.height = height / 2;
                    layoutParams.x = 0;
                    layoutParams.y = height / 2;
                    return layoutParams;
                }

                @Override
                public View createView() {
                    ComplexDoodleView complexDoodleView = (ComplexDoodleView) getLayoutInflater().inflate(R.layout.suspension_doodle, null, false);
                    complexDoodleView.setClose(R.id.btClose, new Closeable() {
                        @Override
                        public void close() {
                            helper.close();
                        }
                    });
                    complexDoodleView.setRecall(R.id.btBack);
                    complexDoodleView.setClear(R.id.btReset);

                    complexDoodleView.getBitmap(R.id.btComplete, new BitmapCallback() {
                        @Override
                        public void getImage(Bitmap bitmap) {
                            iv.setImageBitmap(bitmap);
                        }
                    });
//                    complexDoodleView.getFile(R.id.btCompleteFile,
//                            new FileCallback(10,new File(getCacheDir(), "1221.png")) {
//                                @Override
//                                public void getImage(File bitmap) {
//                                    Toast.makeText(getBaseContext(), "文件路径:" + bitmap.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
                    return complexDoodleView;
                }
            };
        }
        helper.show(suspension);
    }
}