package renderingEngine

interface Renderable {
    fun draw(x: Float, y: Float)
    fun close()
    fun flip(horizontal: Boolean, vertical: Boolean)
}