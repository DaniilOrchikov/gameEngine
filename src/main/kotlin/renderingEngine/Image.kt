package renderingEngine

import util.WindowInfo
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import util.Injector
import java.io.File
import javax.imageio.ImageIO

enum class TextureFilter(val glValue: Int) {
    LINEAR(GL30.GL_LINEAR), // сглаживание при увеличении разрешения
    NEAREST(GL30.GL_NEAREST) // пикселизация при увеличении разрешения
}

class Image : Renderable {
    private val shaderProgram = SimpleImageShader
    private val texture: Int = GL30.glGenTextures()
    private val vao = GL30.glGenVertexArrays()
    private val vbo = GL15.glGenBuffers()
    private val ebo = GL15.glGenBuffers()
    var width = 0
    var height = 0
    private var screenWidth = Injector.getService(WindowInfo::class.java).width
    private var screenHeight = Injector.getService(WindowInfo::class.java).height
    private lateinit var imageData: ImageData
    private var imageMapping = floatArrayOf(1f, 1f, 1f, 0f, 0f, 0f, 0f, 1f)

    constructor(path: String) {
        initTexture(path)
        this.width = imageData.width
        this.height = imageData.height
        initBuffers()
    }

    constructor(imageData: ImageData, width: Int, height: Int) {
        this.width = width
        this.height = height
        initTexture(imageData)
        initBuffers()
    }

    private fun initTexture(imageData: ImageData) {
        this.imageData = imageData
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT)
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT)
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, imageData.width, imageData.height, 0, GL30.GL_RGBA, GL30.GL_FLOAT, imageData.data)
        GL30.glGenerateMipmap(texture)
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0)
        setFilter(TextureFilter.LINEAR)
    }

    private fun initTexture(path: String) = initTexture(getImageData(path))

    private fun initBuffers() {
        val indices = intArrayOf(
            0, 1, 2,
            0, 2, 3
        )
        GL30.glBindVertexArray(vao)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL30.glEnableVertexAttribArray(0)
        GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 20, 0)
        GL30.glEnableVertexAttribArray(1)
        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 20, 12)

        GL15.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL30.glBindVertexArray(0)
    }

    /** Отражает картинку по горизонтали и/или вертикали */
    override fun flip(horizontal:Boolean, vertical:Boolean){
        if (horizontal){
            imageMapping[0] = imageMapping[6].also { imageMapping[6] = imageMapping[0]}
            imageMapping[1] = imageMapping[7].also { imageMapping[7] = imageMapping[1]}
            imageMapping[2] = imageMapping[4].also { imageMapping[4] = imageMapping[2]}
            imageMapping[3] = imageMapping[5].also { imageMapping[5] = imageMapping[3]}
        }
        if (vertical){
            imageMapping[0] = imageMapping[2].also { imageMapping[2] = imageMapping[0]}
            imageMapping[1] = imageMapping[3].also { imageMapping[3] = imageMapping[1]}
            imageMapping[4] = imageMapping[6].also { imageMapping[6] = imageMapping[4]}
            imageMapping[5] = imageMapping[7].also { imageMapping[7] = imageMapping[5]}
        }
    }

    override fun draw(x: Float, y: Float) {
        val posX = x / (screenWidth / 2f) - 1f
        val posY = -(y / (screenHeight / 2f) - 1f)
        val vertices = floatArrayOf(
            posX + width / (screenWidth / 2f), posY - height / (screenHeight / 2f), 0f, imageMapping[0], imageMapping[1],
            posX + width / (screenWidth / 2f), posY, 0f, imageMapping[2], imageMapping[3],
            posX, posY, 0f, imageMapping[4], imageMapping[5],
            posX, posY - height / (screenHeight / 2f), 0f, imageMapping[6], imageMapping[7]
        )

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STREAM_DRAW)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

        shaderProgram.shaderProgram.use()
        GL30.glActiveTexture(GL30.GL_TEXTURE0)
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture)
        shaderProgram.shaderProgram.uniform1i("image", 0)
        GL30.glBindVertexArray(vao)
        GL30.glDrawElements(GL30.GL_TRIANGLES, 6, GL30.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)
    }

    override fun close() {
        GL30.glDeleteVertexArrays(vao)
        GL30.glDeleteBuffers(vbo)
        GL30.glDeleteBuffers(ebo)
    }

    /** Изменение размера изображения с указанием новых размеров */
    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
    /** Изменение размера изображения с указанием коэффициента масштабирования */
    fun resize(scale: Double) = resize((width * scale).toInt(), (height * scale).toInt())

    /** Создание копии изображения с измененными размерами */
    fun getResizedCopy(width: Int, height: Int): Image {
        return Image(imageData, width, height)
    }
    fun getResizedCopy(scale: Double) = getResizedCopy((width * scale).toInt(), (height * scale).toInt())

    fun setFilter(filter: TextureFilter){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, filter.glValue)
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, filter.glValue)
        GL11.glBindTexture(GL30.GL_TEXTURE_2D, 0)
    }
}

/**
 * Позволяет получить одномерный массив цветов вида [r, g, b, a, r, g, b, a, ...] из файла
 */
fun getImageData(imagePath: String): ImageData {
    val image = ImageIO.read(File(imagePath))
    val width = image.width
    val height = image.height
    val colorData = FloatArray(width * height * 4)
    val pixelData = IntArray(width * height)

    image.getRGB(0, 0, width, height, pixelData, 0, width)
    for (i in pixelData.indices) {
        val argb = pixelData[i]
        val alpha = (argb shr 24) and 0xFF
        val red = (argb shr 16) and 0xFF
        val green = (argb shr 8) and 0xFF
        val blue = argb and 0xFF

        colorData[i * 4] = red / 255f
        colorData[i * 4 + 1] = green / 255f
        colorData[i * 4 + 2] = blue / 255f
        colorData[i * 4 + 3] = alpha / 255f
    }
    return ImageData(colorData, width, height)
}
