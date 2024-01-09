import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback

/**
 * Позволяет привязать функции к клавишам
 */
object KeyInputHandler : GLFWKeyCallback() {
    private val pressEvents = HashMap<Int, () -> Unit>()
    private val releaseEvents = HashMap<Int, () -> Unit>()
    private val holdEvents = HashMap<Int, HoldEvent>()

    /** Нажатие на клавишу */
    fun addPressHandler(button: Int, function: () -> Unit) {
        pressEvents[button] = function
    }

    /** Отжатие клавиши */
    fun addReleaseHandler(button: Int, function: () -> Unit) {
        releaseEvents[button] = function
    }

    /** Удержание клавиши */
    fun addHoldHandler(button: Int, function: () -> Unit) {
        holdEvents[button] = HoldEvent(function, false)
    }

    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (action == GLFW.GLFW_PRESS) {
            for (pair in pressEvents)
                if (key == pair.key)
                    pair.value()
            for (pair in holdEvents){
                if (key == pair.key){
                    pair.value.hold = true
                }
            }
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                GLFW.glfwSetWindowShouldClose(window, true)
            }
        }
        if (action == GLFW.GLFW_RELEASE){
            for (pair in releaseEvents)
                if (key == pair.key)
                    pair.value()
            for (pair in holdEvents){
                if (key == pair.key){
                    pair.value.hold = false
                }
            }
        }
    }

    fun updateHoldEvents(){
        for (pair in holdEvents){
            if (pair.value.hold){
                pair.value.function()
            }
        }
    }
}

data class HoldEvent(val function: () -> Unit, var hold:Boolean)