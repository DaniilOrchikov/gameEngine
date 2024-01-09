package renderingEngine

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30

/**
 * Класс отвечающий за компиляцию и использование вершинного и фрагментного шейдеров
 */
class ShaderProgram {
    private var vertexShader: Int? = null
    private var fragmentShader: Int? = null
    var shaderProgram: Int? = null

    fun setVertexShader(vertexShaderSource: String) {
        val success = IntArray(1)
        val infoLog: String
        vertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER)
        GL30.glShaderSource(vertexShader!!, vertexShaderSource)
        GL30.glCompileShader(vertexShader!!)
        GL30.glGetShaderiv(vertexShader!!, GL30.GL_COMPILE_STATUS, success)
        if (success[0] == 0) {
            infoLog = GL30.glGetShaderInfoLog(vertexShader!!)
            throw IllegalStateException("vertexShaderCompilationFailed: $infoLog")
        }
    }

    fun setFragmentShader(fragmentShaderSource: String) {
        val success = IntArray(1)
        val infoLog: String
        fragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER)
        GL30.glShaderSource(fragmentShader!!, fragmentShaderSource)
        GL30.glCompileShader(fragmentShader!!)
        GL30.glGetShaderiv(fragmentShader!!, GL30.GL_COMPILE_STATUS, success)
        if (success[0] == 0) {
            infoLog = GL30.glGetShaderInfoLog(fragmentShader!!)
            throw IllegalStateException("fragmentShaderCompilationFailed: $infoLog")
        }
    }

    fun compile() {
        vertexShader ?: throw IllegalStateException("Vertex shader is null")
        fragmentShader ?: throw IllegalStateException("Fragment shader is null")
        val success = IntArray(1)
        val infoLog: String
        shaderProgram = GL30.glCreateProgram()
        shaderProgram?.let {
            GL30.glAttachShader(it, vertexShader!!)
            GL30.glAttachShader(it, fragmentShader!!)
            GL30.glLinkProgram(it)

            GL30.glDeleteShader(vertexShader!!)
            GL30.glDeleteShader(fragmentShader!!)

            GL30.glGetProgramiv(it, GL30.GL_LINK_STATUS, success)
            if (success[0] == 0) {
                infoLog = GL30.glGetProgramInfoLog(it)
                throw IllegalStateException("shaderProgramCompilationFailed: $infoLog")
            }
        } ?: throw IllegalStateException("Error when executing the glCreateProgram command")
    }

    fun use() {
        shaderProgram?.let {
            glUseProgram(it)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform4f(uniformName: String, v0: Float, v1: Float, v2: Float, v3: Float) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform4f(uniformLocation, v0, v1, v2, v3)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform3f(uniformName: String, v0: Float, v1: Float, v2: Float) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform3f(uniformLocation, v0, v1, v2)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform3f(uniformName: String, v0:Vector3f) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform3f(uniformLocation, v0.x, v0.y, v0.z)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform2f(uniformName: String, v0: Float, v1: Float) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform2f(uniformLocation, v0, v1)
        } ?: throw IllegalStateException("Shader program is null")
    }
    fun uniform2f(uniformName: String, v0: Vector2f) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform2f(uniformLocation, v0.x, v0.y)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform1f(uniformName: String, v0: Float) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform1f(uniformLocation, v0)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform4i(uniformName: String, v0: Int, v1: Int, v2: Int, v3: Int) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform4i(uniformLocation, v0, v1, v2, v3)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform3i(uniformName: String, v0: Int, v1: Int, v2: Int) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform3i(uniformLocation, v0, v1, v2)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform2i(uniformName: String, v0: Int, v1: Int) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform2i(uniformLocation, v0, v1)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniform1i(uniformName: String, v0: Int) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform1i(uniformLocation, v0)
        } ?: throw IllegalStateException("Shader program is null")
    }

    fun uniformMatrix4fv(uniformName: String, v0: Matrix4f) {
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniformMatrix4fv(uniformLocation, false, v0.get(FloatArray(4 * 4)))
        } ?: throw IllegalStateException("Shader program is null")
    }
    fun uniform1fv(uniformName: String, v0: FloatArray){
        shaderProgram?.let {
            val uniformLocation = glGetUniformLocation(it, uniformName)
            glUseProgram(it)
            glUniform1fv(uniformLocation, v0)
        } ?: throw IllegalStateException("Shader program is null")
    }
}