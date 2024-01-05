package renderingEngine

import gameObject.GameObject
import fill
import renderingEngine.animation.AnimationModel

object RenderManager {
    private val RenderableObjects = HashMap<GameObject, AnimationModel?>()

    fun addObject(gameObject: GameObject){
        RenderableObjects[gameObject] = null
    }

    fun addAnimationModelToGameObject(gameObject: GameObject, model: AnimationModel) {
        RenderableObjects[gameObject] = model
    }

    fun getAnimationModel(gameObject: GameObject): AnimationModel?{
        return RenderableObjects[gameObject]
    }

    fun render() {
        fill(BLACK)
        for (pair in RenderableObjects) {
            if (pair.value != null && pair.value!!.hasWorkingAnimation) {
                pair.value!!.draw(
                    pair.key.pos.x - pair.value!!.currentAnimation!!.currentImage.width / 2 - Camera.getPos().x,
                    pair.key.pos.y - pair.value!!.currentAnimation!!.currentImage.height / 2 - Camera.getPos().y
                )
            } else if (pair.key.image != null)
                pair.key.image!!.draw(
                    pair.key.pos.x - pair.key.image!!.width / 2 - Camera.getPos().x,
                    pair.key.pos.y - pair.key.image!!.height / 2 - Camera.getPos().y
                )
        }
    }

    fun close() {
        for (pair in RenderableObjects) {
            if (pair.key.image != null) pair.key.image!!.close()
        }
    }
}