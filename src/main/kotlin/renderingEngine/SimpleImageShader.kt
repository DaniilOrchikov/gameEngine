package renderingEngine

import java.io.File

/**
 * Шейдер с помощью которого рисуются все картинки (Image)
 */
object SimpleImageShader {
    val shaderProgram: ShaderProgram = ShaderProgram()

    init{
        shaderProgram.setVertexShader(File("src/main/GLSL/image.vert").readText())
        shaderProgram.setFragmentShader(File("src/main/GLSL/image.frag").readText())
        shaderProgram.compile()
    }
}