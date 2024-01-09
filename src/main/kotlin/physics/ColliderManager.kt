package physics

import gameObject.GameObject

object ColliderManager {
    private val colliders = HashMap<GameObject, Collider>()

    fun addCollider(gameObject: GameObject, collider: Collider) {
        colliders[gameObject] = collider
    }

    fun getColliders(): MutableCollection<Collider> {
        return colliders.values
    }

    fun getCollider(gameObject: GameObject): Collider?{
        return colliders[gameObject]
    }

    fun deleteCollider(gameObject: GameObject):Boolean {
        if (getCollider(gameObject) == null) return false
        colliders.remove(gameObject)
        return true
    }
}