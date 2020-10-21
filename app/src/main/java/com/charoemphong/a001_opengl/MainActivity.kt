package com.charoemphong.a001_opengl

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.charoemphong.a001_opengl.libs.MyGLSurfaceView

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gLView = MyGLSurfaceView(this)
        setContentView(gLView)
    }
}