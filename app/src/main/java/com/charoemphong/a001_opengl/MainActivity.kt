package com.charoemphong.a001_opengl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.charoemphong.a001_opengl.libs.MyGLSurfaceView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var value = 200.0f
    private lateinit var gLView: GLSurfaceView
    private lateinit var mGLSurfaceView: MyGLSurfaceView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val photo = BitmapFactory.decodeResource(this.resources, R.drawable.img0).resizeByWidth(750)

        mGLSurfaceView = MyGLSurfaceView(this, photo)
        mGLSurfaceView.change(value)
        val frame = findViewById<FrameLayout>(R.id.frameLayout)
        frame.addView(mGLSurfaceView)

        seekBar.min = -360
        seekBar.max = 360
        seekBar.progress = 0
        seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                value = seek.progress.toFloat()
                mGLSurfaceView.change(value)
                Toast.makeText(this@MainActivity,
                        "Progress is: " + seek.progress,
                        Toast.LENGTH_SHORT).show()
                // write custom code for progress is changed
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })
    }

    fun Bitmap.resizeByWidth(width:Int):Bitmap {
        val ratio:Float = this.width.toFloat() / this.height.toFloat()
        val height:Int = Math.round(width / ratio)

        return Bitmap.createScaledBitmap(
                this,
                width,
                height,
                false
        )
    }


    override fun onPause() {
        super.onPause()
        mGLSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView.onResume()
    }
}

