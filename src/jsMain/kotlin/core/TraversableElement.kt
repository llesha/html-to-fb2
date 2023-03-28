package core

actual class TraversableElement actual constructor(content: Any?) {
    actual fun getChildren(): List<TraversableElement> {
        return listOf()
    }

    actual fun getTag(): String {
        return ""
    }

    actual fun getText(): Triple<String, Set<String>, Boolean> {
        TODO("Not yet implemented")
    }

    actual fun getAttributes(): Map<String, String> {
        TODO("Not yet implemented")
    }

    actual fun getLinks(): MutableSet<String> {
        TODO("Not yet implemented")
    }
}
