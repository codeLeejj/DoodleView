package com.lee.doodleview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lee.doodleview.doodle.DoodleView;
import com.lee.doodleview.doodle.IDoodleView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    IDoodleView doodleView;

    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doodleView = findViewById(R.id.doodleView);
        iv = findViewById(R.id.iv);
        findViewById(R.id.btBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doodleView.back();
            }
        });
        findViewById(R.id.btReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doodleView.clear();
            }
        });
        findViewById(R.id.btComplete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = doodleView.getBitmap();
                iv.setImageBitmap(bitmap);

                doodleView.save(new File(getCacheDir(),"1218.png"));
            }
        });
    }
}
