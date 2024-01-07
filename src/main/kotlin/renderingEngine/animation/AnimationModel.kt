package renderingEngine.animation

import renderingEngine.Renderable

class AnimationModel : Renderable {
    private val animations = HashMap<String, Animation>()
    var currentAnimation: Animation? = null
    private var nextAnimation: Animation? = null
    var hasWorkingAnimation = false

    fun addAnimation(animation: Animation, name: String) {
        animations[name] = animation
    }

    fun getAnimation(name: String): Animation?{
        return animations[name]
    }

    fun link(firstAnimationName: String, secondAnimationName: String) {
        require(animations.containsKey(firstAnimationName) && animations.containsKey(secondAnimationName))
        animations[firstAnimationName]?.nextAnimationName = secondAnimationName
    }

    fun play(animationName: String, wait: Boolean = false) {
        if (!wait) currentAnimation = animations[animationName]
        else nextAnimation = animations[animationName]
        hasWorkingAnimation = true
    }

    override fun draw(x: Float, y: Float) {
        require(currentAnimation != null)
        currentAnimation!!.draw(x, y)
        if (currentAnimation!!.end) {
            if (nextAnimation != null) {
                currentAnimation!!.reset()
                currentAnimation = nextAnimation
                nextAnimation = null
            }
            else if (!currentAnimation!!.isLooped) {
                if (currentAnimation!!.nextAnimationName != null) currentAnimation =
                    animations[currentAnimation!!.nextAnimationName]
                else {
                    currentAnimation = null
                    hasWorkingAnimation = false
                }
            }
        }
    }

    override fun close() {
        for (animationName in animations) animations.clone()
    }
    override fun flip(horizontal:Boolean, vertical:Boolean){
        for (animation in animations.values) animation.flip(horizontal, vertical)
    }
}