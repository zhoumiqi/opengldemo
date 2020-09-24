package com.demo.opengles;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 参考 https://www.jianshu.com/p/157c7347937e
 */
public class TriangleView extends GLSurfaceView implements GLSurfaceView.Renderer {

    //三角形顶点数组
    private static final float[] TRIANGLE_COORDS = {
            0.0f, 1.0f, 0.0f,//顶点
            1.0f, 0.0f, 0.0f,//顶点
            0.0f,-1.0f,0.0f,
            -1.0f, 0.0f, 0.0f,//左下角
    };

    //顶点着色器
    private static final String VERTEX_SHADER = "" +
            "//根据所设置的顶点数据而插值后的顶点坐标\n" +
            "attribute vec4 vPosition;" +
            "void main(){" +
            "//设置最终坐标\n" +
            "gl_Position = vPosition;" +
            "}";

    //片元着色器
    private static final String FRAGMENT_SHADER = "" +
            "//设置float 类型默认精度，顶点着色器默认highp,片元着色器需要用户声明\n" +
            "precision mediump float;" +
            "//颜色值，vec4代表四维向量，此处由用户传入，数据格式为{r,g,b,a}\n" +
            "uniform vec4 vColor;" +
            "void main(){" +
            "//该片元最终颜色值\n" +
            "gl_FragColor = vColor;" +
            "}";
    //设置三角形的颜色和透明度
    private static final float[] COLOR = {1.0f, 0.0f, 0.0f, 1.0f};//红色不透明

    private int mProgramId;
    private int mColorId;
    private int mPositionId;

    //设置每个顶点的坐标数
    private static final int COORDS_PER_VERTEX = 3;
    //下一个顶点和上一个顶点之间的步长，以字节为单位，每个float类型变量为4字节
    private final int VERTEX_STRID = COORDS_PER_VERTEX * 4;
    //顶点个数
    private final int VERTEX_COUNT = TRIANGLE_COORDS.length / COORDS_PER_VERTEX;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mFragColorBuffer;

    public TriangleView(Context context) {
        super(context);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        //设置渲染器
        setRenderer(this);
        //设置渲染模式
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //编译着色器并链接顶点与片元着色器生成OpenGL程序句柄
        //可以从glsl文件中加载着色器源码
        String vertexShaderStr = OpenGlUtils.getShaderString(getContext(),"my_vertex.glsl");
        String fragShaderStr = OpenGlUtils.getShaderString(getContext(),"my_frag_color.glsl");
        mProgramId = OpenGlUtils.loadProgram(vertexShaderStr, fragShaderStr);
//        mProgramId = OpenGlUtils.loadProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        //通过OpenGl程序句柄查找获取顶点着色器中的位置句柄
        mPositionId = GLES20.glGetAttribLocation(mProgramId, "vPosition");
        //通过OpenGL程序句柄查找获取片元着色器中的颜色句柄
        mColorId = GLES20.glGetUniformLocation(mProgramId, "vColor");
        //初始化顶点缓冲区
        mVertexBuffer = OpenGlUtils.getFloatBuffer(TRIANGLE_COORDS);
        //初始化颜色缓冲区
        mFragColorBuffer = OpenGlUtils.getFloatBuffer(COLOR);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口
        GLES20.glViewport(0, 0, width, height);
    }

    public void destroy() {
        GLES20.glDeleteProgram(mProgramId);
    }
    //顶点索引数据
    private byte[] index={0,1,2,3};

    @Override
    public void onDrawFrame(GL10 gl) {
        //清空颜色缓冲求
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);//白色不透明
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //设置OpenGL所要使用的程序
        GLES20.glUseProgram(mProgramId);
        //启用三角形顶点数据句柄
        GLES20.glEnableVertexAttribArray(mPositionId);
        //绑定三角形坐标数据
        GLES20.glVertexAttribPointer(mPositionId, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRID, mVertexBuffer);
        //绑定颜色数据
        GLES20.glUniform4fv(mColorId, 1, mFragColorBuffer);
        //绘制三角形
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);
        //参考：https://segmentfault.com/a/1190000015445263
        // GL_TRIANGLES 将传入的顶点作为单独的三角形绘制，ABCDEF绘制ABC,DEF两个三角形
        //GL_TRIANGLE_STRIP 将传入的顶点作为三角条带绘制，ABCDEF绘制ABC,BCD,CDE,DEF四个三角形
        //GL_TRIANGLE_FAN 将传入的顶点作为扇面绘制，ABCDEF绘制ABC、ACD、ADE、AEF四个三角形
        //glDrawArrays 绘制四边形
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, VERTEX_COUNT);
        ByteBuffer indexBuffer = ByteBuffer.allocateDirect(index.length).put(index);
        indexBuffer.position(0);
        //glDrawElements 绘制四边形 GL_TRIANGLE_STRIP 是否可以实现
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, VERTEX_COUNT, GLES20.GL_UNSIGNED_BYTE ,indexBuffer);
        //禁用指向三角形的顶点数据
        GLES20.glDisableVertexAttribArray(mPositionId);

    }
}
