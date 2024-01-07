package physics

import org.joml.Vector2f
import org.joml.Vector3f

class Collider(
    private val gameObjectPos: Vector3f,
    val width: Float,
    val height: Float,
    private val shift: Vector2f = Vector2f(0f, 0f),
    val movable: Boolean = false
) {
    var top: Float
        get() {
            return getPos().y
        }
        set(value) {
            setPosY(value)
        }
    var bottom: Float
        get() {
            return getPos().y + height
        }
        set(value) {
            setPosY(value - height)
        }
    var right: Float
        get() {
            return getPos().x + width
        }
        set(value) {
            setPosX(value - width)
        }

    var left: Float
        get() {
            return getPos().x
        }
        set(value) {
            setPosX(value)
        }

    init {
        shift.sub(width / 2, height / 2)
    }

    val movement = Vector2f(0f, 0f)
    fun moveX() {
        gameObjectPos.x += movement.x
    }

    fun moveY() {
        gameObjectPos.y += movement.y
    }

    fun getPos(): Vector2f {
        return Vector2f(gameObjectPos.x, gameObjectPos.y).add(shift)
    }

    fun setPosX(x: Float) {
        gameObjectPos.x = x - shift.x
    }

    fun setPosY(y: Float) {
        gameObjectPos.y = y - shift.y
    }

    fun collide(collider: Collider): Boolean {
        return this.getPos().x < collider.getPos().x + collider.width &&
                this.getPos().x + this.width > collider.getPos().x &&
                this.getPos().y < collider.getPos().y + collider.height &&
                this.getPos().y + this.height > collider.getPos().y
    }
}
