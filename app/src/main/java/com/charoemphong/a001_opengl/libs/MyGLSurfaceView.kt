package com.charoemphong.a001_opengl.libs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MyGLSurfaceView(context: Context, texture: Bitmap) : GLSurfaceView(context) {
    private var renderer : MyGLRenderer2
    private var photo: Bitmap = texture

    fun change(v: Float) {
        renderer.setValue(v)
    }

    init {
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer2(photo)

        setRenderer(renderer)
    }

}


class MyGLRenderer2(var photo: Bitmap) : GLSurfaceView.Renderer {
    private lateinit var square: Square2
    private val textures = IntArray(2)
    private var value: Float = 360.0f

    fun setValue(v : Float) {
        value = v
    }


    fun generateSquare() {
        GLES31.glGenTextures(2, textures, 0);
        GLES31.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, photo, 0);
//
        photo.recycle()
        square = Square2()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        square = Square2()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
        square.draw(textures[0], value)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height);
        GLES31.glClearColor(0f, 0f, 0f, 1f);
        generateSquare()
    }
}



class MyGLRenderer() : GLSurfaceView.Renderer {
    private lateinit var square: Square


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        square = Square()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        square.draw()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)
    }
}



class TextureHelper {
    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)
        if (textureHandle[0] != 0) {
            val options = BitmapFactory.Options()
            options.inScaled = false // No pre-scaling

            // Read in the resource
            val bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options)

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST)

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle()
        }
        if (textureHandle[0] == 0) {
            throw RuntimeException("Error loading texture.")
        }
        return textureHandle[0]
    }
}