package com.charoemphong.a001_opengl.glllb

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLSurfaceView

class GLlllbView(context: Context, texture: Bitmap) : GLSurfaceView(context) {
    private var renderer : GLlllbRenderer
    private var photo: Bitmap = texture

    fun change(v: Float) {
        renderer.setValue(v)
    }

    init {
        setEGLContextClientVersion(2)

        renderer = GLlllbRenderer(photo)

        setRenderer(renderer)
    }

}