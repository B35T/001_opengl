package com.charoemphong.a001_opengl.glllb.offscreen

import android.opengl.EGL14
import android.opengl.GLSurfaceView.EGLContextFactory
import android.util.Log
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay


class GLContextFactory : EGLContextFactory {

    override fun createContext(egl: EGL10?, display: EGLDisplay?, eglConfig: EGLConfig?): EGLContext {
        Log.d("TAG", "creating OpenGL ES 2.0 context")
        Util.checkEglError(TAG, "Before eglCreateContext", egl!!)
        val attrib_list = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        val context = egl!!.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list)
        Util.checkEglError(TAG, "After eglCreateContext", egl)
        return context
    }

    override fun destroyContext(egl: EGL10, display: EGLDisplay?, context: EGLContext?) {
        egl.eglDestroyContext(display, context)
    }

    companion object {
        private const val TAG = "GLContextFactory"
        private const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
    }
}