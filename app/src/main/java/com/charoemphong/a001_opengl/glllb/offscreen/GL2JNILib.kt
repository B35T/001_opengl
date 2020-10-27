package com.charoemphong.a001_opengl.glllb.offscreen

import android.graphics.Bitmap

object GL2JNILib {
    /**
     * @param width  the current view width
     * @param height the current view height
     */
    external fun init(width: Int, height: Int)
    external fun step()
    external fun readPixelIntoBitmap(outBitmap: Bitmap?): Int

    init {
        System.loadLibrary("gl2jni")
    }
}