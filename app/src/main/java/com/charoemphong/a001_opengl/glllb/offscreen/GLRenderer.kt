package com.charoemphong.a001_opengl.glllb.offscreen

import android.opengl.EGLContext
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GLRenderer : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {}

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GL2JNILib.init(width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GL2JNILib.step()
    }
}