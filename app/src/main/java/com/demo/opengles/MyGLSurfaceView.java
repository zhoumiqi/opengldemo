package com.demo.opengles;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.demo.opengles.shape.Quadrilateral;
import com.demo.opengles.shape.Shape;
import com.demo.opengles.utils.OpenGlUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 参考 https://www.jianshu.com/p/157c7347937e
 */
public class MyGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {
    //    private Shape<?> mShape = new Triangle();
    private Shape<?> mShape = new Quadrilateral();
    //绘制顶点的索引数据
    private byte[] index = {0, 1, 2, 3};

    private int mProgramId;
    private int mColorId;
    private int mPositionId;

    //设置每个顶点的坐标数
    private static final int COORDS_PER_VERTEX = 3;
    //下一个顶点和上一个顶点之间的步长，以字节为单位，每个float类型变量为4字节
    private final int VERTEX_STRID = COORDS_PER_VERTEX * 4;
    //顶点个数
    private final int VERTEX_COUNT = mShape.getVertexCoordinates().length / COORDS_PER_VERTEX;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mFragColorBuffer;

    public MyGLSurfaceView(Context context) {
        super(context);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
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
        mProgramId = OpenGlUtils.loadProgram(mShape);
        //通过OpenGl程序句柄查找获取顶点着色器中的位置句柄
        mPositionId = GLES20.glGetAttribLocation(mProgramId, "vPosition");
        //通过OpenGL程序句柄查找获取片元着色器中的颜色句柄
        mColorId = GLES20.glGetUniformLocation(mProgramId, "vColor");
        //初始化顶点缓冲区
        mVertexBuffer = OpenGlUtils.getFloatBuffer(mShape.getVertexCoordinates());
        //初始化颜色缓冲区
        mFragColorBuffer = OpenGlUtils.getFloatBuffer(mShape.getFragColor());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口
        GLES20.glViewport(0, 0, width, height);
    }

    public void destroy() {
        GLES20.glDeleteProgram(mProgramId);
    }

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
        //绑定颜色数据 确定count参数的值
        GLES20.glUniform4fv(mColorId, 1, mFragColorBuffer);
//        GLES20.glEnableVertexAttribArray(mColorId);
//        GLES20.glVertexAttribPointer(mColorId, 1, GLES20.GL_FLOAT, false, 0, mFragColorBuffer);
        //绘制三角形
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);
        //参考：https://segmentfault.com/a/1190000015445263
        // GL_TRIANGLES 将传入的顶点作为单独的三角形绘制，ABCDEF绘制ABC,DEF两个三角形
        //GL_TRIANGLE_STRIP 将传入的顶点作为三角条带绘制(strip 带)，ABCDEF绘制ABC,BCD,CDE,DEF四个三角形
        //GL_TRIANGLE_FAN 将传入的顶点作为扇面绘制(fan 扇)，ABCDEF绘制ABC、ACD、ADE、AEF四个三角形
        //glDrawArrays 绘制四边形
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, VERTEX_COUNT);
        ByteBuffer indexBuffer = ByteBuffer.allocateDirect(index.length).put(index);
        indexBuffer.position(0);
        //glDrawElements 绘制四边形
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, VERTEX_COUNT, GLES20.GL_UNSIGNED_BYTE, indexBuffer);
        //禁用指向三角形的顶点数据
        GLES20.glDisableVertexAttribArray(mPositionId);
//        GLES20.glDisableVertexAttribArray(mColorId);

    }
}
