package com.charoemphong.a001_opengl.glllb

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import com.charoemphong.a001_opengl.glllb.flitter.Twirl
import java.nio.ByteBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GLlllbRenderer(var photo: Bitmap) : GLSurfaceView.Renderer {
    private lateinit var twikl: Twirl
     val textures = IntArray(2)
    var mBitmap: Bitmap? = null
    private var value: Float = 360.0f

    fun setValue(v: Float) {
        value = v
    }

    var isCreate: Boolean = false

    fun loadTexture() {
        GLES31.glGenTextures(2, textures, 0)
        GLES31.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE)

        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, photo, 0)
//        photo.recycle()
        twikl = Twirl()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        // init
        twikl = Twirl()
        twikl.width = photo.width
        twikl.height = photo.height
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
        twikl.draw(textures[0], value)
        this.toImage()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height);
        GLES31.glClearColor(0f, 0f, 0f, 1f);
        this.loadTexture()
    }

    fun toBitmap(): Bitmap? {
        val buffer = ByteBuffer.allocate(photo.width * photo.height * 4)
        GLES31.glReadPixels(0, 0, photo.width, photo.height, GLES31.GL_RGBA, GLES31.GL_UNSIGNED_BYTE, buffer)
        val bitmap = Bitmap.createBitmap(photo.width, photo.height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

    fun toImage(){
        if (!isCreate) {
            val buffer = ByteBuffer.allocate(photo.width * photo.height * 4)
            GLES31.glPixelStorei(GL10.GL_PACK_ALIGNMENT, 4)
            GLES31.glReadBuffer(GL10.GL_FRONT)
            GLES31.glReadPixels(0, 0, photo.width, photo.height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buffer)
            val bitmap = Bitmap.createBitmap(photo.width, photo.height, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(buffer)
            mBitmap = bitmap
            isCreate = true
        }
    }

}
