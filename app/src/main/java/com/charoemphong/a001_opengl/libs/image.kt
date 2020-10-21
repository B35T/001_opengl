package com.charoemphong.a001_opengl.libs

import android.opengl.GLES31
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

private val vertices = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f)

private val textureVertices = floatArrayOf(
        0f, 1f,
        1f, 1f,
        0f, 0f,
        1f, 0f
)
class Square2 {


    private val vertexShaderCode =
            "attribute vec4 aPosition;" +
            "attribute vec2 aTexPosition;" +
            "varying vec2 vTexPosition;" +
            "void main() {" +
                    "  gl_Position = aPosition;" +
            "  vTexPosition = aTexPosition;" +

            "}"

    //luminance
//    private val fragmentShaderCode =
//            "precision mediump float;" +
//            "uniform sampler2D uTexture;" +
//            "varying vec2 vTexPosition;" +
//                    "const vec3 W = vec3(0.2125, 0.7154, 0.0721);"+
//            "void main() {" +
//                    "float T = 1.0;"+
//                    "vec2 st = vTexPosition.st;"+
//                    "vec3 irgb = texture2D(uTexture, st).rgb;"+
//                    "float luminance = dot(irgb, W);"+
//                    "vec3 target = vec3(luminance, luminance, luminance);"+
//            "  gl_FragColor = vec4(mix(target, irgb, T), 1.);" +
//            "}"

    //twirl_fragment_shader
    var fragmentShaderCode = "precision mediump float;"+
            "uniform sampler2D uTexture;"+
            "varying vec2 vTexPosition;" +
            "void main() {" +
            "float Res = 720.0;" +
            "float D = -190.0;" +
            "float R = 0.3;" +
            "vec2 st = vTexPosition.st;" +
            "float Radius = Res * R;" +
            "vec2 xy = Res * st;" +
            "vec2 dxy = xy - Res/2.0;" +
            "float r = length(dxy);" +
            "float beta = atan(dxy.y, dxy.x) + radians(D)*(Radius - r)/Radius;" +
            "vec2 xy1 = xy;" +
            "if(r <= Radius)" +
            "{" +
            "xy1.s = Res/2.0 + r*vec2(cos(beta)).s;" +
            "xy1.t = Res/2.0 + r*vec2(sin(beta)).t;" +
            "}" +
            "st = xy1/Res;" +
            "vec3 irgb = texture2D(uTexture, st).rgb;" +
            "gl_FragColor = vec4(irgb, 1.);" +
            "}"

    private val verticesBuffer : FloatBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
        order(ByteOrder.nativeOrder())

        asFloatBuffer().apply {
            put(vertices)
            position(0)
        }
    }

    private var textureBuffer: FloatBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
        order(ByteOrder.nativeOrder())

        asFloatBuffer().apply {
            put(textureVertices)
            position(0)
        }
    }

    private var program:Int
    private var positionHandle: Int = 0
    private var textureHandle : Int = 0
    private var texturePositionHandle : Int = 0

    init {
        val vertexShader = loadShader(GLES31.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES31.glCreateProgram().also {
            GLES31.glAttachShader(it, vertexShader)
            GLES31.glAttachShader(it, fragmentShader)
            GLES31.glLinkProgram(it)
        }
    }

    fun draw(texture: Int) {
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0)
        GLES31.glUseProgram(program)
        GLES31.glDisable(GLES31.GL_BLEND)

        positionHandle = GLES31.glGetAttribLocation(program, "aPosition")
        textureHandle = GLES31.glGetUniformLocation(program, "uTexture")
        texturePositionHandle = GLES31.glGetAttribLocation(program, "aTexPosition")

        GLES31.glEnableVertexAttribArray(texturePositionHandle)
        GLES31.glVertexAttribPointer(texturePositionHandle, 2, GLES31.GL_FLOAT, false, 0, textureBuffer)

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture)
        GLES31.glUniform1i(textureHandle,0)

        GLES31.glVertexAttribPointer(positionHandle, 2, GLES31.GL_FLOAT, false, 0, verticesBuffer)
        GLES31.glEnableVertexAttribArray(positionHandle)

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
        GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun loadShader(type: Int, shaderCode: String) : Int {
        return  GLES31.glCreateShader(type).also { shader ->
            GLES31.glShaderSource(shader, shaderCode)
            GLES31.glCompileShader(shader)
        }
    }
}