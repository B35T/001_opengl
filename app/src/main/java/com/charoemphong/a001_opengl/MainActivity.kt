package com.charoemphong.a001_opengl

import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.charoemphong.a001_opengl.libs.MyGLSurfaceView

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val photo = BitmapFactory.decodeResource(this.resources, R.drawable.img0)


        gLView = MyGLSurfaceView(this,photo)
        setContentView(gLView)
    }
}

