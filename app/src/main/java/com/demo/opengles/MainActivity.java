package com.demo.opengles;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private TriangleView mTriangle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTriangle = findViewById(R.id.triangle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTriangle.destroy();
    }
}