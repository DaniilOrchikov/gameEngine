package renderingEngine

import java.io.File

object SimpleImageShader {
    val shaderProgram: ShaderProgram = ShaderProgram()

    init{
        shaderProgram.setVertexShader(File("src/main/GLSL/image.vert").readText())
        shaderProgram.setFragmentShader(File("src/main/GLSL/image.frag").readText())
        shaderProgram.compile()
    }
}