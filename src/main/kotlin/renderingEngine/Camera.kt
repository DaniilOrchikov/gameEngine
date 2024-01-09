package renderingEngine

import gameObject.GameObject
import org.joml.Vector2f
import org.joml.Vector4f
import util.Injector
import util.WindowInfo

/**
 * Следует за GameObject-ом к которому ее привязали
 */
object Camera {
    private lateinit var target: GameObject
    val shift: Vector2f = Vector2f(0f, 0f)
    private val windowInfo = Injector.getService(WindowInfo::class.java)
    private val boundingRect = Vector4f(Float.NaN, Float.NaN, Float.NaN, Float.NaN)
    fun setTarget(gameObject: GameObject) {
        this.target = gameObject
    }

    fun getPos(): Vector2f {
        var x: Float
        var y: Float
        target.let {
            x = it.pos.x - windowInfo.width / 2
            y = it.pos.y - windowInfo.height / 2
        }
        val pos = Vector2f(x, y).add(shift)
        if (!boundingRect.x.isNaN())
            if (x < boundingRect.x)
                x = boundingRect.x
        if (!boundingRect.z.isNaN())
            if (x + windowInfo.width > boundingRect.z)
                x = boundingRect.z - windowInfo.width

        if (boundingRect.y.isNaN())
            if (y < boundingRect.y)
                y = boundingRect.y
        if (!boundingRect.w.isNaN())
            if (y + windowInfo.height > boundingRect.w)
                y = boundingRect.w - windowInfo.height
        pos.x = x
        pos.y = y

        return pos
    }

    /** Позволяет установить границы за которые камера не сможет заходить */
    fun setBoundingRect(left: Float = Float.NaN, right: Float = Float.NaN, up:Float = Float.NaN, down:Float = Float.NaN) {
        boundingRect.x = left
        boundingRect.y = up
        boundingRect.z = right
        boundingRect.w = down
    }
}