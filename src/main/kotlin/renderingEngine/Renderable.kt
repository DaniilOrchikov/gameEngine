package renderingEngine

interface Renderable {
    fun draw(x: Float, y: Float)
    fun close()
}