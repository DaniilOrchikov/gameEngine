package physics

object Physics {
    fun update() {
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
                    currentCollider.setPosX(collider.getPos().x - currentCollider.width)
                else if (currentCollider.movement.x < 0)
                    currentCollider.setPosX(collider.getPos().x + currentCollider.width)
            }
            currentCollider.movement.x = 0f

            currentCollider.moveY()
            hitList = ArrayList()
            for (collider in colliders)
                if (currentCollider != collider && currentCollider.collide(collider))
                    hitList.add(collider)
            for (collider in hitList){
                if (currentCollider.movement.y > 0)
                    currentCollider.setPosY(collider.getPos().y - currentCollider.height)
                else if (currentCollider.movement.y < 0)
                    currentCollider.setPosY(collider.getPos().y + currentCollider.height)
            }
            currentCollider.movement.y = 0f
        }
    }
}