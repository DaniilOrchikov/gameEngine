package renderingEngine

import gameObject.GameObject
import org.joml.Vector2f
import util.Injector
import util.WindowInfo

object Camera {
    private lateinit var target:GameObject
    val shift: Vector2f = Vector2f(0f,0f)
    fun setTarget(gameObject: GameObject){
        this.target = gameObject
    }

    fun getPos(): Vector2f {
        return target.let { Vector2f(it.pos.x - Injector.getService(WindowInfo::class.java).width / 2, it.pos.y - Injector.getService(WindowInfo::class.java).height / 2).add(shift) }
    }
}