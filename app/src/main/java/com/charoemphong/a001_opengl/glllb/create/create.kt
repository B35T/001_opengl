package com.charoemphong.a001_opengl.glllb.create

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

val COORDS_PER_VERTEX = 3
var triangleCoords = floatArrayOf(     // in counterclockwise order:
        0.0f, 0.622008459f, 0.0f,      // top
        -0.5f, -0.311004243f, 0.0f,    // bottom left
        0.5f, -0.311004243f, 0.0f      // bottom right
)

var bitmap: Bitmap? = null

class create(context: Context) : GLSurfaceView(context){
    private val render: GLSurfaceView.Renderer


    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        render = Renderer()
        // Set the Renderer for drawing on the GLSurfaceView

        setRenderer(render)
    }

    fun toBitmap() : Bitmap? {
        return bitmap
    }

    fun saveFrame() {
        // Get the context wrapper instance
        val filepath = Environment.DIRECTORY_DCIM

        // Create a file to save the image
        val file = File("/storage/emulated/0/$filepath", "gl_${UUID.randomUUID()}.jpg")


        val b = bitmap

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

    internal class Renderer: GLSurfaceView.Renderer {
        private lateinit var triangle: Triangle

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            triangle = Triangle()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)

        }

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            triangle.draw()
        }
    }

    internal class Triangle {

        private var mProgram: Int

        private val vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = vPosition;" +
                        "}"

        private val fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}"

        private var positionHandle: Int = 0
        private var mColorHandle: Int = 0

        private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
        private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

        init {

            val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
            val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

            // create empty OpenGL ES Program
            mProgram = GLES20.glCreateProgram().also {

                // add the vertex shader to program
                GLES20.glAttachShader(it, vertexShader)

                // add the fragment shader to program
                GLES20.glAttachShader(it, fragmentShader)

                // creates OpenGL ES program executables
                GLES20.glLinkProgram(it)
            }
        }

        // Set color with red, green, blue and alpha (opacity) values
        val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

        private var vertexBuffer: FloatBuffer =
                // (number of coordinate values * 4 bytes per float)
                ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
                    // use the device hardware's native byte order
                    order(ByteOrder.nativeOrder())

                    // create a floating point buffer from the ByteBuffer
                    asFloatBuffer().apply {
                        // add the coordinates to the FloatBuffer
                        put(triangleCoords)
                        // set the buffer to read the first coordinate
                        position(0)
                    }
                }

        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

        fun draw() {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(mProgram)

            // get handle to vertex shader's vPosition member
            positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

                // Enable a handle to the triangle vertices
                GLES20.glEnableVertexAttribArray(it)

                // Prepare the triangle coordinate data
                GLES20.glVertexAttribPointer(
                        it,
                        COORDS_PER_VERTEX,
                        GLES20.GL_FLOAT,
                        false,
                        vertexStride,
                        vertexBuffer
                )

                // get handle to fragment shader's vColor member
                mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                    // Set color for drawing the triangle
                    GLES20.glUniform4fv(colorHandle, 1, color, 0)
                }

                // Draw the triangle
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

                // Disable vertex array
                GLES20.glDisableVertexAttribArray(it)
            }
        }

        fun saveTexture(width: Int, height: Int): Bitmap? {
            val frame = IntArray(1)
            GLES20.glGenFramebuffers(1, frame, 0)
            checkGlError("glGenFramebuffers")
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frame[0])
            checkGlError("glFramebufferTexture2D")
            val buffer = ByteBuffer.allocate(width * height * 4)
            GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer)
            checkGlError("glReadPixels")
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(buffer)
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
            checkGlError("glBindFramebuffer")
            GLES20.glDeleteFramebuffers(1, frame, 0)
            checkGlError("glDeleteFramebuffer")
            return bitmap
        }

        private fun checkGlError(op: String) {
            var error: Int
            while (GLES20.glGetError().also { error = it } != GLES20.GL_NO_ERROR) {
                throw RuntimeException("$op: glError $error")
            }
        }
    }
}
