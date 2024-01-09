package renderingEngine

import util.Injector
import util.WindowInfo
import earcut4j.Earcut
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.io.File

/**
 * Отвечает за рисование простых геометрических фигур (в данный момент только многоугольники без самопересечений)
 * В данный момент у пользователя нет способа рисовать что-либо с его помощью
 */
object SimpleDrawer {
    private val shaderProgram = ShaderProgram()

    init {
        shaderProgram.setFragmentShader(File("src/main/GLSL/shader.frag").readText())
        shaderProgram.setVertexShader(File("src/main/GLSL/shader.vert").readText())
        shaderProgram.compile()
    }

    private val vao = GL30.glGenVertexArrays()
    private val vbo = GL15.glGenBuffers()
    private val ebo = GL15.glGenBuffers()
    private var width = Injector.getService(WindowInfo::class.java).width
    private var height = Injector.getService(WindowInfo::class.java).height

    init {
        GL30.glBindVertexArray(vao)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 0, GL15.GL_DYNAMIC_DRAW)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, GL15.GL_DYNAMIC_DRAW)

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 24, 0)
        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 24, 12)
        GL20.glEnableVertexAttribArray(1)

        GL30.glBindVertexArray(0)
    }

    /** Для определения границ многоугольника используется библиотека earcut4j (https://github.com/earcut4j/earcut4j) */
    fun drawPolygon(points: FloatArray, color: Color = BLACK) {
        if (points.size % 2 != 0 || points.size < 6) throw IllegalArgumentException("Invalid points")
        val vertices = FloatArray(points.size * 3)

        val normalizedPoints = points.clone()

        for (i in normalizedPoints.indices step 2) {
            normalizedPoints[i] /= width / 2f
            normalizedPoints[i] -= 1f
            normalizedPoints[i + 1] /= -height / 2f
            normalizedPoints[i + 1] += 1f

            vertices[(i / 2) * 6] = normalizedPoints[i]
            vertices[(i / 2) * 6 + 1] = normalizedPoints[i + 1]
            vertices[(i / 2) * 6 + 2] = 0f
            vertices[(i / 2) * 6 + 3] = color.r
            vertices[(i / 2) * 6 + 4] = color.g
            vertices[(i / 2) * 6 + 5] = color.b
        }

        val indices = Earcut.earcut(normalizedPoints.map { it.toDouble() }.toDoubleArray(), null, 2).toIntArray()

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices.size * 4L, GL15.GL_DYNAMIC_DRAW)
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertices)

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices.size * 4L, GL15.GL_DYNAMIC_DRAW)
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, indices)

        shaderProgram.use()
        GL30.glBindVertexArray(vao)
        GL30.glDrawElements(GL30.GL_TRIANGLES, indices.size * 3, GL30.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)
    }

    internal fun close() {
        GL30.glDeleteVertexArrays(vao)
        GL30.glDeleteBuffers(vbo)
        GL30.glDeleteBuffers(ebo)
    }
}