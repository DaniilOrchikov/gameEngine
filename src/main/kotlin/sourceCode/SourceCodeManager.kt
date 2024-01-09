package sourceCode

import gameObject.GameObject

object SourceCodeManager {
    val sourceCodeList = ArrayList<Pair<GameObject, ISourceCode>>()

    fun addCode(gameObject: GameObject, createSourceCode: () -> ISourceCode) {
        sourceCodeList.add(Pair(gameObject, createSourceCode()))
    }

    internal fun init() {
        for (pair in sourceCodeList) {
            pair.second.init(pair.first)
        }
    }

    internal fun update() {
        for (pair in sourceCodeList) {
            pair.second.update()
        }
    }

    /** Удаляет все скрипты которые привязаны к объекту */
    fun deleteAllCodeForObject(gameObject: GameObject) {
        val deletedItems = HashSet<Pair<GameObject, ISourceCode>>()
        for (sc in sourceCodeList)
            if (sc.first == gameObject) {
                sc.second.destroy()
                deletedItems.add(sc)
            }
        sourceCodeList.removeAll(deletedItems)
    }

    internal fun close() {
        for (pair in sourceCodeList) {
            pair.second.destroy()
        }
    }
}