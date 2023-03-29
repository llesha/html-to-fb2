package core

expect open class TraversableElement(content: Any? = null) {

    fun getChildren(): List<TraversableElement>
    fun getTag(): String
    fun getText(): Triple<String, Set<String>, Boolean>
    fun getAttributes(): Map<String, String>
    fun getLinks(): MutableSet<String>
}
