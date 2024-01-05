import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import physics.Physics
import renderingEngine.*
import util.Injector
import util.WindowInfo
import java.lang.Thread.sleep


/** ссылка на окно  */
private var window: Long = 0

fun createDisplay(width: Int, height: Int, title: String = "") {

    // Инициализация GLFW. Большинство функций GLFW не будут работать перед этим
    glfwInit()

    // Настройка GLFW
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // окно будет скрыто после создания
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE) // окно будет изменяемого размера
    // Задается минимальная требуемая версия OpenGL.
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3) // Мажорная
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3) // Минорная
    // Установка профайла для которого создается контекст
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

    // создание окна
    window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
    if (window == MemoryUtil.NULL) throw RuntimeException("Ошибка создания GLFW окна")
    val windowInfo = WindowInfo(width, height, window)
    Injector.addService(WindowInfo::class.java, windowInfo)

    glfwSetKeyCallback(window, KeyInputHandler)

    // Получаем разрешение основного монитора/экрана
    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
    // устанавливаем окно в центр экрана
    glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2)

    // Делаем контекст OpenGL текущим
    glfwMakeContextCurrent(window)

    // Эта строка критически важна для взаимодействия LWJGL с контекстом GLGW OpenGL
    // или любым контекстом, который управляется извне. LWJGL обнаруживает
    // текущий контекст в текущем потоке, создает экземпляр GLCapabilities
    // и делает привязки OpenGL доступными для использования.
    GL.createCapabilities()

    // показываем окно
    glfwShowWindow(window)
}

private var FPS = 60
private var singleFrameTime = 1.0 / FPS
private var lastTime = 0.0
private var currentTime = 0.0
private var sleepTime:Long = 0

fun setFPS(fps: Int){
    FPS = fps
    singleFrameTime = 1.0 / FPS
}

fun startGameCycle(){
    while (shouldDisplayClose()) {
        currentTime = glfwGetTime()
        if(currentTime - lastTime < singleFrameTime){
            sleepTime = ((singleFrameTime - (currentTime - lastTime)) * 1000).toLong()
            sleep(sleepTime)
        }
        lastTime = glfwGetTime()
        KeyInputHandler.updateHoldEvents()
        Physics.update()
        RenderManager.render()
        updateDisplay()
    }
    closeResources()
}

fun closeResources(){
    RenderManager.close()
    SimpleDrawer.close()
    closeDisplay()
}

fun updateDisplay() {
    glfwSwapBuffers(window) // поменяем цветовые буферы
    // Проверяет были ли вызваны какие либо события (вроде ввода с клавиатуры или перемещение мыши)
    glfwPollEvents()
}

fun shouldDisplayClose(): Boolean {
    return !glfwWindowShouldClose(window)
}

fun closeDisplay() {
    glfwWindowShouldClose(window) // Освобождаем обратные вызовы окна
    glfwDestroyWindow(window) // Уничтожаем окно
    glfwTerminate() // Завершаем GLFW. Очистка выделенных нам ресурсов
}

// Функция для заполнения экрана определенным цветом
fun fill(r: Float, g: Float, b: Float, a: Float = 1f) {
    GL30.glClearColor(r, g, b, a)
    GL30.glClear(GL30.GL_COLOR_BUFFER_BIT)
}

fun fill(color: Color) = fill(color.r, color.g, color.b)

fun fill(r: Int, g: Int, b: Int, a: Int = 255) =
    fill(r.toFloat() / 255, g.toFloat() / 255, b.toFloat() / 255, a.toFloat() / 255)

fun fill(hexColor: String) {
    val color = Color(hexColor)
    fill(color)
}



