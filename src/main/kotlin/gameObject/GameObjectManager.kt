package gameObject

import physics.ColliderManager
import renderingEngine.RenderManager
import sourceCode.SourceCodeManager

/**
 * Позволяет создавать и удалять объекты
 */
object GameObjectManager {
    private val objects = ArrayList<GameObject>()

    fun createGameObject(x: Float = 0f, y: Float = 0f): GameObject {
        val gameObject = GameObject(x, y)
        objects.add(gameObject)
        return gameObject
    }

    /**
     * Удаляет объект и все привязанные к нему компоненты
     */
    fun destroyGameObject(gameObject: GameObject) {
        objects.remove(gameObject)
        RenderManager.deleteRenderableObject(gameObject)
        ColliderManager.deleteCollider(gameObject)
        SourceCodeManager.deleteAllCodeForObject(gameObject)
    }
}