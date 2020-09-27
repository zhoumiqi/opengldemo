package com.demo.opengles.shader;

import android.opengl.GLES20;

import com.demo.opengles.MyApp;
import com.demo.opengles.utils.Constants;
import com.demo.opengles.utils.OpenGlUtils;

public class SquareShader implements Shader {
    @Override
    public int loadVertexShader() {
        return OpenGlUtils.loadShader(OpenGlUtils.getShaderString(MyApp.getInstance(), Constants.DEFAULT_VERTEX_SHADER), GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public int loadFragmentShader() {
        return OpenGlUtils.loadShader(OpenGlUtils.getShaderString(MyApp.getInstance(), Constants.DEFAULT_FRAGMENT_SHADER), GLES20.GL_FRAGMENT_SHADER);
    }
}
