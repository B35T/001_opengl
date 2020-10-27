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

    fun toBitmap() : Bitmap? {
        return renderer.mBitmap
    }
    fun saveFrame() {
        // Get the context wrapper instance
        val filepath = Environment.DIRECTORY_DCIM

        // Create a file to save the image
        val file = File("/storage/emulated/0/$filepath", "gl_${UUID.randomUUID()}.jpg")

        renderer.isCreate = false
        val b = renderer.mBitmap

        Log.d("Sizing", "size ${b!!.byteCount}")
        try {
            Log.d("Sizing", file.path)


            val stream: OutputStream = FileOutputStream(file)

            if (b == null) {
                Log.d("Sizing", "no image")
                return
            }
            b.compress(Bitmap.CompressFormat.JPEG, 80, stream)
//            // Flush the stream

            stream.flush()
//
//            // Close stream
            stream.close()
            Toast.makeText(context,"SAVE", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}