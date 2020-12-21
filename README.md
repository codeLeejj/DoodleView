# DoodleView
Doodle view

IDoodleView 为画板

ComplexDoodleView 对 IDoodleView 进行包装，并开放操作，已插件方式设置各个操作view

SuspensionHelper 负责WindowManger的操作，

ISuspension 定义显示视图，及layoutParams



### 使用

记得添加权限

```html
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

检查权限

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_CODE);
            } else {
                show();
            }
        } else {
            show();
        }
```

#### 简单使用：

```java
private void show() {
        if (helper == null)
            helper = new SuspensionHelper(MainActivity.this);

        if (suspension == null) {
            suspension = new ISuspension() {
                @Override
                public WindowManager.LayoutParams getLayoutParams() {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    } else {
                        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                    }
                    layoutParams.format = PixelFormat.RGBA_8888;

                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();
                    int height = display.getHeight();
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
                    complexDoodleView.getFile(R.id.btCompleteFile,
                            new FileCallback(new File(getCacheDir(), "1221.png")) {
                                @Override
                                public void getImage(File bitmap) {
                                    Toast.makeText(getBaseContext(), "文件路径:" + bitmap.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    return complexDoodleView;
                }
            };
        }
        helper.show(suspension);
    }
```

