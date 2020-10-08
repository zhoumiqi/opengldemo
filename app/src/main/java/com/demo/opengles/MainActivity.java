package com.demo.opengles;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MyGLSurfaceView mMyGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyGLSurfaceView = findViewById(R.id.my_gl_surface_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyGLSurfaceView.destroy();
    }
}