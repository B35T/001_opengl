package com.charoemphong.a001_opengl

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.charoemphong.a001_opengl.glllb.GLlllbRenderer
import com.charoemphong.a001_opengl.glllb.GLlllbView
import com.charoemphong.a001_opengl.glllb.create.create
import com.charoemphong.a001_opengl.glllb.offscreen.GLConfigChooser
import com.charoemphong.a001_opengl.glllb.offscreen.GLContextFactory
import com.charoemphong.a001_opengl.glllb.offscreen.GLRenderer
import com.charoemphong.a001_opengl.glllb.offscreen.PixelBuffer
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    var value = 360.0f
    private lateinit var mGLSurfaceView: GLlllbView
    private lateinit var  pixelBuffer: PixelBuffer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
//                String{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)
                val s = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this,s,0)
            }
        }

        val photo = BitmapFactory.decodeResource(this.resources, R.drawable.img0)
        Log.d("IMAGE_SIZE", "${photo.width}, ${photo.height}")

        mGLSurfaceView = GLlllbView(this, photo)
        mGLSurfaceView.change(value)
        val frame = findViewById<FrameLayout>(R.id.frameLayout)
        frame.addView(mGLSurfaceView)

        seekBar.min = -360
        seekBar.max = 360
        seekBar.progress = 360
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

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            pixelBuffer = PixelBuffer(1080, 1080, GLContextFactory(), GLConfigChooser(8,8,8,8,0,0))
            pixelBuffer.setRenderer(GLlllbRenderer(photo))
            val bitmap : Bitmap? = pixelBuffer.getBitmap()

            if (bitmap != null) {
                try {
                    val filepath = Environment.DIRECTORY_DCIM

                    // Create a file to save the image
                    val file = File("/storage/emulated/0/$filepath", "gl_${UUID.randomUUID()}.jpg")

                    val fos = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    Log.d("Ragnarok", "successfully store bitmap")
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
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

