package com.charoemphong.a001_opengl.libs

import android.content.Context
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer : MyGLRenderer

    init {
        setEGLContextClientVersion(3)

        renderer = MyGLRenderer()

        setRenderer(renderer)

    }
}

class MyGLRenderer : GLSurfaceView.Renderer {
    private lateinit var square: Square

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES31.glClearColor(0.0f,0.0f,0.0f,0.0f)

        square = Square()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        square.draw()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0,0,width, height)
    }
}