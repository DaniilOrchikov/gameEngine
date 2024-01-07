package gameObject

import org.joml.Vector3f
import renderingEngine.Image
import renderingEngine.RenderManager

class GameObject(x: Float = 0f, y: Float = 0f, z: Float = 0f) {
    val pos = Vector3f(x, y, z)
    var image: Image? = null
        set(value) {
            field = value
            RenderManager.addObject(this)
        }

    fun flip(horizontal: Boolean, vertical: Boolean) {
        RenderManager.getAnimationModel(this)!!.flip(horizontal, vertical)
    }
}