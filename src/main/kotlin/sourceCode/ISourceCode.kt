package sourceCode

import gameObject.GameObject

interface ISourceCode {
    fun init(gameObject: GameObject)
    fun update()
    fun close()
}