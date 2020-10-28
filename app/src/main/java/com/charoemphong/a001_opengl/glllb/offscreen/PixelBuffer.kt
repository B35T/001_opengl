package com.charoemphong.a001_opengl.glllb.offscreen

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import androidx.core.graphics.rotationMatrix
import java.nio.IntBuffer
import javax.microedition.khronos.egl.*
import javax.microedition.khronos.opengles.GL10


class PixelBuffer(var width: Int, var height: Int, contextFactory: GLContextFactory, configChooser: GLConfigChooser) {

    var mBitmap: Bitmap? = null

    private lateinit var mRenderer: GLSurfaceView.Renderer

    private var mEgl : EGL10
    private var mEGLConfig :EGLConfig
    private var mEglDisplay: EGLDisplay
    private var mEglContext :EGLContext
    private var mEglSurface :EGLSurface
    private var mGL:GL10

    var mThreadOwner: String? = null
    init {
        val version = IntArray(2)
        val attribList = intArrayOf(
                EGL10.EGL_WIDTH, width,
                EGL10.EGL_HEIGHT, height,
                EGL10.EGL_NONE
        )

        mEgl = EGLContext.getEGL() as EGL10
        mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        mEgl.eglInitialize(mEglDisplay, version)
        mEGLConfig = configChooser.chooseConfig(mEgl, mEglDisplay)!!
        mEglContext = contextFactory.createContext(mEgl, mEglDisplay, mEGLConfig)

        mEglSurface = mEgl.eglCreatePbufferSurface(mEglDisplay, mEGLConfig, attribList)
        mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)

        mGL = mEglContext.gl as GL10

        // Record thread owner of OpenGL context
        mThreadOwner = Thread.currentThread().name
    }

    fun setRenderer(renderer: GLSurfaceView.Renderer) {
        mRenderer = renderer

        // Does this thread own the OpenGL context?
        if (!Thread.currentThread().getName().equals(mThreadOwner)) {
            Log.d("TAG", "setRenderer: This thread does not own the OpenGL context.")
            return
        }

        // Call the renderer initialization routines
        mRenderer.onSurfaceCreated(mGL, mEGLConfig)
        mRenderer.onSurfaceChanged(mGL, width, height)
    }


    fun getBitmap(): Bitmap? {
        // Do we have a renderer?
        if (mRenderer == null) {
            Log.d("TAG", "getBitmap: Renderer was not set.")
            return null
        }

        // Does this thread own the OpenGL context?
        if (Thread.currentThread().name != mThreadOwner) {
            Log.d("TAG", "getBitmap: This thread does not own the OpenGL context.")
            return null
        }

        // Call the renderer draw routine (it seems that some filters do not
        // work if this is only called once)
        mRenderer.onDrawFrame(mGL)
        mRenderer.onDrawFrame(mGL)
        convertToBitmap()
        return mBitmap
    }


    fun destroy() {
        mRenderer.onDrawFrame(mGL)
        mRenderer.onDrawFrame(mGL)
        mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)
        mEgl.eglDestroySurface(mEglDisplay, mEglSurface)
        mEgl.eglDestroyContext(mEglDisplay, mEglContext)
        mEgl.eglTerminate(mEglDisplay)
    }

    private fun convertToBitmap() {
        val startTime = System.currentTimeMillis()
        val ib: IntBuffer = IntBuffer.allocate(width * height)
        mGL.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib)

        // create Bitmap and copied buffer
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mBitmap!!.copyPixelsFromBuffer(ib)

        //Rotation and Flip
        val matrix = android.graphics.Matrix()
        matrix.postRotate(180f)
        matrix.postScale(-1.0f, 1.0f)

        mBitmap = Bitmap.createBitmap(mBitmap!!, 0,0,width,height,matrix,false)

        val endTime = System.currentTimeMillis()
        Log.d("TAG", "convertToBitmap used " + (endTime - startTime))
    }
}