package gameObject

import renderingEngine.RenderManager

object GameObjectManager {
    private val objects = ArrayList<GameObject>()

    fun createGameObject(x:Float=0f, y:Float =0f): GameObject {
        val gameObject = GameObject(x, y)
        objects.add(gameObject)
        RenderManager.addObject(gameObject)
        return gameObject
    }
}