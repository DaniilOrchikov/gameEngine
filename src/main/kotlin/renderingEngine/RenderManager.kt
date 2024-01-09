package renderingEngine

import gameObject.GameObject
import fill
import org.joml.Vector3f
import renderingEngine.animation.AnimationModel

/**
 * Занимается рисованием картинок и анимаций
 */
object RenderManager {
    private val renderableObjects = HashMap<GameObject, Renderable>()

    /** Добавление нового GameObject-а. Необходимо чтобы у него была установлена картинка
     * Не нужно использовать этот метод вручную. При добавлении картинки объекту, он вызывается автоматически
     */
    internal fun addObject(gameObject: GameObject) {
        require(gameObject.image != null) { "The object must contain an image" }
        renderableObjects[gameObject] = gameObject.image!!
    }

    /** Привязка AnimationModel-и к GameObject-у */
    fun addAnimationModelToGameObject(gameObject: GameObject, model: AnimationModel) {
        renderableObjects[gameObject] = model
    }

    fun getAnimationModel(gameObject: GameObject): AnimationModel? {
        val renderable = renderableObjects[gameObject]
        if (renderable is Image) return null
        return renderableObjects[gameObject] as AnimationModel
    }

    fun deleteRenderableObject(gameObject: GameObject): Boolean {
        if (getAnimationModel(gameObject) == null) return false
        renderableObjects.remove(gameObject)
        return true
    }

    internal fun render() {
        fun drawImage(image: Image, pos: Vector3f) {
            image.draw(
                pos.x - image.width / 2 - Camera.getPos().x,
                pos.y - image.height / 2 - Camera.getPos().y
            )
        }
        fill(OLIVE)
        for (pair in renderableObjects) {
            if (pair.value is AnimationModel) {
                val animModel = pair.value as AnimationModel
                if (!animModel.hasWorkingAnimation)
                    drawImage(pair.key.image!!, pair.key.pos)
                else
                    animModel.draw(
                        pair.key.pos.x - animModel.currentAnimation!!.width / 2 - Camera.getPos().x,
                        pair.key.pos.y - animModel.currentAnimation!!.height / 2 - Camera.getPos().y
                    )
            } else if (pair.value is Image) {
                drawImage(pair.value as Image, pair.key.pos)
            }
        }
    }

    internal fun close() {
        for (pair in renderableObjects) {
            if (pair.key.image != null) pair.key.image!!.close()
        }
    }

    fun flip(horizontal: Boolean, vertical: Boolean) {
        for (o in renderableObjects.values) o.flip(horizontal, vertical)
    }

}