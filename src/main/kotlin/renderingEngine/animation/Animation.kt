package renderingEngine.animation

import renderingEngine.Renderable
import renderingEngine.Image
import java.io.File

class Animation : Renderable {
    private val images: ArrayList<Image>
    val count: Int
    var currentImageIndex = 0
    var currentImage: Image
    var end = false
    var nextAnimationName: String? = null
    val isLooped: Boolean

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

    fun reset() {
        currentImageIndex = 0
        currentImage = images[0]
    }
}