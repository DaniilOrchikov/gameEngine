package physics

import org.joml.Vector2f
import org.joml.Vector3f

class Collider(
    private val gameObjectPos: Vector3f,
    val width: Float,
    val height: Float,
    val shift: Vector2f = Vector2f(-width / 2, -height / 2),
    val movable:Boolean = false
) {
    val movement = Vector2f(0f, 0f)
    fun moveX(){
        gameObjectPos.x += movement.x
    }
    fun moveY(){
        gameObjectPos.y += movement.y
    }
    fun getPos(): Vector2f {
        return Vector2f(gameObjectPos.x, gameObjectPos.y).add(shift)
    }

    fun setPosX(x:Float){
        gameObjectPos.x = x - shift.x
    }
    fun setPosY(y:Float){
        gameObjectPos.y = y - shift.y
    }

    fun collide(collider: Collider): Boolean {
        return this.getPos().x < collider.getPos().x + collider.width &&
                this.getPos().x + this.width > collider.getPos().x &&
                this.getPos().y < collider.getPos().y + collider.height &&
                this.getPos().y + this.height > collider.getPos().y
    }
}
