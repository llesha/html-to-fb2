package core

class MetaInformation(private val head: TraversableElement) {
    val title = head.getChildren().find { it.getTag() == "title" }?.getText()?.first

    val author = findValue("author")
    val description = findValue("description")
    val language = findValue("lang") ?: findValue("language")

    fun findValueFrom(set: Set<String>): String? {
        return head.getChildren().find {
            set.contains(it.getAttributes()["name"])
        }?.getAttributes()?.get("content")
    }

    fun findValue(value: String): String? {
        return head.getChildren().find {
            it.getAttributes()["name"] == value
        }?.getAttributes()?.get("content")
    }
}