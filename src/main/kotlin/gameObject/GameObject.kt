package gameObject

import org.joml.Vector3f
import renderingEngine.Image

class GameObject(x: Float = 0f, y: Float = 0f, z: Float = 0f) {
    val pos = Vector3f(x, y, z)
    var image: Image? = null
}