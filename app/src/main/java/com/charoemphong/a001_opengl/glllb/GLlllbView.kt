package com.charoemphong.a001_opengl.glllb

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.os.Environment
import android.util.Log
import android.util.Size
import android.widget.Toast
import java.io.*
import java.util.*

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