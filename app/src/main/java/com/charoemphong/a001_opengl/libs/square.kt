package com.charoemphong.a001_opengl.libs

import android.opengl.GLES31
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

const val COORDS_PER_VERTEX = 3

var squareCoords = floatArrayOf(
        -0.3f, -0.3f,  0.0f,  // 0. left-bottom
        0.3f, -0.3f,  0.0f,  // 1. right-bottom
        -0.3f,  0.3f,  0.0f,  // 2. left-top
        0.3f,  0.3f,  0.0f    // 3. right-top
)

class Square {
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)


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

    private var mProgram : Int
    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount : Int = squareCoords.size / COORDS_PER_VERTEX
    private val vertexStride : Int = COORDS_PER_VERTEX * 4

    private val drawOrder = shortArrayOf(0,1,2,0,2,3)

    fun draw() {
        // Add program to OpenGL ES environment
        GLES31.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES31.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES31.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES31.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES31.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES31.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, vertexCount)

            // Disable vertex array
            GLES31.glDisableVertexAttribArray(it)
        }
    }

    private val vertexBuffer : FloatBuffer = ByteBuffer.allocateDirect(squareCoords.size * 4).run {
        order(ByteOrder.nativeOrder())

        asFloatBuffer().apply {
            put(squareCoords)
            position(0)
        }
    }

    private val drawListBuffer: ShortBuffer = ByteBuffer.allocateDirect(drawOrder.size * 2).run {
        order(ByteOrder.nativeOrder())

        asShortBuffer().apply {
            put(drawOrder)
            position(0)
        }
    }

    init {
        val vertexShader = loadShader(GLES31.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES31.glCreateProgram().also {
            GLES31.glAttachShader(it, vertexShader)
            GLES31.glAttachShader(it, fragmentShader)
            GLES31.glLinkProgram(it)
        }
    }

    fun loadShader(type : Int, shaderCode : String) : Int {
        return  GLES31.glCreateShader(type).also { shader ->
            GLES31.glShaderSource(shader, shaderCode)
            GLES31.glCompileShader(shader)
        }
    }
}

