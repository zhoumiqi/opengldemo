package com.demo.opengles;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView triangle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        triangle = findViewById(R.id.triangle);
//        triangle.setEGLContextClientVersion(2);
//        triangle.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}