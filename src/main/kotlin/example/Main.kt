package example

import createDisplay
import gameObject.*
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW
import physics.*
import renderingEngine.*
import renderingEngine.animation.*
import sourceCode.*
import startGameCycle

val map = arrayOf( // Это лишь пример. Так делать карту локации категорически не рекомендуется
    IntArray(30) { 1 },
    IntArray(30) { 2 },
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(26) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(16) { 0 } + intArrayOf(1, 1) + IntArray(8) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(15) { 0 } + intArrayOf(1, 2, 2, 1, 1) + IntArray(6) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2) + IntArray(4) { 0 } + intArrayOf(1, 1, 1, 1, 1) + IntArray(17) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2, 0, 0, 0) + intArrayOf(1, 2, 2, 2, 2, 2, 1, 1, 1) + IntArray(14) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2, 0, 0) + intArrayOf(1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1) + IntArray(13) { 0 } + intArrayOf(2, 2),
    intArrayOf(2, 2, 1, 1) + intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2) + IntArray(13) { 1 } + intArrayOf(2, 2),
    IntArray(30) { 2 }
)

fun main() {
    createDisplay(1000, 700)

    val player = GameObjectManager.createGameObject(500f, 300f)
    val animationModel = AnimationModel()
    RenderManager.addAnimationModelToGameObject(player, animationModel)

    val idleAnimation = Animation("src/main/resources/idle", true)
    animationModel.addAnimation(idleAnimation, "idle")
    animationModel.play("idle")

    val runAnimation = Animation("src/main/resources/run", true)
    animationModel.addAnimation(runAnimation, "run")

    val collider = Collider(player.pos, 75f, 80f, Vector2f(0f, 2.5f), movable = true)
    ColliderManager.addCollider(player, collider)
    Camera.shift.y = -150f
    Camera.setTarget(player)

    val blockImage = Image("src/main/resources/Block.png")
    val dirtImage = Image("src/main/resources/Dirt.png")
    for (i in map.indices)
        for (j in map[i].indices){
            if (map[i][j] != 0){
                val go = GameObjectManager.createGameObject(j * 40f, i * 40f)
                go.image = if (map[i][j] == 1) blockImage else dirtImage
                RenderManager.addObject(go)
                val col = Collider(go.pos, 40f, 40f)
                ColliderManager.addCollider(go, col)
            }
        }

    SourceCodeManager.addCode(player) { Code() }

    startGameCycle()
}


class Code : ISourceCode {
    private lateinit var gameObject: GameObject
    private var jumpImpulse = 0f
    var dir = true

    override fun init(gameObject: GameObject) {
        this.gameObject = gameObject
        KeyInputHandler.addPressHandler(GLFW.GLFW_KEY_SPACE) {
            jumpImpulse += 60f
        }
        KeyInputHandler.addHoldHandler(GLFW.GLFW_KEY_RIGHT) {
            ColliderManager.getCollider(gameObject)!!.movement.x += 5
            RenderManager.getAnimationModel(gameObject)!!.play("run")
        }
        KeyInputHandler.addHoldHandler(GLFW.GLFW_KEY_LEFT) {
            ColliderManager.getCollider(gameObject)!!.movement.x -= 5
            RenderManager.getAnimationModel(gameObject)!!.play("run")
        }
        KeyInputHandler.addReleaseHandler(GLFW.GLFW_KEY_LEFT) {
            RenderManager.getAnimationModel(gameObject)!!.play("idle")
        }
        KeyInputHandler.addReleaseHandler(GLFW.GLFW_KEY_RIGHT) {
            RenderManager.getAnimationModel(gameObject)!!.play("idle")
        }
        KeyInputHandler.addPressHandler(GLFW.GLFW_KEY_LEFT) {
            if (dir) {
                dir = false
                gameObject.flip(horizontal = true, vertical = false)
            }
        }
        KeyInputHandler.addPressHandler(GLFW.GLFW_KEY_RIGHT) {
            if (!dir) {
                dir = true
                gameObject.flip(horizontal = true, vertical = false)
            }
        }
    }

    override fun update() {
        if (jumpImpulse > 1) jumpImpulse *= 0.9f
        else jumpImpulse = 0f
        ColliderManager.getCollider(gameObject)!!.movement.y += 20 - jumpImpulse
    }

    override fun close() {
    }
}
