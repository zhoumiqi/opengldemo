package com.demo.opengles;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.demo.opengles.shape.Cube;
import com.demo.opengles.shape.Quadrilateral;
import com.demo.opengles.shape.Shape;
import com.demo.opengles.shape.Square;
import com.demo.opengles.shape.Triangle;
import com.demo.opengles.shape.TriangleWithTexture;
import com.demo.opengles.utils.OpenGlUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 参考 https://www.jianshu.com/p/157c7347937e
 * OpenGL ES 官方开发指导文档:https://github.com/KhronosGroup/OpenGL-Refpages
 * https://www.khronos.org/registry/OpenGL-Refpages/es2.0/
 * API中文文档: https://blog.csdn.net/flycatdeng/article/details/82588903?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param
 */
public class MyGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private int mProgramId;
    private int mColorOrTexCoordId;
    private int mPositionId;
    private int mMatrixId;
    private Shape<?> mShape;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mFragColorOrTextureCoordBuffer;
    private int mTextureId;

    private float[] mViewMatrix;
    private float[] mProjectMatrix;
    private float[] mMVPMatrix;

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
//        mShape = new Triangle();
//        mShape = new Quadrilateral();
//        mShape = new Square();
//        mShape = new TriangleWithTexture();
        mShape = new Cube();

        if (mShape.haveMatrixUniform()) {
            mViewMatrix = new float[16];
            mProjectMatrix = new float[16];
            mMVPMatrix = new float[16];
        }
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
        if (mShape.needEnableDepthTest()) {
            //开启深度测试
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }
        //编译着色器并链接顶点与片元着色器生成OpenGL程序句柄
        mProgramId = OpenGlUtils.loadProgram(mShape);
        //通过OpenGl程序句柄查找获取顶点着色器中的位置句柄
        mPositionId = GLES20.glGetAttribLocation(mProgramId, mShape.getPositionAttrName());
        //通过OpenGL程序句柄查找获取片元着色器中的颜色或纹理坐标句柄
        if (mShape.haveTextureAttr()) {
            mColorOrTexCoordId = GLES20.glGetAttribLocation(mProgramId, mShape.getTextureCoordAttrName());
        } else {
            mColorOrTexCoordId = GLES20.glGetAttribLocation(mProgramId, mShape.getColorAttrName());
        }
        if (mShape.haveMatrixUniform()) {
            //通过OpenGL程序句柄查找顶点着色器中的矩阵变换句柄
            mMatrixId = GLES20.glGetUniformLocation(mProgramId, mShape.getMatrixUniformName());
        }
        //初始化顶点缓冲区
        mVertexBuffer = OpenGlUtils.getFloatBuffer(mShape.getVertexCoordinates());
        //初始化颜色or纹理坐标缓冲区
        mFragColorOrTextureCoordBuffer = OpenGlUtils.getFloatBuffer(mShape.getFragColorOrTexCoords());
        if (mShape.haveTextureAttr()) {
            //初始化纹理id
            mTextureId = OpenGlUtils.getTextureId(R.drawable.qianxun);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mShape.haveMatrixUniform()) {
            setMatrix(width, height);
        } else {
            //设置视口
            GLES20.glViewport(0, 0, width, height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清空颜色缓冲区
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);//白色不透明
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //设置OpenGL所要使用的程序
        GLES20.glUseProgram(mProgramId);
        if (mShape.haveMatrixUniform()) {
            //指定vMatrix的值(location:指定要修改的统一变量的位置。 count:指定要修改的元素数。 如果目标统一变量不是数组，则此值应为1;如果是数组，则应为1或更大。transpose:指定在将值加载到统一变量时是否转置矩阵。 必须是GL_FALSE。)
            GLES20.glUniformMatrix4fv(mMatrixId, 1, false, mMVPMatrix, 0);
        }
        //绘制图形
        drawShape();
    }

    public void destroy() {
        GLES20.glDeleteProgram(mProgramId);
    }

    /**
     * 设置矩阵
     *
     * @param width  宽度
     * @param height 高度
     */
    private void setMatrix(float width, int height) {
        //计算宽高比
        float ratio = width / height;
        //设置透视投影 (根据六个剪切平面定义投影矩阵)
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    /**
     * 绘制纹理贴图
     * 参考：https://blog.csdn.net/qq_36391075/article/details/81564454
     */
    private void drawShapeWithTexture() {
        //设置使用的纹理编号
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //绑定指定的纹理id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
    }


    /**
     * 绘制图形
     */
    private void drawShape() {
        //1、启用图形顶点数据句柄
        GLES20.glEnableVertexAttribArray(mPositionId);
        //2、绑定图形坐标数据,glVertexAttribPointer 参数说明
        //index:指定要修改的顶点属性的索引值
        //size:指定每个顶点属性的组件数量。必须为1、2、3或者4。初始值为4。（如position是由3个（x,y,z）组成，而颜色是4个（r,g,b,a））
        //type:指定数组中每个组件的数据类型。可用的符号常量有GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT,GL_UNSIGNED_SHORT, GL_FIXED, 和 GL_FLOAT，初始值为GL_FLOAT。
        //normalized:指定当被访问时，固定点数据值是否应该被归一化（GL_TRUE）或者直接转换为固定点值（GL_FALSE）。
        //stride:指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0。
        //pointer:指定第一个组件在数组的第一个顶点属性中的偏移量。该数组与GL_ARRAY_BUFFER绑定，储存于缓冲区中。初始值为0；
        //int VERTEX_STRID = mShape.getCoordinatesCountPerVertex() * 4;//byte 、boolean占1字节;short 、char占2字节;int 、float占4字节;long 、double占8字节
        GLES20.glVertexAttribPointer(mPositionId, mShape.getCoordsCountPerVertex(), GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        //3、绑定颜色数据 (必须根据颜色向量声明的类型来调用api,否则类型不正确，颜色取值赋值不对导致显示将不正常)
        //(1)如果顶点颜色用的uniform 类型修饰用以下glUniform4fv来绑定颜色数据
        //GLES20.glUniform4fv(mColorId, mShape.getVectorCountPerFragColor(), mFragColorBuffer);
        //(2)如果顶点颜色用attribute 类型修饰就用glVertexAttribPointer 来绑定颜色数据
        GLES20.glEnableVertexAttribArray(mColorOrTexCoordId);
        GLES20.glVertexAttribPointer(mColorOrTexCoordId, mShape.getVectorCountPerFragColorOrTexCoord(), GLES20.GL_FLOAT, false, 0, mFragColorOrTextureCoordBuffer);
        //4、绘制纹理贴图
        if (mShape.haveTextureAttr()) {
            drawShapeWithTexture();
        }
        if (mShape.drawWithSpecifiedIndex()) {
            //5、绘制图形,方式2：使用使用glDrawElements 绘制图形
            drawElementsWithIndex();
        } else {
            //5、绘制图形,方式1：使用glDrawArrays绘制图形
            //mode这个参数的说明，参考：https://segmentfault.com/a/1190000015445263
            //GL_TRIANGLES 将传入的顶点作为单独的三角形绘制，ABCDEF绘制ABC,DEF两个三角形
            //GL_TRIANGLE_STRIP 将传入的顶点作为三角条带绘制(strip 带)，ABCDEF绘制ABC,BCD,CDE,DEF四个三角形
            //GL_TRIANGLE_FAN 将传入的顶点作为扇面绘制(fan 扇)，ABCDEF绘制ABC、ACD、ADE、AEF四个三角形
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mShape.getVertexCount());
        }
        //6、禁用指向图形的顶点数据
        GLES20.glDisableVertexAttribArray(mPositionId);
        GLES20.glDisableVertexAttribArray(mColorOrTexCoordId);
    }

    /**
     * 使用glDrawElements 绘制图形
     * 可以通过索引数组来指定绘制顺序
     */
    private void drawElementsWithIndex() {
        //设置绘制顶点的索引数据(绘制顶点顺序的下标)
//        byte[] index = {0, 1, 2, 3};
//        ByteBuffer indexBuffer = OpenGlUtils.getByteBuffer(index);
        ShortBuffer indexBuffer = OpenGlUtils.getShortBuffer(mShape.getIndexData());
        //绘制图形
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, mShape.getIndexData().length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
