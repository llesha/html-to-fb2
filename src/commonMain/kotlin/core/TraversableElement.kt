package core

expect class TraversableElement(content: Any? = null) {

    fun getChildren(): List<TraversableElement>
    fun getTag(): String
    fun getText(): String
    fun getAttributes(): Map<String, String>
}
