package com.charoemphong.a001_opengl.glllb.offscreen

import android.util.Log
import javax.microedition.khronos.egl.EGL10


object Util {
    fun checkEglError(TAG: String?, prompt: String?, egl: EGL10) {
        var error: Int
        while (egl.eglGetError().also { error = it } != EGL10.EGL_SUCCESS) {
            Log.d("TAG", String.format("%s: EGL error: 0x%x", prompt, error))
        }
    }
}