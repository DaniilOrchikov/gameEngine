package physics

/**
 * Объект который рассчитывает физику для всех GameObject-ов
 * Пока работает только с физикой прямоугольников, выравненных по координатным осям
 */
internal object Physics {
    internal fun update() {
        val colliders = ColliderManager.getColliders()
        for (currentCollider in colliders) {
            if (!currentCollider.movable) continue
            currentCollider.moveX()
            var hitList = ArrayList<Collider>()
            for (collider in colliders)
                if (currentCollider != collider && currentCollider.collide(collider))
                    hitList.add(collider)
            for (collider in hitList){
                if (currentCollider.movement.x > 0)
                    currentCollider.right = collider.left
                else if (currentCollider.movement.x < 0)
                    currentCollider.left = collider.right
            }
            currentCollider.movement.x = 0f

            currentCollider.moveY()
            hitList = ArrayList()
            for (collider in colliders)
                if (currentCollider != collider && currentCollider.collide(collider))
                    hitList.add(collider)
            for (collider in hitList){
                if (currentCollider.movement.y > 0)
                    currentCollider.bottom = collider.top
                else if (currentCollider.movement.y < 0)
                    currentCollider.top = collider.bottom
            }
            currentCollider.movement.y = 0f
        }
    }
}