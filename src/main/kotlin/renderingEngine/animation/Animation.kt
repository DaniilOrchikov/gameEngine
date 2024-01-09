package renderingEngine.animation

import renderingEngine.Renderable
import renderingEngine.Image
import java.io.File

class Animation : Renderable {
    private val images: ArrayList<Image>
    private val count: Int
    private var currentImageIndex = 0
    private var currentImage: Image
    /** Завершился ли круг */
    var end = false
    /** Если установлен в true, то анимация после прохождения полного круга будет начинаться заново */
    val isLooped: Boolean
    /** Если isLooped = false, то после прохождения полного круга будет играть анимация с таким именем (анимация с этим именем должна быть добавлена в ту же AnimationModel) */
    var nextAnimationName: String? = null
    val width: Int
        get() = currentImage.width
    val height: Int
        get() = currentImage.height

    constructor(images: ArrayList<Image>, isLooped: Boolean) {
        require(images.isNotEmpty()) { "An empty list of images" }
        this.images = images
        count = images.size
        currentImage = images[0]
        this.isLooped = isLooped
    }

    constructor(path: String, isLooped: Boolean) {
        images = ArrayList()
        val folder = File(path)
        require(folder.exists()) { "The specified path could not be found" }
        val files = folder.listFiles()!!
        require(files.isNotEmpty()) { "There are no files in the folder" }
        for (file in files) {
            if (file.isFile) {
                images.add(Image(path + "/" + file.name))
            }
        }
        require(images.isNotEmpty()) { "There are no matching files in the folder" }
        count = images.size
        currentImage = images[0]
        this.isLooped = isLooped
    }

    fun next() {
        end = false
        currentImageIndex += 1
        currentImage = images[currentImageIndex]
        if (currentImageIndex + 1 >= count) {
            end = true
            currentImageIndex = -1
        }
    }

    override fun draw(x: Float, y: Float) {
        currentImage.draw(x, y)
        next()
    }

    override fun close() {
        for (image in images) image.close()
    }

    /** Сброс к первому кадру */
    fun reset() {
        currentImageIndex = -1
        currentImage = images[0]
    }

    override fun flip(horizontal: Boolean, vertical: Boolean) {
        for (image in images) image.flip(horizontal, vertical)
    }
}