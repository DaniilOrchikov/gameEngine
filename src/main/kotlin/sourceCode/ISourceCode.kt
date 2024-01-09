package sourceCode

import gameObject.GameObject

/**
 * Необходимо имплементировать для создания пользовательского кода
 */
interface ISourceCode {
    /** Вызывается при создании объекта */
    fun init(gameObject: GameObject)
    /** Вызывается каждый кадр пока объект существует */
    fun update()
    /** Вызывается при уничтожении объекта */
    fun destroy()
}