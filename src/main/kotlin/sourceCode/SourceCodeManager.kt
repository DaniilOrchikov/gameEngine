package sourceCode

import gameObject.GameObject

object SourceCodeManager {
    val sourceCodeList = ArrayList<Pair<GameObject, ISourceCode>>()

    fun addCode(gameObject: GameObject, createSourceCode: () -> ISourceCode){
        sourceCodeList.add(Pair(gameObject, createSourceCode()))
    }

    fun init(){
        for (pair in sourceCodeList){
            pair.second.init(pair.first)
        }
    }

    fun update(){
        for (pair in sourceCodeList){
            pair.second.update()
        }
    }

    fun close(){
        for (pair in sourceCodeList){
            pair.second.close()
        }
    }
}